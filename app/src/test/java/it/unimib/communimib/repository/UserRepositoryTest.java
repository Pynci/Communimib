package it.unimib.communimib.repository;

import static com.google.common.base.Verify.verify;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;

import android.net.Uri;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import it.unimib.communimib.datasource.user.FakeAuthDataSource;
import it.unimib.communimib.datasource.user.FakeUserLocalDataSource;
import it.unimib.communimib.datasource.user.FakeUserRemoteDataSource;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.util.ErrorMapper;

public class UserRepositoryTest {

    private IUserRepository userRepository;
    private FakeUserRemoteDataSource remoteDataSource;
    private FakeAuthDataSource authDataSource;
    private User marco;
    private volatile Result result;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        remoteDataSource = new FakeUserRemoteDataSource();
        FakeUserLocalDataSource localDataSource = new FakeUserLocalDataSource();
        authDataSource = new FakeAuthDataSource();
        marco = new User("12345", "marco@unimib.it", "Marco", "Ferioli", true);
        clearAll();
        userRepository = UserRepository.getInstance(
                authDataSource,
                remoteDataSource,
                localDataSource
        );
    }

    @Test
    public void signUpSuccess() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        CountDownLatch countDownLatch = new CountDownLatch(1);


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

        remoteDataSource.users.put(marco.getUid(), marco);
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

        remoteDataSource.users.put(marco.getUid(), marco);
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

        remoteDataSource.users.put(marco.getUid(), marco);
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
        //userDAO.insertUser(marco);
        remoteDataSource.users.put(marco.getUid(), marco);
        authDataSource.signedupUsers.add(marco);
        authDataSource.currentUser = marco;

        userRepository.signOut(result -> {
            this.result = result;
            countDownLatch.countDown();
        });
        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.Success);
        Assert.assertNull(userRepository.getCurrentUser());
        //Assert.assertNull(userDAO.getUser());
    }

    @Test
    public void signOutFailure() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        remoteDataSource.users.put(marco.getUid(), marco);
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
        //userDAO.insertUser(marco);
        remoteDataSource.users.put(marco.getUid(), marco);
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
        //userDAO.insertUser(marco);
        remoteDataSource.users.put(marco.getUid(), marco);
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
        remoteDataSource.users.put(marco.getUid(), marco);
        initializeCurrentUser(marco);

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
        //Assert.assertEquals("Luca", userDAO.getUser().getName());
        //Assert.assertEquals("Pinciroli", userDAO.getUser().getSurname());
    }

    @Test
    public void updateUserNameAndSurnameFailure() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        remoteDataSource.users.put(marco.getUid(), marco);
        // missing current user initialization (on purpose)

        userRepository.updateUserNameAndSurname("Luca", "Pinciroli", result -> {
            this.result = result;
            countDownLatch.countDown();
        });

        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.Error);
        Assert.assertEquals(ErrorMapper.USER_NOT_AUTHENTICATED_ERROR, ((Result.Error) result).getMessage());
    }

    @Test
    public void uploadPropicSuccess() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        remoteDataSource.users.put(marco.getUid(), marco);
        initializeCurrentUser(marco);
        Uri uri = Mockito.mock(Uri.class);

        userRepository.uploadPropic(uri, result -> {
           this.result = result;
           countDownLatch.countDown();
        });

        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.Success);
        Assert.assertEquals(uri.toString(), remoteDataSource.users.get("12345").getPropic());
        Assert.assertEquals(uri.toString(), userRepository.getCurrentUser().getPropic());
    }

    @Test
    public void uploadPropicFailure() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        remoteDataSource.users.put(marco.getUid(), marco);
        Uri uri = Mockito.mock(Uri.class);
        // missing current user initialization (on purpose)

        userRepository.uploadPropic(uri, result -> {
            this.result = result;
            countDownLatch.countDown();
        });

        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.Error);
        Assert.assertEquals(ErrorMapper.USER_NOT_AUTHENTICATED_ERROR, ((Result.Error) result).getMessage());
    }

    @Test
    public void storeUserFavoriteBuildingsSuccess() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        remoteDataSource.users.put(marco.getUid(), marco);
        initializeCurrentUser(marco);
        List<String> favoriteBuildings = new ArrayList<>();
        favoriteBuildings.add("U1");
        favoriteBuildings.add("U2");

        userRepository.storeUserFavoriteBuildings(favoriteBuildings, result -> {
            this.result = result;
            countDownLatch.countDown();
        });

        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.Success);
    }

    @Test
    public void readUserFavoriteBuildingsUpdateSuccess() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        List<String> favoriteBuildings = new ArrayList<>();
        favoriteBuildings.add("U1");
        favoriteBuildings.add("U2");
        remoteDataSource.users.put(marco.getUid(), marco);
        remoteDataSource.usersFavoriteBuildings.put(marco.getUid(), favoriteBuildings);
        initializeCurrentUser(marco);
        initializeLastFavoriteBuildingsUpdate(0);

        userRepository.readUserFavoriteBuildings(result -> {
            this.result = result;
            countDownLatch.countDown();
        });

        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.UserFavoriteBuildingsSuccess);
        Assert.assertEquals(favoriteBuildings, ((Result.UserFavoriteBuildingsSuccess) result).getFavoriteBuildings());
    }

    @Test
    public void readUserFavoriteBuildingsFetchSuccess() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        List<String> favoriteBuildings = new ArrayList<>();
        favoriteBuildings.add("U1");
        favoriteBuildings.add("U2");
        remoteDataSource.users.put(marco.getUid(), marco);
        remoteDataSource.usersFavoriteBuildings.put(marco.getUid(), favoriteBuildings);
        initializeCurrentUser(marco);
        initializeLastFavoriteBuildingsUpdate(System.currentTimeMillis());

        userRepository.readUserFavoriteBuildings(result -> {
            this.result = result;
            countDownLatch.countDown();
        });

        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.UserFavoriteBuildingsSuccess);
        Assert.assertEquals(favoriteBuildings, ((Result.UserFavoriteBuildingsSuccess) result).getFavoriteBuildings());
    }

    private void clearAll() throws NoSuchFieldException, IllegalAccessException {
        remoteDataSource.users.clear();
        //userDAO.clearUser();
        authDataSource.signedupUsers.clear();
        Field instance = UserRepository.class.getDeclaredField("INSTANCE");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    private void initializeCurrentUser(User currentUser) throws NoSuchFieldException, IllegalAccessException {
        Field currentUserField = UserRepository.class.getDeclaredField("currentUser");
        currentUserField.setAccessible(true);
        currentUserField.set(userRepository, currentUser);
    }

    private void initializeLastFavoriteBuildingsUpdate(long milliseconds) throws NoSuchFieldException, IllegalAccessException {
        Field lastFavoriteBuildingsUpdateField = UserRepository.class.getDeclaredField("lastFavoriteBuildingsUpdate");
        lastFavoriteBuildingsUpdateField.setAccessible(true);
        lastFavoriteBuildingsUpdateField.set(userRepository, milliseconds);
    }

}