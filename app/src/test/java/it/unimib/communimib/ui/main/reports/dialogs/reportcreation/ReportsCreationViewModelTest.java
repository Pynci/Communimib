package it.unimib.communimib.ui.main.reports.dialogs.reportcreation;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.Test;

import it.unimib.communimib.Callback;
import it.unimib.communimib.LiveDataTestUtil;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.ReportRepository;
import it.unimib.communimib.repository.TokenRepository;
import it.unimib.communimib.repository.UserRepository;

public class ReportsCreationViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void createReport() throws InterruptedException {
        ReportRepository reportRepository = mock(ReportRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        TokenRepository tokenRepository = mock(TokenRepository.class);
        ReportsCreationViewModel reportsCreationViewModel = new ReportsCreationViewModel(reportRepository, userRepository, tokenRepository, mock(Context.class)); //non ho idea se mock context funzioni, è una prova
        String title = "titolo";
        String description = "descrizione";
        String building = "U14";
        String category = "guasto";

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(5);
            callback.onComplete(new Result.Success());
            return null;
        }).when(reportRepository).createReport(eq(title), eq(description), eq(building), eq(category), any(), any());
        reportsCreationViewModel.createReport(title, description, building, category, () -> {});
        Result result = LiveDataTestUtil.getOrAwaitValue(reportsCreationViewModel.getCreateReportResult());
        assertTrue(result instanceof Result.Success);
    }
}