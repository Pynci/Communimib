package it.unimib.communimib.repository;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.User;

public interface IReportRepository {
    void createReport(String titolo, String descrizione, String edificio, String categoria, User author, Callback callback);

    void deleteReport(Report report, Callback callback);
}
