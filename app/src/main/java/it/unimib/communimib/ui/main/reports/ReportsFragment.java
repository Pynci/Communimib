package it.unimib.communimib.ui.main.reports;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SearchView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.R;
import it.unimib.communimib.databinding.FragmentReportsBinding;
import it.unimib.communimib.model.BuildingReport;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.ui.main.reports.dialogs.favorites.FavoriteBuildingViewModel;
import it.unimib.communimib.ui.main.reports.dialogs.favorites.FavoriteBuildingViewModelFactory;
import it.unimib.communimib.ui.main.reports.dialogs.favorites.FavoriteBuildingsFragmentDialog;
import it.unimib.communimib.ui.main.reports.dialogs.reportcreation.NewReportFragmentDialog;
import it.unimib.communimib.ui.main.reports.dialogs.reportcreation.ReportsCreationViewModel;
import it.unimib.communimib.ui.main.reports.dialogs.reportcreation.ReportsCreationViewModelFactory;
import it.unimib.communimib.util.ErrorMapper;
import it.unimib.communimib.ui.main.reports.dialogs.filters.FiltersFragmentDialog;
import it.unimib.communimib.ui.main.reports.dialogs.filters.FiltersViewModel;

public class ReportsFragment extends Fragment {

    private FragmentReportsBinding fragmentReportsBinding;
    private ReportsViewModel reportsViewModel;
    private FiltersViewModel filtersViewModel;
    private ReportsCreationViewModel reportsCreationViewModel;
    private FavoriteBuildingViewModel favoriteBuildingViewModel;
    private ReportMainRecyclerViewAdapter reportMainRecyclerViewAdapter;
    private List<String> favoriteBuildings;
    private boolean isFilteredByFavorites = false;
    private boolean menuVisibile;

    public ReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportsViewModel =
                new ViewModelProvider(this, new ReportsViewModelFactory(this.getContext()))
                .get(ReportsViewModel.class);

        reportsCreationViewModel =
                new ViewModelProvider(this, new ReportsCreationViewModelFactory(this.getContext()))
                        .get(ReportsCreationViewModel.class);

        filtersViewModel = new ViewModelProvider(this).get(FiltersViewModel.class);


