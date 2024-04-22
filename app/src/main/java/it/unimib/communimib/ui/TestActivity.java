package it.unimib.communimib.ui;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import it.unimib.communimib.R;
import it.unimib.communimib.datasource.user.AuthDataSource;
import it.unimib.communimib.datasource.user.UserLocalDataSource;
import it.unimib.communimib.datasource.user.UserRemoteDataSource;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.User;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.repository.UserRepository;
import it.unimib.communimib.util.Constants;
import it.unimib.communimib.util.ServiceLocator;

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

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(getApplicationContext());
        userRepository.signIn("l.pinciroli3@campus.unimib.it", "Prova123!", result -> {
            Log.d("negro", "bestia");
        });

        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        userRepository.uploadPropic(uri, result -> {
                            if(result.isSuccessful()){

                            }
                            else{
                                Log.d("negro", "mamma troia");
                            }
                        });
                    } else {
                        Log.d("negro", "vaffanculo");
                    }
                });

        findViewById(R.id.button_caricafoto).setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        findViewById(R.id.button_glide).setOnClickListener(v -> {
            Glide
                    .with(getApplicationContext())
                    .load(Uri.parse(userRepository.getCurrentUser().getPropic()))
                    .into((ImageView) findViewById(R.id.imageViewTest));
        });
    }
}