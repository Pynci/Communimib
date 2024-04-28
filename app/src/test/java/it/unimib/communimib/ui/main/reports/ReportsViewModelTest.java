package it.unimib.communimib.ui.main.reports;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.Callback;
import it.unimib.communimib.LiveDataTestUtil;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.repository.ReportRepository;
import it.unimib.communimib.repository.UserRepository;
import it.unimib.communimib.util.ErrorMapper;

public class ReportsViewModelTest {

    private ReportsViewModel reportsViewModel;
    private UserRepository userRepository;
    private ReportRepository reportRepository;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws Exception {
        userRepository = mock(UserRepository.class);
        reportRepository = mock(ReportRepository.class);
        reportsViewModel = new ReportsViewModel(reportRepository, userRepository);
    }

    @Test
    public void getCurrentUser() {
        User user = new User("testUser");
        when(userRepository.getCurrentUser()).thenReturn(user);
        User currentUser = reportsViewModel.getCurrentUser();
        assertEquals(user, currentUser);
    }

    @Test
    public void readAllReports() throws InterruptedException {

        doAnswer(invocation -> {
            Callback addedCallback = invocation.getArgument(0);
            addedCallback.onComplete(new Result.Success());
            Callback changedCallback = invocation.getArgument(1);
            changedCallback.onComplete(new Result.Success());
            Callback removedCallback = invocation.getArgument(2);
            removedCallback.onComplete(new Result.Success());
            Callback cancelledCallback = invocation.getArgument(3);
            cancelledCallback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_GET_ERROR));
            return null;
        }).when(reportRepository).readAllReports(any(), any(), any(), any());

        reportsViewModel.readAllReports();
        Result addedResult = LiveDataTestUtil.getOrAwaitValue(reportsViewModel.getReportAddedReadResult());
        assertTrue(addedResult instanceof Result.Success);
        Result changedResult = LiveDataTestUtil.getOrAwaitValue(reportsViewModel.getReportChangedReadResult());
        assertTrue(changedResult instanceof Result.Success);
        Result removedResult = LiveDataTestUtil.getOrAwaitValue(reportsViewModel.getReportRemovedReadResult());
        assertTrue(removedResult instanceof Result.Success);
        Result cancelledResult = LiveDataTestUtil.getOrAwaitValue(reportsViewModel.getReadCancelledResult());
        assertTrue(cancelledResult instanceof Result.Error);
        assertEquals(ErrorMapper.REMOTEDB_GET_ERROR, ((Result.Error) cancelledResult).getMessage());
    }

    @Test
    public void readReportsByBuildings() throws InterruptedException {
        List<String> buildings = new ArrayList<>();
        buildings.add("U1");
        buildings.add("U2");

        doAnswer(invocation -> {
            Callback addedCallback = invocation.getArgument(1);
            addedCallback.onComplete(new Result.Success());
            Callback changedCallback = invocation.getArgument(2);
            changedCallback.onComplete(new Result.Success());
            Callback removedCallback = invocation.getArgument(3);
            removedCallback.onComplete(new Result.Success());
            Callback cancelledCallback = invocation.getArgument(4);
            cancelledCallback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_GET_ERROR));
            return null;
        }).when(reportRepository).readReportsByBuildings(eq(buildings), any(), any(), any(), any());

        reportsViewModel.readReportsByBuildings(buildings);
        Result addedResult = LiveDataTestUtil.getOrAwaitValue(reportsViewModel.getReportAddedReadResult());
        assertTrue(addedResult instanceof Result.Success);
        Result changedResult = LiveDataTestUtil.getOrAwaitValue(reportsViewModel.getReportChangedReadResult());
        assertTrue(changedResult instanceof Result.Success);
        Result removedResult = LiveDataTestUtil.getOrAwaitValue(reportsViewModel.getReportRemovedReadResult());
        assertTrue(removedResult instanceof Result.Success);
        Result cancelledResult = LiveDataTestUtil.getOrAwaitValue(reportsViewModel.getReadCancelledResult());
        assertTrue(cancelledResult instanceof Result.Error);
        assertEquals(ErrorMapper.REMOTEDB_GET_ERROR, ((Result.Error) cancelledResult).getMessage());
    }

    @Test
    public void readReportsByTitleAndDescription() throws InterruptedException {
        String keyword = "keyword";

        doAnswer(invocation -> {
            Callback addedCallback = invocation.getArgument(1);
            addedCallback.onComplete(new Result.Success());
            Callback changedCallback = invocation.getArgument(2);
            changedCallback.onComplete(new Result.Success());
            Callback removedCallback = invocation.getArgument(3);
            removedCallback.onComplete(new Result.Success());
            Callback cancelledCallback = invocation.getArgument(4);
            cancelledCallback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_GET_ERROR));
            return null;
        }).when(reportRepository).readReportsByTitleAndDescription(eq(keyword), any(), any(), any(), any());

        reportsViewModel.readReportsByTitleAndDescription(keyword);
        Result addedResult = LiveDataTestUtil.getOrAwaitValue(reportsViewModel.getReportAddedReadResult());
        assertTrue(addedResult instanceof Result.Success);
        Result changedResult = LiveDataTestUtil.getOrAwaitValue(reportsViewModel.getReportChangedReadResult());
        assertTrue(changedResult instanceof Result.Success);
        Result removedResult = LiveDataTestUtil.getOrAwaitValue(reportsViewModel.getReportRemovedReadResult());
        assertTrue(removedResult instanceof Result.Success);
        Result cancelledResult = LiveDataTestUtil.getOrAwaitValue(reportsViewModel.getReadCancelledResult());
        assertTrue(cancelledResult instanceof Result.Error);
        assertEquals(ErrorMapper.REMOTEDB_GET_ERROR, ((Result.Error) cancelledResult).getMessage());
    }

    @Test
    public void deleteReport() throws InterruptedException {
        User marco = new User("12345", "m.ferioli@campus.unimib.it", "Marco", "Ferioli", false);
        Report report = new Report("titolo", "descrizione", "U14", "guasto", marco);
        report.setRid("54321");

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(1);
            callback.onComplete(new Result.Success());
            return null;
        }).when(reportRepository).deleteReport(eq(report), any());

        reportsViewModel.deleteReport(report);
        Result result = LiveDataTestUtil.getOrAwaitValue(reportsViewModel.getDeleteReportResult());
        assertTrue(result instanceof Result.Success);

    }
}