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

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.util.Constants;
import it.unimib.communimib.util.ErrorMapper;

public class ReportRemoteDataSource implements IReportRemoteDataSource {
    private final DatabaseReference databaseReference;
    private ChildEventListener currentListener;
    private DatabaseReference currentReference;

    public ReportRemoteDataSource(){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance(Constants.DATABASE).getReference();
    }

    public void getAllReports(Callback callback){

    }

    public void getReportsByBuilding(String building, Callback callback){
        removeCurrentListener();

        Query query = databaseReference
                .child(Constants.REPORTS_PATH)
                .orderByChild("building")
                .equalTo(building);
        currentReference = query.getRef();
        currentListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(this.getClass().getSimpleName(), "TRIGGER AGGIUNTA EDIFICIO: " + building);
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

        query.addChildEventListener(currentListener);
    }

    public void getReportsByCategory(String category, Callback callback){
        removeCurrentListener();

        Query query = databaseReference
                .child(Constants.REPORTS_PATH)
                .orderByChild("category")
                .equalTo(category);
        currentReference = query.getRef();
        currentListener = new ChildEventListener() {
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

        query.addChildEventListener(currentListener);
    }

    public void test1(){
        databaseReference.child(Constants.REPORTS_PATH).child("123")
                .setValue(new Report("a", "b", "U2", "asd", "Luca"));
    }

    public void test2(){
        databaseReference.child(Constants.REPORTS_PATH).child("456")
                .setValue(new Report("a", "b", "U24", "asd", "Luca"));
    }
    public void test3(){
        databaseReference.child(Constants.REPORTS_PATH).child("789")
                .setValue(new Report("a", "b", "U2", "asd", "Marco"));
    }

    public void test4(){
        databaseReference.child(Constants.REPORTS_PATH).child("000")
                .setValue(new Report("a", "b", "U2", "asd", "Giulia"));
    }

    public void getReportsByUID(){

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

    private void removeCurrentListener(){
        if(currentListener != null && currentReference != null){
            currentReference.removeEventListener(currentListener);
            currentListener = null;
            currentReference = null;
        }
    }
}
