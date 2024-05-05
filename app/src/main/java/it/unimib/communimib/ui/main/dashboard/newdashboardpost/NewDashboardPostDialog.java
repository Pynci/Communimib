package it.unimib.communimib.ui.main.dashboard.newdashboardpost;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import it.unimib.communimib.BottomNavigationBarListener;
import it.unimib.communimib.TopNavigationBarListener;
import it.unimib.communimib.databinding.FragmentNewDashboardPostDialogBinding;

public class NewDashboardPostDialog extends Fragment {

    private FragmentNewDashboardPostDialogBinding binding;

    private BottomNavigationBarListener bottomListener;
    private TopNavigationBarListener topListener;
    public NewDashboardPostDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNavigationBars();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewDashboardPostDialogBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        showNavigationBars();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BottomNavigationBarListener && context instanceof TopNavigationBarListener) {
            bottomListener = (BottomNavigationBarListener) context;
            topListener = (TopNavigationBarListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement BottomNavigationBarListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bottomListener = null;
        topListener = null;
    }

    private void hideNavigationBars() {
        if (bottomListener != null) {
            bottomListener.hideBottomNavigationBar();
        }

        if(topListener != null) {
            topListener.hideTopNavigationBar();
        }
    }

    private void showNavigationBars() {
        if (bottomListener != null) {
            bottomListener.showBottomNavigationBar();
        }

        if(topListener != null) {
            topListener.showTopNavigationBar();
        }
    }

}