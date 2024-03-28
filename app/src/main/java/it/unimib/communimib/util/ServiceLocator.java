package it.unimib.communimib.util;

import it.unimib.communimib.datasource.user.AuthDataSource;
import it.unimib.communimib.datasource.user.UserRemoteDataSource;
import it.unimib.communimib.repository.IUserRepository;
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

    public IUserRepository getUserRepository(){
        return new UserRepository(new AuthDataSource(), new UserRemoteDataSource());
    }

}
