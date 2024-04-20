package it.unimib.communimib.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.communimib.R;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.User;
import it.unimib.communimib.util.Constants;

public class TestActivity extends AppCompatActivity {

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

        DatabaseReference databaseReference = FirebaseDatabase.getInstance(Constants.DATABASE).getReference().child(Constants.REPORTS_PATH);
        User user = new User("uid", "luca@campus.unimib.it", "luca", "pinciroli", false);
        user.setUid("uid");
        Report report = new Report("guasto ascensore", "l'ascensore principale è rotto",
                "U14", "guasto", user);
        databaseReference.child("rid").setValue(report);

    }
}