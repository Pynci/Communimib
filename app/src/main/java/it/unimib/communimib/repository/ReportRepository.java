package it.unimib.communimib.repository;

import java.util.List;

import it.unimib.communimib.Callback;
import it.unimib.communimib.datasource.report.IReportRemoteDataSource;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;

public class ReportRepository implements IReportRepository {

    private final IReportRemoteDataSource reportRemoteDataSource;

    private List<Report> reportList;

    public ReportRepository (IReportRemoteDataSource reportRemoteDataSource) {
        this.reportRemoteDataSource = reportRemoteDataSource;
    }

    @Override
    public void readAllReports(Callback callback) {
        reportRemoteDataSource.readAllReports(result -> {
            Report report = ((Result.ReportSuccess) result).getReport();
            reportList.add(report);
            //callback.onComplete(reportList);
        },
        result -> {
            Report newReport = ((Result.ReportSuccess) result).getReport();
            Report oldReport = findOldReport(newReport);
            reportList.remove(oldReport);
            reportList.add(newReport);
            //callback.onComplete(reportList);
        },
        result -> {
            Report report = ((Result.ReportSuccess) result).getReport();
            reportList.remove(report);
            //callback.onComplete(reportList);
        },
        result -> {
            //callback.onComplete(reportList);
        });
    }

    @Override
    public void createReport(String titolo, String descrizione, String edificio, String categoria, User author, Callback callback) {
        reportRemoteDataSource.createReport(new Report(titolo, descrizione, edificio, categoria, author), callback);
    }

    public void deleteReport(Report report, Callback callback){
        reportRemoteDataSource.deleteReport(report, callback);
    }

    public Report findOldReport(Report newReport){
        for (Report report: reportList) {
            if(report.getRid().equals(newReport.getRid())){
                return report;
            }
        }
        return null;
    }

}
