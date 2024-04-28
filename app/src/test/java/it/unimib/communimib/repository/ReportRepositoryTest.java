package it.unimib.communimib.repository;

import static org.junit.Assert.*;

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
import it.unimib.communimib.datasource.user.FakeUserRemoteDataSource;
import it.unimib.communimib.datasource.user.IUserRemoteDataSource;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.util.ErrorMapper;

public class ReportRepositoryTest {

    volatile Result result;
    IReportRepository reportRepository;
    FakeReportRemoteDataSource reportRemoteDataSource;
    User marco;

    @Before
    public void setUp() throws Exception {
        reportRemoteDataSource = new FakeReportRemoteDataSource();
        reportRepository = new ReportRepository(reportRemoteDataSource);
        marco = new User("12345", "m.ferioli@campus.unimib.it", "Marco", "Ferioli", false);
    }

    @Test
    public void readAllReports() {
        Callback addedCallback = result -> {};
        Callback changedCallback = result -> {};
        Callback removedCallback = result -> {};
        Callback cancelledCallback = result -> {};
        reportRepository.readAllReports(
                addedCallback,
                changedCallback,
                removedCallback,
                cancelledCallback
        );

        assertEquals(addedCallback, reportRemoteDataSource.addedCallback);
        assertEquals(changedCallback, reportRemoteDataSource.changedCallback);
        assertEquals(removedCallback, reportRemoteDataSource.removedCallback);
        assertEquals(cancelledCallback, reportRemoteDataSource.cancelledCallback);
    }

    @Test
    public void readReportsByBuildings() {
        Callback addedCallback = result -> {};
        Callback changedCallback = result -> {};
        Callback removedCallback = result -> {};
        Callback cancelledCallback = result -> {};
        List<String> buildings = new ArrayList<>();
        buildings.add("U1");
        buildings.add("U2");
        reportRepository.readReportsByBuildings(
                buildings,
                addedCallback,
                changedCallback,
                removedCallback,
                cancelledCallback
        );

        assertEquals(buildings, reportRemoteDataSource.buildings);
        assertEquals(addedCallback, reportRemoteDataSource.addedCallback);
        assertEquals(changedCallback, reportRemoteDataSource.changedCallback);
        assertEquals(removedCallback, reportRemoteDataSource.removedCallback);
        assertEquals(cancelledCallback, reportRemoteDataSource.cancelledCallback);
    }

    @Test
    public void readReportsByTitleAndDescription() {
        Callback addedCallback = result -> {};
        Callback changedCallback = result -> {};
        Callback removedCallback = result -> {};
        Callback cancelledCallback = result -> {};
        String keyword = "keyword";
        reportRepository.readReportsByTitleAndDescription(
                keyword,
                addedCallback,
                changedCallback,
                removedCallback,
                cancelledCallback
        );

        assertEquals(keyword, reportRemoteDataSource.keyword);
        assertEquals(addedCallback, reportRemoteDataSource.addedCallback);
        assertEquals(changedCallback, reportRemoteDataSource.changedCallback);
        assertEquals(removedCallback, reportRemoteDataSource.removedCallback);
        assertEquals(cancelledCallback, reportRemoteDataSource.cancelledCallback);
    }

    @Test
    public void createReportSuccess() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        reportRepository.createReport("titolo", "descrizione", "U14", "guasto", marco, result -> {
            this.result = result;
            countDownLatch.countDown();
        });
        countDownLatch.await();
        assertTrue(result instanceof Result.Success);
    }

    @Test
    public void deleteReportSuccess() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Report report = new Report("titolo", "descrizione", "U14", "guasto", marco);
        report.setRid("54321");
        reportRemoteDataSource.reports.put(report.getRid(), report);

        reportRepository.deleteReport(report, result -> {
            this.result = result;
            countDownLatch.countDown();
        });
        countDownLatch.await();
        assertTrue(result instanceof Result.Success);
    }

    @Test
    public void deleteReportFailure() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Report report = new Report("titolo", "descrizione", "U14", "guasto", marco);
        report.setRid("54321");
        reportRemoteDataSource.reports.clear();

        reportRepository.deleteReport(report, result -> {
            this.result = result;
            countDownLatch.countDown();
        });
        countDownLatch.await();
        assertTrue(result instanceof Result.Error);
        assertEquals(ErrorMapper.REPORT_DELETING_ERROR, ((Result.Error) result).getMessage());
    }
}