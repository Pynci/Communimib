package it.unimib.communimib.util;

import android.content.Context;
import android.content.SharedPreferences;

import it.unimib.communimib.database.LocalDatabase;
import it.unimib.communimib.datasource.report.ReportRemoteDataSource;
import it.unimib.communimib.datasource.user.AuthDataSource;
import it.unimib.communimib.datasource.user.UserLocalDataSource;
import it.unimib.communimib.datasource.user.UserRemoteDataSource;
import it.unimib.communimib.repository.IReportRepository;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.repository.ReportRepository;
import it.unimib.communimib.repository.UserRepository;

public class ServiceLocator {

    private static volatile ServiceLocator INSTANCE = null;

    private ServiceLocator() {}

    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized(ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }

    public IUserRepository getUserRepository(Context context){
        return UserRepository.getInstance(
                new AuthDataSource(),
                new UserRemoteDataSource(),
                new UserLocalDataSource(getLocalDatabase(context).userDAO(), getFavoriteBuildingsSharedPreferences(context)));
    }

    public IReportRepository getReportRepository() {
        return new ReportRepository(new ReportRemoteDataSource());
    }

    public LocalDatabase getLocalDatabase(Context context) {
        return LocalDatabase.getDatabase(context);
    }

    public SharedPreferences getFavoriteBuildingsSharedPreferences(Context context) {
        return context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
    }
}
