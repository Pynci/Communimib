package it.unimib.communimib.ui.main.reports.dialogs.filters;

import static org.junit.Assert.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.LiveDataTestUtil;

public class FiltersViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void setFilters() throws InterruptedException {
        FiltersViewModel filtersViewModel = new FiltersViewModel();
        List<String> filters = new ArrayList<>();
        filters.add("filter-by-all");

        filtersViewModel.setFilters(filters, () -> {});
        List<String> actualFilters = LiveDataTestUtil.getOrAwaitValue(filtersViewModel.getChosenFilter());
        assertEquals(filters, actualFilters);
    }
}