        favoriteBuildingViewModel = new ViewModelProvider(this, new FavoriteBuildingViewModelFactory(this.getContext()))
                .get(FavoriteBuildingViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentReportsBinding = FragmentReportsBinding.inflate(inflater, container, false);
        return fragmentReportsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentReportsBinding.fragmentReportSearchView.setOnClickListener(v ->
                fragmentReportsBinding.fragmentReportSearchView.setIconified(false));

        fragmentReportsBinding.fragmentReportSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                reportMainRecyclerViewAdapter.clearHorizontalAdapters();
                reportsViewModel.readReportsByTitleAndDescription(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        fragmentReportsBinding.fragmentReportSearchView.setOnCloseListener(() -> {
            filter(filtersViewModel.getChosenFilter().getValue());
            return false;
        });


        //Gestione pulsanti del menu
        fragmentReportsBinding.floatingActionButtonMenu.setOnClickListener(v ->
            onMenuButtonClicked(getContext())
        );

        fragmentReportsBinding.floatingActionButtonFavorite.setOnClickListener(v -> {
            FavoriteBuildingsFragmentDialog favoriteBuildingsFragmentDialog = new FavoriteBuildingsFragmentDialog(favoriteBuildingViewModel);
            favoriteBuildingsFragmentDialog.show(getParentFragmentManager(), "New Favorite Dialog");
            onMenuButtonClicked(getContext());
        });

        fragmentReportsBinding.floatingActionButtonFilterBuildings.setOnClickListener(v -> {
            FiltersFragmentDialog filtersFragmentDialog = new FiltersFragmentDialog(filtersViewModel);
            filtersFragmentDialog.show(getParentFragmentManager(), "New Filter Fragment Dialog");
            onMenuButtonClicked(getContext());
        });

        fragmentReportsBinding.floatingActionButtonAddNewReport.setOnClickListener(v -> {
            NewReportFragmentDialog dialog = new NewReportFragmentDialog(reportsCreationViewModel);
            dialog.show(getParentFragmentManager(), "New Report Fragment Dialog");
            onMenuButtonClicked(getContext());
        });

        //Gestione osservazione creazione
        reportsCreationViewModel.getCreateReportResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()) {
                // TODO: togliere questo testo hardcodato
                Snackbar.make(view, "La segnalazione è stata creata con successo", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        reportsViewModel.getReportAddedReadResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Report report = ((Result.ReportSuccess) result).getReport();
                reportMainRecyclerViewAdapter.addItem(report.getBuilding(), report);
                fragmentReportsBinding.textViewAlert.setVisibility(View.GONE);

            }
            else{
                Snackbar
                        .make(view, ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()), BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        reportsViewModel.getReportChangedReadResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Report report = ((Result.ReportSuccess) result).getReport();
                reportMainRecyclerViewAdapter.editItem(report.getBuilding(),report);
            }
            else{
                Snackbar
                        .make(view, ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()), BaseTransientBottomBar.LENGTH_SHORT)
                        .show();
            }
        });

        reportsViewModel.getReportRemovedReadResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Report report = ((Result.ReportSuccess) result).getReport();
                reportMainRecyclerViewAdapter.removeItem(report.getBuilding(),report);
                if(reportMainRecyclerViewAdapter.isEmpty()){
                    setTextAlert();
                }
            }
            else{
                Snackbar
                        .make(view, ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()), BaseTransientBottomBar.LENGTH_SHORT)
                        .show();
            }
        });

        reportsViewModel.getReadCancelledResult().observe(getViewLifecycleOwner(), result ->
            Snackbar
                    .make(view, ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()), BaseTransientBottomBar.LENGTH_SHORT)
                    .show()
        );

        reportsViewModel.getCloseReportResult().observe(getViewLifecycleOwner(), result ->{
            if(result.isSuccessful()){
                Snackbar.make(view, R.string.report_closed_correctly, BaseTransientBottomBar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(view, ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()), BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });


        List<BuildingReport> buildingReportList = new ArrayList<>();
        String[] buildings = getResources().getStringArray(R.array.buildings);
        for (int i = 0; i<buildings.length - 1; i++) {
            ReportsHorizontalRecyclerViewAdapter reportsHorizontalRecyclerViewAdapter =
                    new ReportsHorizontalRecyclerViewAdapter(reportsViewModel.getCurrentUser().isUnimibEmployee(),
                            new OnReportClickListener() {
                                @Override
                                public void onItemClick(Report report) {
                                    ReportsFragmentDirections.ActionReportsFragmentToDetailedReportFragment action =
                                            ReportsFragmentDirections.actionReportsFragmentToDetailedReportFragment(report);
                                    Navigation.findNavController(view).navigate(action);
                                }

                                @Override
                                public void onCloseReportClick(Report report) {
                                    reportsViewModel.closeReport(report);
                                }

                                @Override
                                public void onProfileClick(User reportAuthor) {
                                    ReportsFragmentDirections.ActionReportsFragmentToOtherUserProfileFragment action =
                                            ReportsFragmentDirections.actionReportsFragmentToOtherUserProfileFragment(reportAuthor);
                                    Navigation.findNavController(view).navigate(action);
                                }
                            },
                            requireContext(),
                            R.layout.report_horizontal_item);
            buildingReportList.add(new BuildingReport(buildings[i],reportsHorizontalRecyclerViewAdapter));
        }

        RecyclerView mainRecyclerView = fragmentReportsBinding.fragmentReportRecyclerView;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        reportMainRecyclerViewAdapter = new ReportMainRecyclerViewAdapter(buildingReportList);
        mainRecyclerView.setAdapter(reportMainRecyclerViewAdapter);
        mainRecyclerView.setLayoutManager(layoutManager);

        favoriteBuildings = new ArrayList<>();
        favoriteBuildingViewModel.getUserFavoriteBuildings();
        favoriteBuildingViewModel.getGetUserFavoriteBuildingsResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()) {

                boolean updatedFavoriteBuildings = false;
                
                // alla prima chiamata riempie i preferiti
                if(favoriteBuildings.isEmpty()){
                    favoriteBuildings = ((Result.UserFavoriteBuildingsSuccess) result).getFavoriteBuildings();
                    reportsViewModel.readReportsByBuildings(favoriteBuildings);
                    isFilteredByFavorites = true;
                }
                //successivamente effettua la rilettura solo se i preferiti sono cambiati rispetto a prima
                if(!favoriteBuildings.equals(((Result.UserFavoriteBuildingsSuccess) result).getFavoriteBuildings())){
                    favoriteBuildings = ((Result.UserFavoriteBuildingsSuccess) result).getFavoriteBuildings();
                    updatedFavoriteBuildings = true;
                }

                // aggiorna recycler view solo se è stata selezionata la visualizzazione per preferiti
                if(isFilteredByFavorites && updatedFavoriteBuildings) {
                        reportMainRecyclerViewAdapter.clearHorizontalAdapters();
                        reportsViewModel.readReportsByBuildings(favoriteBuildings);
                }

            } else {
                Snackbar.make(requireView(), ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        //setto la text view di alert se non sono presenti report
        setTextAlert();


        favoriteBuildingViewModel.getSetUserFavoriteBuildingsResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                favoriteBuildingViewModel.getUserFavoriteBuildings();
            } else {
                Snackbar.make(requireView(), ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        //Gestione osservazione filtri
        filtersViewModel.getChosenFilter().observe(getViewLifecycleOwner(), this::filter);

    }

    private void filter(List<String> filter) {
        if(filter != null && !filter.isEmpty()){
            if(filter.get(0).equals("filter-by-favorite")) {
                isFilteredByFavorites = true;
                if(!favoriteBuildings.isEmpty()){
                    reportMainRecyclerViewAdapter.clearHorizontalAdapters();
                    reportsViewModel.readReportsByBuildings(favoriteBuildings);
                } else {
                    Snackbar.make(requireView(), R.string.no_favorites_building, BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            } else if (filter.get(0).equals("filter-by-all")) {
                isFilteredByFavorites = false;
                reportMainRecyclerViewAdapter.clearHorizontalAdapters();
                reportsViewModel.readAllReports();
            } else {
                isFilteredByFavorites = false;
                reportMainRecyclerViewAdapter.clearHorizontalAdapters();
                reportsViewModel.readReportsByBuildings(filter);
            }
        }
        else{
            reportMainRecyclerViewAdapter.clearHorizontalAdapters();
            reportsViewModel.readAllReports();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        reportsViewModel.cleanViewModel();
        reportsCreationViewModel.cleanViewModel();
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
    //metodo per settare la textView che compare quando la recycler view è vuota
    public void setTextAlert(){
        if(reportMainRecyclerViewAdapter.isEmpty()){
            fragmentReportsBinding.textViewAlert.setVisibility(View.VISIBLE);
            if(favoriteBuildings.isEmpty()){
                fragmentReportsBinding.textViewAlert.setText(R.string.no_favorite_buildings_chosen);
            } else {
                fragmentReportsBinding.textViewAlert.setText(R.string.no_favorite_buildings_reports);
            }
        } else {
            fragmentReportsBinding.textViewAlert.setVisibility(View.GONE);
        }
    }

}