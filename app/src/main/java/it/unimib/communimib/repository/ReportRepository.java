package it.unimib.communimib.repository;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.Callback;
import it.unimib.communimib.datasource.report.IReportRemoteDataSource;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.User;

public class ReportRepository implements IReportRepository {

    private final IReportRemoteDataSource reportRemoteDataSource;

    private final List<Report> reportList;

    public ReportRepository (IReportRemoteDataSource reportRemoteDataSource) {
        this.reportRemoteDataSource = reportRemoteDataSource;
        reportList = new ArrayList<>();
    }

    @Override
    public void readAllReports(Callback addedCallback,
                               Callback changedCallback,
                               Callback removedCallback,
                               Callback cancelledCallback) {
        reportRemoteDataSource.readAllReports(addedCallback, changedCallback, removedCallback, cancelledCallback);
    }

    @Override
    public void createReport(String titolo, String descrizione, String edificio, String categoria, User author, Callback callback) {
        reportRemoteDataSource.createReport(new Report(titolo, descrizione, edificio, categoria, author), callback);
    }

    public void deleteReport(Report report, Callback callback){
        reportRemoteDataSource.deleteReport(report, callback);
    }

    private Report findOldReport(Report newReport){
        for (Report report: reportList) {
            if(report.getRid().equals(newReport.getRid())){
                return report;
            }
        }
        return null;
    }

}
