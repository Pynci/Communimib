package it.unimib.communimib.repository;

import it.unimib.communimib.Callback;
import it.unimib.communimib.datasource.user.IUserRemoteDataSource;

public class ReportRepository implements IReportRepository {

    private final IUserRemoteDataSource userRemoteDataSource;

    public ReportRepository (IUserRemoteDataSource userRemoteDataSource) {
        this.userRemoteDataSource = userRemoteDataSource;
    }
    @Override
    public void createReport(String titolo, String descrizione, String edificio, String categoria, String emailCreator, Callback callback) {

    }
}
