package it.unimib.communimib.datasource.report;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Report;

public interface IReportRemoteDataSource {
    void getAllReports(Callback addedCallback,
                       Callback changedCallback,
                       Callback removedCallback,
                       Callback movedCallback);

    void getReportsByBuilding(String[] buildings,
                              Callback addedCallback,
                              Callback changedCallback,
                              Callback removedCallback,
                              Callback cancelledCallback);

    void getReportsByCategory(String[] categories,
                              Callback addedCallback,
                              Callback changedCallback,
                              Callback removedCallback,
                              Callback cancelledCallback);

    void getReportsByUID(String author,
                         Callback addedCallback,
                         Callback changedCallback,
                         Callback removedCallback,
                         Callback cancelledCallback);

    void addReport(Report report, Callback callback);
}
