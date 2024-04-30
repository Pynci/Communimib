package it.unimib.communimib.repository;

import java.util.List;

import it.unimib.communimib.Callback;
import it.unimib.communimib.datasource.report.IReportRemoteDataSource;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.User;

public class ReportRepository implements IReportRepository {

    private final IReportRemoteDataSource reportRemoteDataSource;

    public ReportRepository (IReportRemoteDataSource reportRemoteDataSource) {
        this.reportRemoteDataSource = reportRemoteDataSource;
    }

    @Override
    public void readAllReports(Callback addedCallback,
                               Callback changedCallback,
                               Callback removedCallback,
                               Callback cancelledCallback) {
        reportRemoteDataSource.readAllReports(addedCallback, changedCallback, removedCallback, cancelledCallback);
    }

    @Override
    public void readReportsByBuildings(List<String> buildings,
                                       Callback addedCallback,
                                       Callback changedCallback,
                                       Callback removedCallback,
                                       Callback cancelledCallback){
        reportRemoteDataSource.readReportsByBuildings(buildings, addedCallback, changedCallback, removedCallback, cancelledCallback);
    }

    @Override
    public void readReportsByTitleAndDescription(String keyword,
                                                 Callback addedCallback,
                                                 Callback changedCallback,
                                                 Callback removedCallback,
                                                 Callback cancelledCallback) {
        reportRemoteDataSource.readReportsByTitleAndDescription(keyword, addedCallback, changedCallback, removedCallback, cancelledCallback);
    }

    @Override
    public void createReport(String titolo, String descrizione, String edificio, String categoria, User author, Callback callback) {
        reportRemoteDataSource.createReport(new Report(titolo, descrizione, edificio, categoria, author), callback);
    }

    public void deleteReport(Report report, Callback callback){
        reportRemoteDataSource.deleteReport(report, callback);
    }

}
