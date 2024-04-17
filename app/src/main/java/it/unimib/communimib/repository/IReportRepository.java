package it.unimib.communimib.repository;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Report;

public interface IReportRepository {
    void createReport(String titolo, String descrizione, String edificio, String categoria, String emailCreator, Callback callback);

    void deleteReport(Report report, Callback callback);
}
