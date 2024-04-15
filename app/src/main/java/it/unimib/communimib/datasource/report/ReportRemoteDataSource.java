package it.unimib.communimib.datasource.report;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.util.Constants;
import it.unimib.communimib.util.ErrorMapper;

public class ReportRemoteDataSource implements IReportRemoteDataSource {
    private final DatabaseReference databaseReference;

    public ReportRemoteDataSource(){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance(Constants.DATABASE).getReference();
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
}
