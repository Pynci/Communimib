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
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;

import it.unimib.communimib.R;
import it.unimib.communimib.datasource.user.AuthDataSource;
import it.unimib.communimib.datasource.user.UserLocalDataSource;
import it.unimib.communimib.datasource.user.UserRemoteDataSource;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.User;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.repository.PostRepository;
import it.unimib.communimib.repository.UserRepository;
import it.unimib.communimib.util.Constants;
import it.unimib.communimib.util.ServiceLocator;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);

        ServiceLocator.getInstance().getPostRepository().createPost("Un altisonante titolo", "Descrizione molto poco lunga ed assolutamente" +
                "non consona a quello che normalmente andrebbe scritto", "Eventi",
                new User("12345", "luca@unimib.it", "Luca", "Pinciroli", true), "luca@unimib.it",
                "https://www.unimib.it/", System.currentTimeMillis(), result -> {});
    }
}