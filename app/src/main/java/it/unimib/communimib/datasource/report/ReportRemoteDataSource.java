package it.unimib.communimib.datasource.report;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.util.Constants;
import it.unimib.communimib.util.ErrorMapper;

public class ReportRemoteDataSource implements IReportRemoteDataSource {
    private final DatabaseReference databaseReference;
    private final List<ChildEventListener> currentListeners;
    private final List<DatabaseReference> currentReferences;

    public ReportRemoteDataSource(){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance(Constants.DATABASE).getReference();
        currentListeners = new ArrayList<>();
        currentReferences = new ArrayList<>();
    }

    @Override
    public void readAllReports(Callback addedCallback,
                               Callback changedCallback,
                               Callback removedCallback,
                               Callback cancelledCallback){
        removeAllQueryListeners();
        Query query = databaseReference
                .child(Constants.REPORTS_PATH)
                .limitToFirst(30);
        currentReferences.add(query.getRef());
        currentListeners.add(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Report report = snapshot.getValue(Report.class);
                report.setRid(snapshot.getKey());

                addedCallback.onComplete(new Result.ReportSuccess(report));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Report report = snapshot.getValue(Report.class);
                report.setRid(snapshot.getKey());
                changedCallback.onComplete(new Result.ReportSuccess(report));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Report report = snapshot.getValue(Report.class);
                report.setRid(snapshot.getKey());
                removedCallback.onComplete(new Result.ReportSuccess(report));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // per ora niente, nel caso aggiungere una callback
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                cancelledCallback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_GET_ERROR)); //TODO: capire se qui bisogna mettere un errore specifico
            }
        });

        query.addChildEventListener(currentListeners.get(0));
    }

    @Override
    public void readReportsByBuildings(List<String> buildings,
                                       Callback addedCallback,
                                       Callback changedCallback,
                                       Callback removedCallback,
                                       Callback cancelledCallback){
        removeAllQueryListeners();
        for (String building : buildings) {
            addQueryListener("building", building, addedCallback, changedCallback, removedCallback, cancelledCallback);
        }

    }

    @Override
    public void readReportsByTitleAndDescription(String keyword,
                             Callback addedCallback,
                             Callback changedCallback,
                             Callback removedCallback,
                             Callback cancelledCallback){
        removeAllQueryListeners();
        Query query = databaseReference
                .child(Constants.REPORTS_PATH);
        currentReferences.add(query.getRef());
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Report report = snapshot.getValue(Report.class);
                report.setRid(snapshot.getKey());
                String title = report.getTitle().toLowerCase();
                String description = report.getDescription().toLowerCase();
                if(title.contains(keyword.toLowerCase()) || description.contains(keyword.toLowerCase())){
                    addedCallback.onComplete(new Result.ReportSuccess(report));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Report report = snapshot.getValue(Report.class);
                report.setRid(snapshot.getKey());
                String title = report.getTitle().toLowerCase();
                String description = report.getDescription().toLowerCase();
                if(title.contains(keyword.toLowerCase()) || description.contains(keyword.toLowerCase())){
                    changedCallback.onComplete(new Result.ReportSuccess(report));
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Report report = snapshot.getValue(Report.class);
                report.setRid(snapshot.getKey());
                String title = report.getTitle().toLowerCase();
                String description = report.getDescription().toLowerCase();
                if(title.contains(keyword.toLowerCase()) || description.contains(keyword.toLowerCase())){
                    removedCallback.onComplete(new Result.ReportSuccess(report));
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // per ora niente, nel caso aggiungere una callback
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                cancelledCallback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_GET_ERROR)); //TODO: capire se qui bisogna mettere un errore specifico
            }
        };

        currentListeners.add(listener);
        query.addChildEventListener(listener);
    }

    @Override
    public void readReportsByUID(String author,
                                 Callback addedCallback,
                                 Callback changedCallback,
                                 Callback removedCallback,
                                 Callback cancelledCallback){
        removeAllQueryListeners();
        addQueryListener("author", author, addedCallback, changedCallback, removedCallback, cancelledCallback);
    }

    @Override
    public void createReport(Report report, Callback callback) {

        String key = databaseReference.child(Constants.REPORTS_PATH).push().getKey();

        databaseReference
                .child(Constants.REPORTS_PATH)
                .child(key)
                .setValue(report).addOnCompleteListener(reportTask -> {
                    if(reportTask.isSuccessful()) {
                        databaseReference
                                .child(Constants.USERSREPORTS_PATH)
                                .child(report.getAuthor().getUid())
                                .child(key)
                                .setValue(true)
                                .addOnCompleteListener(userReportTask -> {
                                    if(userReportTask.isSuccessful()){
                                        callback.onComplete(new Result.Success());
                                    }
                                    else{
                                        //TODO: inserire un errore più specifico qui
                                        callback.onComplete(new Result.Error(ErrorMapper.REPORT_CREATION_ERROR));
                                    }
                                });
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.REPORT_CREATION_ERROR));
                    }
                });
    }

    public void deleteReport(Report report, Callback callback){
        databaseReference
                .child(Constants.REPORTS_PATH)
                .child(report.getRid())
                .removeValue().addOnCompleteListener(reportTask -> {
                    if(reportTask.isSuccessful()){
                        databaseReference
                                .child(Constants.USERSREPORTS_PATH)
                                .child(report.getAuthor().getUid())
                                .child(report.getRid())
                                .removeValue()
                                .addOnCompleteListener(userReportTask -> {
                                    if(userReportTask.isSuccessful()){
                                        callback.onComplete(new Result.Success());
                                    }
                                    else{
                                        callback.onComplete(new Result.Error(ErrorMapper.REPORT_DELETING_ERROR));
                                    }
                                });
                    } else {
                        callback.onComplete(new Result.Error(ErrorMapper.REPORT_DELETING_ERROR));
                    }
        });
    }

    private void removeAllQueryListeners(){
        if(!currentListeners.isEmpty() && !currentReferences.isEmpty()){
            for (int i = 0; i < currentListeners.size(); i++) {
                currentReferences.get(i).removeEventListener(currentListeners.get(i));
            }
            currentListeners.clear();
            currentReferences.clear();
        }
    }

    private void addQueryListener(String path, String queryParameter,
                                  Callback addedCallback,
                                  Callback changedCallback,
                                  Callback removedCallback,
                                  Callback cancelledCallback) {
        Query query = databaseReference
                .child(Constants.REPORTS_PATH)
                .orderByChild(path)
                .equalTo(queryParameter);
        currentReferences.add(query.getRef());
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Report report = snapshot.getValue(Report.class);
                report.setRid(snapshot.getKey());
                addedCallback.onComplete(new Result.ReportSuccess(report));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Report report = snapshot.getValue(Report.class);
                report.setRid(snapshot.getKey());
                changedCallback.onComplete(new Result.ReportSuccess(report));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Report report = snapshot.getValue(Report.class);
                report.setRid(snapshot.getKey());
                removedCallback.onComplete(new Result.ReportSuccess(report));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // per ora niente, nel caso aggiungere una callback
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                cancelledCallback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_GET_ERROR)); //TODO: capire se qui bisogna mettere un errore specifico
            }
        };

        currentListeners.add(listener);
        query.addChildEventListener(listener);
    }
}
