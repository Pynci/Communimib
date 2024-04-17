package it.unimib.communimib.ui.main.reports;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import it.unimib.communimib.R;
import it.unimib.communimib.databinding.FragmentReportsBinding;

public class ReportsFragment extends Fragment {

    private FragmentReportsBinding fragmentReportsBinding;
    private ReportsViewModel reportsViewModel;

    private boolean menuVisibile;
    public ReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportsViewModel = new ViewModelProvider(
                this,
                new ReportsViewModelFactory(this.getContext()))
                .get(ReportsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentReportsBinding = FragmentReportsBinding.inflate(inflater, container, false);
        return fragmentReportsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Gestione menu
        fragmentReportsBinding.floatingActionButtonMenu.setOnClickListener(v -> {
            onMenuButtonClicked(getContext());
        });

        fragmentReportsBinding.floatingActionButtonFavorite.setOnClickListener(v -> {

        });

        fragmentReportsBinding.floatingActionButtonFilterBuildings.setOnClickListener(v -> {

        });

        fragmentReportsBinding.floatingActionButtonAddNewReport.setOnClickListener(v -> {
            NewReportFragmentDialog dialog = new NewReportFragmentDialog(reportsViewModel);
            dialog.show(getParentFragmentManager(), "New Report Fragment Dialog");
        });

        //Gestione osservazione creazione
        reportsViewModel.getCreateReportResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()) {
                Snackbar.make(view, "La segnalazione è stata creata con successo", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

    }

    private void onMenuButtonClicked(Context context) {
        setVisibility();
        setAnimation(context);
        menuVisibile = !menuVisibile;
    }

    private void setVisibility() {
        if(!menuVisibile) {
            fragmentReportsBinding.floatingActionButtonAddNewReport.setVisibility(View.VISIBLE);
            fragmentReportsBinding.floatingActionButtonFavorite.setVisibility(View.VISIBLE);
            fragmentReportsBinding.floatingActionButtonFilterBuildings.setVisibility(View.VISIBLE);
        }
        else{
            fragmentReportsBinding.floatingActionButtonAddNewReport.setVisibility(View.INVISIBLE);
            fragmentReportsBinding.floatingActionButtonFavorite.setVisibility(View.INVISIBLE);
            fragmentReportsBinding.floatingActionButtonFilterBuildings.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(Context context) {
        Animation animationFromBottom = AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim);
        Animation animationToBottom = AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim);
        Animation animationRotateOpen = AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim);
        Animation animationRotateClose = AnimationUtils.loadAnimation(context, R.anim.rotate_close_anim);

        if(!menuVisibile) {
            fragmentReportsBinding.floatingActionButtonAddNewReport.startAnimation(animationFromBottom);
            fragmentReportsBinding.floatingActionButtonFavorite.startAnimation(animationFromBottom);
            fragmentReportsBinding.floatingActionButtonFilterBuildings.startAnimation(animationFromBottom);
            fragmentReportsBinding.floatingActionButtonMenu.startAnimation(animationRotateOpen);
        }
        else{
            fragmentReportsBinding.floatingActionButtonAddNewReport.startAnimation(animationToBottom);
            fragmentReportsBinding.floatingActionButtonFavorite.startAnimation(animationToBottom);
            fragmentReportsBinding.floatingActionButtonFilterBuildings.startAnimation(animationToBottom);
            fragmentReportsBinding.floatingActionButtonMenu.startAnimation(animationRotateClose);
        }
    }
}