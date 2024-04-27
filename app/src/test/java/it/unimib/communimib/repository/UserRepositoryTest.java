package it.unimib.communimib.repository;

import static com.google.common.base.Verify.verify;
import static org.hamcrest.CoreMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import android.content.SharedPreferences;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;

import it.unimib.communimib.Callback;
import it.unimib.communimib.database.FakeUserDAO;
import it.unimib.communimib.datasource.user.FakeAuthDataSource;
import it.unimib.communimib.datasource.user.FakeUserRemoteDataSource;
import it.unimib.communimib.datasource.user.UserLocalDataSource;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.util.ErrorMapper;

public class UserRepositoryTest {

    IUserRepository userRepository;
    FakeUserRemoteDataSource remoteDataSource;
    UserLocalDataSource localDataSource;
    FakeAuthDataSource authDataSource;
    FakeUserDAO userDAO;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    User marco;
    volatile Result result;

    @Before
    public void setUp() {
        sharedPreferences = mock(SharedPreferences.class);
        editor = mock(SharedPreferences.Editor.class);
        userDAO = new FakeUserDAO();
        remoteDataSource = new FakeUserRemoteDataSource();
        localDataSource = new UserLocalDataSource(userDAO, sharedPreferences);
        authDataSource = new FakeAuthDataSource();
        marco = new User("12345", "marco@unimib.it", "Marco", "Ferioli", true);

        userRepository = UserRepository.getInstance(
                authDataSource,
                remoteDataSource,
                localDataSource
        );
    }

    @Test
    public void signUpSuccess() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        clearAll();

        userRepository.signUp(marco.getEmail(), "password", marco.getName(), marco.getSurname(), result -> {
            this.result = result;
            countDownLatch.countDown();
        });
        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.Success);
        Assert.assertEquals(marco.getEmail(), userRepository.getCurrentUser().getEmail());
    }

    @Test
    public void signUpFailure() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        clearAll();

        authDataSource.signedupUsers.add(marco);

        userRepository.signUp(marco.getEmail(), "password", marco.getName(), marco.getSurname(), result -> {
            this.result = result;
            countDownLatch.countDown();
        });
        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.Error);
        Assert.assertEquals(ErrorMapper.SIGNUP_ERROR, ((Result.Error) result).getMessage());
    }

    @Test
    public void signInSuccess() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        clearAll();

        remoteDataSource.users.put("12345", marco);
        authDataSource.signedupUsers.add(marco);

        userRepository.signIn(marco.getEmail(), "password", result -> {
            this.result = result;
            countDownLatch.countDown();
        });
        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.Success);
        Assert.assertEquals(marco.getEmail(), userRepository.getCurrentUser().getEmail());
    }

    @Test
    public void signInFailureWrongPassword() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        clearAll();

        remoteDataSource.users.put("12345", marco);
        authDataSource.signedupUsers.add(marco);

        userRepository.signIn(marco.getEmail(), "wrongPassword", result -> {
            this.result = result;
            countDownLatch.countDown();
        });
        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.Error);
        Assert.assertEquals(ErrorMapper.SIGNIN_ERROR, ((Result.Error) result).getMessage());
    }

    @Test
    public void signInFailureWrongEmail() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        clearAll();

        remoteDataSource.users.put("12345", marco);
        authDataSource.signedupUsers.add(marco);

        userRepository.signIn("luca@unimib.it", "password", result -> {
            this.result = result;
            countDownLatch.countDown();
        });
        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.Error);
        Assert.assertEquals(ErrorMapper.SIGNIN_ERROR, ((Result.Error) result).getMessage());
    }

    @Test
    public void signInFailureDataInconsistency() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        clearAll();

        // data are missing in remote data source (intentionally)
        authDataSource.signedupUsers.add(marco);

        userRepository.signIn(marco.getEmail(), "password", result -> {
            this.result = result;
            countDownLatch.countDown();
        });
        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.Error);
        Assert.assertEquals(ErrorMapper.USER_NOT_FOUND_ERROR, ((Result.Error) result).getMessage());
    }

    @Test
    public void signOutSuccess() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        clearAll();
        userDAO.insertUser(marco);
        remoteDataSource.users.put("12345", marco);
        authDataSource.signedupUsers.add(marco);
        authDataSource.currentUser = marco;

        userRepository.signOut(result -> {
            this.result = result;
            countDownLatch.countDown();
        });
        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.Success);
        Assert.assertNull(userRepository.getCurrentUser());
        Assert.assertNull(userDAO.getUser());
    }

    @Test
    public void signOutFailure() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        clearAll();
        remoteDataSource.users.put("12345", marco);
        authDataSource.signedupUsers.add(marco);

        userRepository.signOut(result -> {
            this.result = result;
            countDownLatch.countDown();
        });
        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.Error);
        Assert.assertEquals(ErrorMapper.USER_NOT_AUTHENTICATED_ERROR, ((Result.Error) result).getMessage());
    }

    @Test
    public void isSessionStillActiveTrue() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        clearAll();
        userDAO.insertUser(marco);
        remoteDataSource.users.put("12345", marco);
        authDataSource.signedupUsers.add(marco);
        authDataSource.currentUser = marco;

        userRepository.isSessionStillActive(result -> {
            this.result = result;
            countDownLatch.countDown();
        });
        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.BooleanSuccess);
        Assert.assertTrue(((Result.BooleanSuccess) result).getBoolean());
    }

    @Test
    public void isSessionStillActiveFalse() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        clearAll();
        userDAO.insertUser(marco);
        remoteDataSource.users.put("12345", marco);
        authDataSource.signedupUsers.add(marco);

        userRepository.isSessionStillActive(result -> {
            this.result = result;
            countDownLatch.countDown();
        });
        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.BooleanSuccess);
        Assert.assertFalse(((Result.BooleanSuccess) result).getBoolean());
    }

    @Test
    public void updateUserNameAndSurnameSuccess() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        clearAll();
        remoteDataSource.users.put("12345", marco);
        authDataSource.signedupUsers.add(marco);
        Field currentUser = UserRepository.class.getDeclaredField("currentUser");
        currentUser.setAccessible(true);
        currentUser.set(userRepository, marco);

        userRepository.updateUserNameAndSurname("Luca", "Pinciroli", result -> {
            this.result = result;
            countDownLatch.countDown();
        });

        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.Success);
        Assert.assertEquals("Luca", remoteDataSource.users.get("12345").getName());
        Assert.assertEquals("Pinciroli", remoteDataSource.users.get("12345").getSurname());
        Assert.assertEquals("Luca", userRepository.getCurrentUser().getName());
        Assert.assertEquals("Pinciroli", userRepository.getCurrentUser().getSurname());
        Assert.assertEquals("Luca", userDAO.getUser().getName());
        Assert.assertEquals("Pinciroli", userDAO.getUser().getSurname());
    }

    @Test
    public void uploadPropic() {
    }

    private void clearAll() throws NoSuchFieldException, IllegalAccessException {
        remoteDataSource.users.clear();
        userDAO.clearUser();
        authDataSource.signedupUsers.clear();
        Field instance = UserRepository.class.getDeclaredField("INSTANCE");
        instance.setAccessible(true);
        instance.set(null, null);
    }


}