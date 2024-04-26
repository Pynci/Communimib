package it.unimib.communimib.ui.main.reports;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.unimib.communimib.databinding.FragmentDetailedReportBinding;
import it.unimib.communimib.BottomNavigationBarListener;
import it.unimib.communimib.R;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.util.GestTopbar;

public class DetailedReportFragment extends Fragment {

    private FragmentDetailedReportBinding binding;
    private BottomNavigationBarListener mListener;
    private Report report;

    public DetailedReportFragment() {
        //Empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomNavigationBar();
        GestTopbar.gestisciTopbar((AppCompatActivity) getActivity());

        try {
            DetailedReportFragmentArgs args = DetailedReportFragmentArgs.fromBundle(getArguments());
            this.report = args.getReport();
        }
        catch (Exception e) {
            report = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailedReportBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.textViewTitolo.setText(report.getTitle());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        showBottomNavigationBar();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BottomNavigationBarListener) {
            mListener = (BottomNavigationBarListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement BottomNavigationBarListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void hideBottomNavigationBar() {
        if (mListener != null) {
            mListener.hideBottomNavigationBar();
        }
    }

    private void showBottomNavigationBar() {
        if (mListener != null) {
            mListener.showBottomNavigationBar();
        }
    }
}