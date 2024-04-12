package it.unimib.communimib.datasource.report;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.communimib.util.Constants;

public class ReportRemoteDataSource implements IReportRemoteDataSource {
    private final DatabaseReference databaseReference;

    public ReportRemoteDataSource(){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance(Constants.DATABASE).getReference();
    }


}
