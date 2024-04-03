package it.unimib.communimib.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import it.unimib.communimib.R;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.ui.auth.AuthActivity;
import it.unimib.communimib.util.ServiceLocator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(this.getApplicationContext());

        findViewById(R.id.registrazione).setOnClickListener(view -> {
            userRepository.signUp("m.ferioli@campus.unimib.it", "pinselo", "Pueblo", "Scatarri", result -> {
                if(result.isSuccessful()){
                    Log.d(this.getClass().getSimpleName(), "REGISTRAZIONE: successo");
                }
                else{
                    Log.d(this.getClass().getSimpleName(), "REGISTRAZIONE: errore - " + ((Result.Error) result).getMessage());
                }
            });
        });

        findViewById(R.id.login).setOnClickListener(view -> {
            userRepository.signIn("m.ferioli@campus.unimib.it", "pinselo", result -> {
                if(result.isSuccessful()){
                    Log.d(this.getClass().getSimpleName(), "LOGIN: successo");
                }
                else{
                    Log.d(this.getClass().getSimpleName(), "LOGIN: errore - " + ((Result.Error) result).getMessage());
                }
            });
        });

        findViewById(R.id.logout).setOnClickListener(view -> {
            userRepository.signOut(result -> {
                if(result.isSuccessful()){
                    Log.d(this.getClass().getSimpleName(), "LOGOUT: successo");
                }
                else{
                    Log.d(this.getClass().getSimpleName(), "LOGOUT: errore - " + ((Result.Error) result).getMessage());
                }
            });
        });

        findViewById(R.id.sessione).setOnClickListener(view -> {
            userRepository.isSessionStillActive(result -> {
                if(result.isSuccessful()){
                    Log.d(this.getClass().getSimpleName(), "SESSIONE: successo - " + ((Result.BooleanSuccess) result).getBoolean());
                }
                else{
                    Log.d(this.getClass().getSimpleName(), "SESSIONE: errore - " + ((Result.Error) result).getMessage());
                }
            });
        });

        findViewById(R.id.avvioMail).setOnClickListener(view -> {
            userRepository.startEmailPolling(result -> {
                if(result.isSuccessful()){
                    Log.d(this.getClass().getSimpleName(), "MAIL: VERIFICATA!");
                }
                else{
                    Log.d(this.getClass().getSimpleName(), "MAIL: errore - " + ((Result.Error) result).getMessage());
                }
            });
        });

        findViewById(R.id.stopMail).setOnClickListener(view -> {
            userRepository.stopEmailPolling();
        });

        findViewById(R.id.mail).setOnClickListener(view -> {
            userRepository.sendEmailVerification(result -> {
                if(result.isSuccessful()){
                    Log.d(this.getClass().getSimpleName(), "INVIO MAIL: INVIATA!");
                }
                else{
                    Log.d(this.getClass().getSimpleName(), "INVIO MAIL: errore - " + ((Result.Error) result).getMessage());
                }
            });
        });

        findViewById(R.id.resetpassword).setOnClickListener(view -> {
            userRepository.resetPassword("l.pinciroli3@campus.unimib.it", result -> {
                if(result.isSuccessful()){
                    Log.d(this.getClass().getSimpleName(), "RESET PASSWORD MAIL: INVIATA!");
                }
                else{
                    Log.d(this.getClass().getSimpleName(), "RESET PASSWORD MAIL: errore - " + ((Result.Error) result).getMessage());
                }
            });
        });

        findViewById(R.id.vaiConfermaMail).setOnClickListener(view -> {
            startActivity(new Intent(this, AuthActivity.class));
        });
    }


}