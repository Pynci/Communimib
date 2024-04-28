package it.unimib.communimib.datasource.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import it.unimib.communimib.Callback;
import it.unimib.communimib.database.FakeUserDAO;
import it.unimib.communimib.database.UserDAO;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;

public class UserLocalDataSourceTest {

    UserLocalDataSource userLocalDataSource;
    UserDAO fakeUserDAO;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Callback callback;
    User marco;
    volatile Result result;

    @Before
    public void setUp() throws Exception {
        sharedPreferences = Mockito.mock(SharedPreferences.class);
        editor = Mockito.mock(SharedPreferences.Editor.class);
        callback = Mockito.mock(Callback.class);
        fakeUserDAO = new FakeUserDAO();
        userLocalDataSource = new UserLocalDataSource(fakeUserDAO, sharedPreferences);
        marco = new User("123456", "marco@unimib.it", "Marco", "Ferioli", true);
    }

    @Test
    public void getUser() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        fakeUserDAO.insertUser(marco);
        userLocalDataSource.getUser(result -> {
            this.result = result;
            countDownLatch.countDown();
        });
        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.UserSuccess);
        Assert.assertEquals(marco, ((Result.UserSuccess) result).getUser());
    }

    @Test
    public void insertUser() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        fakeUserDAO.clearUser();
        userLocalDataSource.insertUser(marco,result -> {
            this.result = result;
            countDownLatch.countDown();
        });
        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.Success);
    }

    @Test
    public void updateUser() throws InterruptedException {
        //TODO: implementare il test
    }

    @Test
    public void deleteUser() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        fakeUserDAO.insertUser(marco);
        userLocalDataSource.deleteUser(result -> {
            this.result = result;
            countDownLatch.countDown();
        });
        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.Success);
    }

    @Test
    public void saveUserFavoriteBuildings() {
        List<String> favoriteBuildings = new ArrayList<>();
        favoriteBuildings.add("U1");
        favoriteBuildings.add("U2");
        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.putString(eq("string_array_list"), anyString())).thenReturn(editor);

        userLocalDataSource.saveUserFavoriteBuildings(favoriteBuildings, callback);

        verify(editor).putString(eq("string_array_list"), anyString());
        verify(editor).apply();
        verify(callback).onComplete(any(Result.Success.class));
    }

    @Test
    public void getUserFavoriteBuildings() {
        List<String> favoriteBuildings = new ArrayList<>();
        favoriteBuildings.add("U1");
        favoriteBuildings.add("U2");
        String json = new Gson().toJson(favoriteBuildings);
        when(sharedPreferences.getString(eq("string_array_list"), isNull())).thenReturn(json);

        userLocalDataSource.getUserFavoriteBuildings(callback);

        verify(callback).onComplete(
                argThat(result -> result instanceof Result.UserFavoriteBuildingsSuccess && ((Result.UserFavoriteBuildingsSuccess) result)
                                .getFavoriteBuildings()
                                .equals(favoriteBuildings)));
    }
}