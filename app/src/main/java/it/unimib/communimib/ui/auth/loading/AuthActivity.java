package it.unimib.communimib.ui.auth.loading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

import it.unimib.communimib.R;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.ui.main.MainActivity;

public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "AuthActivity";
    private LoadingScreenViewModel loadingScreenViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Questo va messo prima del onCreate. È volutamente qui e non va spostato
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        //Ottieni riferimento a viewmodel
        loadingScreenViewModel = new ViewModelProvider(
                this,
                new LoadingScreenViewModelFactory(this.getApplicationContext()))
                .get(LoadingScreenViewModel.class);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Questo serve per dire quando deve avviare un'animazione di uscita
        splashScreen.setKeepOnScreenCondition(() -> Boolean.FALSE.equals(loadingScreenViewModel.getAreAllDataAvaible().getValue()));

        //Questo serve per l'animazione di uscita
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getSplashScreen().setOnExitAnimationListener(splashScreenView -> {
                View iconView = splashScreenView.getIconView();

                ObjectAnimator scaleX = ObjectAnimator.ofFloat(
                        iconView,
                        View.SCALE_X,
                        1.0f,
                        0.0f
                );
                scaleX.setDuration(500);
                scaleX.setInterpolator(new AccelerateInterpolator());

                ObjectAnimator scaleY = ObjectAnimator.ofFloat(
                        iconView,
                        View.SCALE_Y,
                        1.0f,
                        0.0f
                );
                scaleY.setDuration(500);
                scaleY.setInterpolator(new AccelerateInterpolator());

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(scaleX, scaleY);
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        boolean resultSession;
                        boolean resultEmail;
                        try {
                            resultSession = ((Result.BooleanSuccess) Objects.requireNonNull(loadingScreenViewModel.getSessionResult().getValue())).getBoolean();
                        }catch (NullPointerException exception) {
                            resultSession = false;
                        }

                        try{
                            resultEmail = ((Result.BooleanSuccess) Objects.requireNonNull(loadingScreenViewModel.getEmailCheckResult().getValue())).getBoolean();
                        }
                        catch (NullPointerException exception) {
                            resultEmail = false;
                        }

                        if(resultSession && resultEmail) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            splashScreenView.remove();
                        }
                    }
                });

                if(iconView != null)
                    animatorSet.start();
            });
        }

        //Lancio il controllo sulla sessione
        loadingScreenViewModel.checkSession();

        loadingScreenViewModel.getSessionResult().observe(this, sessionResult -> {
            if(sessionResult.isSuccessful()) {
                boolean result = ((Result.BooleanSuccess) sessionResult).getBoolean();
                if(result)
                    loadingScreenViewModel.checkEmailVerified();
                else{
                    notifyDataAreAvaible();
                }
            }
            else{
                String error = ((Result.Error) sessionResult).getMessage();
                Log.d(TAG, error);
            }
        });

        loadingScreenViewModel.getEmailCheckResult().observe(this, emailCheckResult -> {
            if (emailCheckResult.isSuccessful()) {
                boolean result = ((Result.BooleanSuccess) emailCheckResult).getBoolean();
                if(!result){
                    NavHostFragment navHostFragment = (NavHostFragment)
                            getSupportFragmentManager().findFragmentById(R.id.activityAuth_navHostFragment);

                    assert navHostFragment != null;
                    NavController navController = navHostFragment.getNavController();

                    navController.navigate(R.id.emailVerificationFragment);
                }
                notifyDataAreAvaible();
            }
            else{
                String error = ((Result.Error) emailCheckResult).getMessage();
                Log.d(TAG, error);
            }
        });
    }

    private void notifyDataAreAvaible() {
        try {
            loadingScreenViewModel.setAreAllDataAvaible();
        }
        catch (InterruptedException exception) {
            Log.d(TAG, Objects.requireNonNull(exception.getMessage()));
        }
    }
}