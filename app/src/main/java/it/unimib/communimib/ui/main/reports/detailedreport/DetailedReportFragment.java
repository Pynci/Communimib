package it.unimib.communimib.ui.main.reports.detailedreport;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import it.unimib.communimib.databinding.FragmentDetailedReportBinding;
import it.unimib.communimib.BottomNavigationBarListener;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.util.ErrorMapper;
import it.unimib.communimib.util.BuildingsImagesHelper;
import it.unimib.communimib.util.TopbarHelper;

public class DetailedReportFragment extends Fragment {

    private FragmentDetailedReportBinding binding;
    private BottomNavigationBarListener mListener;

    private DetailedReportViewModel detailedReportViewModel;
    private Report report;

    public DetailedReportFragment() {
        //Empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomNavigationBar();
        TopbarHelper.handleTopbar((AppCompatActivity) getActivity());

        detailedReportViewModel = new ViewModelProvider(
                this,
                new DetailedReportViewModelFactory(requireContext())).get(DetailedReportViewModel.class);

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
        binding.textViewDescription.setText(report.getDescription());
        binding.textViewCategory.setText(report.getCategory());
        BuildingsImagesHelper.setBuildingImage(binding.reportListItemImageBuilding, report.getBuilding());
        binding.textViewBuilding.setText(report.getBuilding());
        binding.textViewAuthor.setText(report.getAuthor().getName() + " " + report.getAuthor().getSurname());
        Glide
                .with(requireContext())
                .load(Uri.parse(report.getAuthor().getPropic()))
                .into(binding.reportListItemImageProfile);

        User currentUser = detailedReportViewModel.getCurrentUser();
        if(currentUser != null && currentUser.isUnimibEmployee())
            binding.buttonCloseReport.setVisibility(View.VISIBLE);

        binding.buttonCloseReport.setOnClickListener(v -> detailedReportViewModel.closeReport(report));

        detailedReportViewModel.getCloseReportResult().observe(getViewLifecycleOwner(), result -> {

            if(result.isSuccessful()) {
                Navigation.findNavController(view).popBackStack();
                showBottomNavigationBar();
            }
            else {
                Result.Error error = (Result.Error) result;
                Snackbar.make(
                        view,
                        ErrorMapper.getInstance().getErrorMessage(error.getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });
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