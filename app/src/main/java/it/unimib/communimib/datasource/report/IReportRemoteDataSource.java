package it.unimib.communimib.datasource.report;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Report;

public interface IReportRemoteDataSource {
    void readAllReports(Callback addedCallback,
                        Callback changedCallback,
                        Callback removedCallback,
                        Callback cancelledCallback);

    void readReportsByBuildings(String[] buildings,
                                Callback addedCallback,
                                Callback changedCallback,
                                Callback removedCallback,
                                Callback cancelledCallback);

    void readReportsByUID(String author,
                          Callback addedCallback,
                          Callback changedCallback,
                          Callback removedCallback,
                          Callback cancelledCallback);

    void createReport(Report report, Callback callback);

    void deleteReport(Report report, Callback callback);
}
