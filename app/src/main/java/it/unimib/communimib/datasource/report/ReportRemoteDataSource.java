package it.unimib.communimib.datasource.report;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.util.Constants;
import it.unimib.communimib.util.ErrorMapper;

public class ReportRemoteDataSource implements IReportRemoteDataSource {
    private final DatabaseReference databaseReference;
    private List<ChildEventListener> currentListeners;
    private List<DatabaseReference> currentReferences;

    public ReportRemoteDataSource(){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance(Constants.DATABASE).getReference();
    }

    public void getAllReports(Callback addedCallback,
                              Callback changedCallback,
                              Callback removedCallback,
                              Callback movedCallback){
        removeCurrentListeners();
        Query query = databaseReference
                .child(Constants.REPORTS_PATH);
        currentReferences.add(query.getRef());
        currentListeners.add(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        query.addChildEventListener(currentListeners.get(0));
    }

    public void getReportsByBuilding(String[] buildings,
                                     Callback addedCallback,
                                     Callback changedCallback,
                                     Callback removedCallback,
                                     Callback movedCallback){
        removeCurrentListeners();

        for (String building :
                buildings) {
            Query query = databaseReference
                    .child(Constants.REPORTS_PATH)
                    .orderByChild("building")
                    .equalTo(building);
            currentReferences.add(query.getRef());
            ChildEventListener listener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };

            currentListeners.add(listener);
            query.addChildEventListener(listener);
        }

    }

    public void getReportsByCategory(String[] categories,
                                     Callback addedCallback,
                                     Callback changedCallback,
                                     Callback removedCallback,
                                     Callback movedCallback){
        removeCurrentListeners();

        for (String category :
                categories) {
            Query query = databaseReference
                    .child(Constants.REPORTS_PATH)
                    .orderByChild("category")
                    .equalTo(category);
            currentReferences.add(query.getRef());
            ChildEventListener listener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };

            currentListeners.add(listener);
            query.addChildEventListener(listener);
        }
    }

    public void getReportsByUID(String author,
                                Callback addedCallback,
                                Callback changedCallback,
                                Callback removedCallback,
                                Callback movedCallback){
        removeCurrentListeners();

        removeCurrentListeners();
        Query query = databaseReference
                .child(Constants.REPORTS_PATH)
                .orderByChild("author")
                .equalTo(author);
        currentReferences.add(query.getRef());
        currentListeners.add(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        query.addChildEventListener(currentListeners.get(0));
    }

    @Override
    public void addReport(Report report, Callback callback) {

        String key = databaseReference.child(Constants.REPORTS_PATH).push().getKey();

        databaseReference
                .child(Constants.REPORTS_PATH)
                .child(key)
                .setValue(report).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        callback.onComplete(new Result.Success());
                    }
                    else{
                        callback.onComplete(new Result.Error(ErrorMapper.REPORT_CREATION_ERROR));
                    }
                });
    }

    private void removeCurrentListeners(){
        if(!currentListeners.isEmpty() && !currentReferences.isEmpty()){
            for (int i = 0; i < currentListeners.size(); i++) {
                currentReferences.get(i).removeEventListener(currentListeners.get(i));
            }
            currentListeners.clear();
            currentReferences.clear();
        }
    }
}
