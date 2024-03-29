package it.unimib.communimib.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.communimib.R;

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
                final ObjectAnimator zoomX = ObjectAnimator.ofFloat(
                        splashScreenView.getIconView(),
                        View.SCALE_X,
                        0.2f,
                        0.0f
                );
                zoomX.setInterpolator(new OvershootInterpolator());
                zoomX.setDuration(1000);

                zoomX.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        splashScreenView.remove();
                    }
                });

                final ObjectAnimator zoomY = ObjectAnimator.ofFloat(
                        splashScreenView.getIconView(),
                        View.SCALE_Y,
                        0.2f,
                        0.0f
                );
                zoomY.setInterpolator(new OvershootInterpolator());
                zoomY.setDuration(1000);

                zoomY.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        splashScreenView.remove();
                    }
                });

                zoomX.start();
                zoomY.start();

            });
        }

        try {
            loadingScreenViewModel.setDataAvaible();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}