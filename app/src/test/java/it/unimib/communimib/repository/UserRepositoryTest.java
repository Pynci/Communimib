package it.unimib.communimib.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;

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
    User marco;
    volatile Result result;

    @Before
    public void setUp() {
        userDAO = new FakeUserDAO();
        remoteDataSource = new FakeUserRemoteDataSource();
        localDataSource = new UserLocalDataSource(userDAO);
        authDataSource = new FakeAuthDataSource();
        marco = new User("123456", "marco@unimib.it", "Marco", "Ferioli", true);

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
    public void updateUserNameAndSurname() {
        //TODO: implementare il test
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