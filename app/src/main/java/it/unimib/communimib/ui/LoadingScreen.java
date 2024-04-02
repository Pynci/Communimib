package it.unimib.communimib.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.communimib.R;
import it.unimib.communimib.ui.auth.AuthActivity;

public class LoadingScreen extends AppCompatActivity {

    private LoadingScreenViewModel loadingScreenViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        loadingScreenViewModel = new ViewModelProvider(this).get(LoadingScreenViewModel.class);

        setContentView(R.layout.activity_loading_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        splashScreen.setKeepOnScreenCondition(() -> !loadingScreenViewModel.areDataAvaible().getValue() );


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getSplashScreen().setOnExitAnimationListener(splashScreenView -> {
                // Ottieni l'icona dalla SplashScreen
                View iconView = splashScreenView.getIconView();

                // Crea un'animazione per rimpicciolire gradualmente l'icona lungo l'asse X
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(
                        iconView, // L'oggetto da animare
                        View.SCALE_X, // La proprietà da animare
                        1.0f, // Valore iniziale
                        0.0f // Valore finale
                );
                scaleX.setDuration(500); // Imposta la durata dell'animazione in millisecondi
                scaleX.setInterpolator(new AccelerateInterpolator()); // Interpolatore per accelerare l'animazione

                // Crea un'animazione per rimpicciolire gradualmente l'icona lungo l'asse Y
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(
                        iconView, // L'oggetto da animare
                        View.SCALE_Y, // La proprietà da animare
                        1.0f, // Valore iniziale
                        0.0f // Valore finale
                );
                scaleY.setDuration(500); // Imposta la durata dell'animazione in millisecondi
                scaleY.setInterpolator(new AccelerateInterpolator()); // Interpolatore per accelerare l'animazione

                // Crea un set di animazioni che eseguono le animazioni scaleX e scaleY contemporaneamente
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(scaleX, scaleY); // Esegue entrambe le animazioni contemporaneamente
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        splashScreenView.remove(); // Rimuovi la SplashScreen quando l'animazione è completata
                    }
                });

                // Avvia l'insieme di animazioni
                animatorSet.start();
            });
        }

        try {
            loadingScreenViewModel.setDataAvaible();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}