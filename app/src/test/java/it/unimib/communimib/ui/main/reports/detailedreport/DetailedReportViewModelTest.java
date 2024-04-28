package it.unimib.communimib.ui.main.reports.detailedreport;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import it.unimib.communimib.Callback;
import it.unimib.communimib.LiveDataTestUtil;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.repository.ReportRepository;
import it.unimib.communimib.repository.UserRepository;
import it.unimib.communimib.ui.main.reports.ReportsViewModel;

public class DetailedReportViewModelTest {

    private DetailedReportViewModel detailedReportViewModel;
    private UserRepository userRepository;
    private ReportRepository reportRepository;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws Exception {
        userRepository = mock(UserRepository.class);
        reportRepository = mock(ReportRepository.class);
        detailedReportViewModel = new DetailedReportViewModel(userRepository, reportRepository);
    }

    @Test
    public void closeReport() throws InterruptedException {
        User marco = new User("12345", "m.ferioli@campus.unimib.it", "Marco", "Ferioli", false);
        Report report = new Report("titolo", "descrizione", "U14", "guasto", marco);
        report.setRid("54321");

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(1);
            callback.onComplete(new Result.Success());
            return null;
        }).when(reportRepository).deleteReport(eq(report), any());

        detailedReportViewModel.closeReport(report);
        Result result = LiveDataTestUtil.getOrAwaitValue(detailedReportViewModel.getCloseReportResult());
        assertTrue(result instanceof Result.Success);
    }
}