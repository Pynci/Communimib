package it.unimib.communimib.datasource.report;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Report;

public interface IReportRemoteDataSource {
    void addReport(Report report, Callback callback);
}
