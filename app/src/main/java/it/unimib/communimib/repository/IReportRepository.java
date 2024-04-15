package it.unimib.communimib.repository;

import it.unimib.communimib.Callback;

public interface IReportRepository {
    void createReport(String titolo, String descrizione, String edificio, String categoria, Callback callback);
}
