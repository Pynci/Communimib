package it.unimib.communimib.repository;

import java.util.List;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.User;

public interface IReportRepository {

    void readAllReports(Callback addedCallback,
                        Callback changedCallback,
                        Callback removedCallback,
                        Callback cancelledCallback);

    void readReportsByBuildings(List<String> buildings,
                                Callback addedCallback,
                                Callback changedCallback,
                                Callback removedCallback,
                                Callback cancelledCallback);

    void readReportsByTitleAndDescription(String keyword,
                                          Callback addedCallback,
                                          Callback changedCallback,
                                          Callback removedCallback,
                                          Callback cancelledCallback);

    void createReport(String titolo, String descrizione, String edificio, String categoria, User author, Callback callback);

    void deleteReport(Report report, Callback callback);
}
