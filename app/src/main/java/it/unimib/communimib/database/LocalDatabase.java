package it.unimib.communimib.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.unimib.communimib.model.User;

@Database(entities = {User.class}, version = 1)
public abstract class LocalDatabase extends RoomDatabase {

    public static final String NOME_DATABASE = "communimib-db";

    //Dichiarazione dei DAO
    public abstract UserDAO userDAO();

    //Gestione dei thread
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //Il database è un singleton
    private static volatile LocalDatabase INSTANCE;

    public static LocalDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LocalDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = buildDatabase(context);
                }
            }
        }
        return INSTANCE;
    }


    private static LocalDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(
                context.getApplicationContext(),
                LocalDatabase.class, NOME_DATABASE)
                .build();
    }
}
