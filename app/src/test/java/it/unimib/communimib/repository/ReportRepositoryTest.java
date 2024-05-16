package it.unimib.communimib.repository;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import it.unimib.communimib.Callback;
import it.unimib.communimib.R;
import it.unimib.communimib.datasource.report.FakeReportRemoteDataSource;
import it.unimib.communimib.datasource.report.IReportRemoteDataSource;
import it.unimib.communimib.datasource.report.ReportRemoteDataSource;
import it.unimib.communimib.datasource.user.FakeUserRemoteDataSource;
import it.unimib.communimib.datasource.user.IUserRemoteDataSource;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.util.ErrorMapper;

public class ReportRepositoryTest {

    private volatile Result result;
    private IReportRepository reportRepository;
    private IReportRemoteDataSource reportRemoteDataSource;
    private User marco;

    @Before
    public void setUp() throws Exception {
        reportRemoteDataSource = mock(ReportRemoteDataSource.class);
        reportRepository = new ReportRepository(reportRemoteDataSource);
        marco = new User("12345", "m.ferioli@campus.unimib.it", "Marco", "Ferioli", false);
    }

    @Test
    public void readAllReports() {
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
        }).when(reportRemoteDataSource).readAllReports(any(), any(), any(), any());

        reportRepository.readAllReports(
                result ->
                        assertTrue(result instanceof Result.Success)
                ,
                result ->
                        assertTrue(result instanceof Result.Success)
                ,
                result ->
                        assertTrue(result instanceof Result.Success)
                ,
                result -> {
                    assertTrue(result instanceof Result.Error);
                    assertEquals(((Result.Error) result).getMessage(), ErrorMapper.REMOTEDB_GET_ERROR);
                });

        verify(reportRemoteDataSource).readAllReports(any(), any(), any(), any());
    }

    @Test
    public void readReportsByBuildings() {
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
        }).when(reportRemoteDataSource).readReportsByBuildings(eq(buildings), any(), any(), any(), any());

        reportRepository.readReportsByBuildings(
                buildings,
                result ->
                        assertTrue(result instanceof Result.Success)
                ,
                result ->
                        assertTrue(result instanceof Result.Success)
                ,
                result ->
                        assertTrue(result instanceof Result.Success)
                ,
                result -> {
                    assertTrue(result instanceof Result.Error);
                    assertEquals(((Result.Error) result).getMessage(), ErrorMapper.REMOTEDB_GET_ERROR);
                });

        verify(reportRemoteDataSource).readReportsByBuildings(eq(buildings), any(), any(), any(), any());
    }

    @Test
    public void readReportsByTitleAndDescription() {
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
        }).when(reportRemoteDataSource).readReportsByTitleAndDescription(eq(keyword), any(), any(), any(), any());

        reportRepository.readReportsByTitleAndDescription(
                keyword,
                result ->
                        assertTrue(result instanceof Result.Success)
                ,
                result ->
                        assertTrue(result instanceof Result.Success)
                ,
                result ->
                        assertTrue(result instanceof Result.Success)
                ,
                result -> {
                    assertTrue(result instanceof Result.Error);
                    assertEquals(((Result.Error) result).getMessage(), ErrorMapper.REMOTEDB_GET_ERROR);
                });

        verify(reportRemoteDataSource).readReportsByTitleAndDescription(eq(keyword), any(), any(), any(), any());
    }

    @Test
    public void createReportSuccess(){
        Report report = new Report("titolo", "descrizione", "U14", "guasto", marco);
        report.setRid("12345");
        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(1);
            callback.onComplete(new Result.Success());
            return null;
        }).when(reportRemoteDataSource).createReport(eq(report), any());

        reportRepository.createReport(
                "titolo",
                "descrizione",
                "U14",
                "guasto",
                marco,
                result ->
                        assertTrue(result instanceof Result.Success)
        );

    }

    @Test
    public void deleteReportSuccess() throws InterruptedException {
        Report report = new Report("titolo", "descrizione", "U14", "guasto", marco);
        report.setRid("54321");
        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(1);
            callback.onComplete(new Result.Success());
            return null;
        }).when(reportRemoteDataSource).deleteReport(eq(report), any());

        reportRepository.deleteReport(
                report,
                result ->
                        assertTrue(result instanceof Result.Success)
        );

        verify(reportRemoteDataSource).deleteReport(eq(report), any());
    }

}