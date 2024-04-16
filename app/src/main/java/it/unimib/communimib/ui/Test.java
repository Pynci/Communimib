package it.unimib.communimib.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import it.unimib.communimib.Callback;
import it.unimib.communimib.R;
import it.unimib.communimib.datasource.report.ReportRemoteDataSource;
import it.unimib.communimib.model.Result;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ReportRemoteDataSource remoteDataSource = new ReportRemoteDataSource();
        findViewById(R.id.test1).setOnClickListener(view -> {
            remoteDataSource.test1();
        });

        findViewById(R.id.test2).setOnClickListener(view -> {
            remoteDataSource.test2();
        });

        findViewById(R.id.test3).setOnClickListener(view -> {
            remoteDataSource.test3();
        });

        findViewById(R.id.test4).setOnClickListener(view -> {
            remoteDataSource.test4();
        });

        findViewById(R.id.ascoltau2).setOnClickListener(view -> {
            remoteDataSource.getReportsByBuilding("U2", result -> {

            });
        });

        findViewById(R.id.ascoltau24).setOnClickListener(view -> {
            remoteDataSource.getReportsByBuilding("U24", result -> {

            });
        });
    }
}