package it.unimib.communimib.repository;

import it.unimib.communimib.Callback;
import it.unimib.communimib.datasource.report.IReportRemoteDataSource;
import it.unimib.communimib.model.Report;

public class ReportRepository implements IReportRepository {

    private final IReportRemoteDataSource reportRemoteDataSource;

    public ReportRepository (IReportRemoteDataSource reportRemoteDataSource) {
        this.reportRemoteDataSource = reportRemoteDataSource;
    }
    @Override
    public void createReport(String titolo, String descrizione, String edificio, String categoria, String emailCreator, Callback callback) {
        reportRemoteDataSource.createReport(new Report(titolo, descrizione, edificio, categoria, emailCreator), callback);
    }

    public void deleteReport(Report report, Callback callback){
        reportRemoteDataSource.deleteReport(report, callback);
    }

}
