package it.unimib.communimib.repository;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.Callback;
import it.unimib.communimib.datasource.user.IAuthDataSource;
import it.unimib.communimib.datasource.user.IUserLocalDataSource;
import it.unimib.communimib.datasource.user.IUserRemoteDataSource;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;

public class UserRepositoryTest {

    private IAuthDataSource authDataSource;
    private IUserRemoteDataSource userRemoteDataSource;
    private IUserLocalDataSource userLocalDataSource;
    private IUserRepository userRepository;
    private User user;

    @Before
    public void setUp() throws Exception {
        authDataSource = mock(IAuthDataSource.class);
        userRemoteDataSource = mock(IUserRemoteDataSource.class);
        userLocalDataSource = mock(IUserLocalDataSource.class);
        userRepository = mock(UserRepository.class);
        user = new User("11111", "g.vitale16@campus.unimib.it", "Giulia", "Vitale", false);
    }

    @Test
    public void signUp() {
        doAnswer(invocation -> {
            Callback authCallback = invocation.getArgument(2);
            authCallback.onComplete(new Result.SignupSuccess("12345"));
            return null;
        }).when(authDataSource).signUp(any(), any(), any());

        doAnswer(invocation -> {
            Callback dbCallback = invocation.getArgument(5);
            dbCallback.onComplete(new Result.Success());
            return null;
        }).when(userRemoteDataSource).storeUserParameters(any(), any(), any(), any(), anyBoolean(), any());

        Callback callback = result -> {
            assertTrue(result instanceof Result.Success);
            User currentUser = userRepository.getCurrentUser();
            assertNotNull(currentUser);
            assertEquals("12345", currentUser.getUid());
        };

        userRepository.signUp("g.vitale16@campus.unimib.it", "password", "Giulia", "Vitale", callback);
    }

    @Test
    public void signIn() {
        doAnswer(invocation -> {
            Callback authCallback = invocation.getArgument(2);
            authCallback.onComplete(new Result.Success());
            return null;
        }).when(authDataSource).signIn(any(), any(), any());

        doAnswer(invocation -> {
            Callback userCallback = invocation.getArgument(1);
            userCallback.onComplete(new Result.UserSuccess(user));
            return null;
        }).when(userRemoteDataSource).getUserByEmail(any(), any());

        Callback callback = result -> {
            assertTrue(result instanceof Result.Success);
            User currentUser = userRepository.getCurrentUser();
            assertNotNull(currentUser);
            assertEquals("g.vitale16@campus.unimib.it", currentUser.getEmail());
        };

        userRepository.signIn("g.vitale16@campus.unimib.it", "password", callback);
    }

    @Test
    public void signOut() {
        doAnswer(invocation -> {
            Callback authCallback = invocation.getArgument(0);
            authCallback.onComplete(new Result.Success());
            return null;
        }).when(authDataSource).signOut(any());

        Callback callback = result -> {
            assertTrue(result instanceof Result.Success);
            User currentUser = userRepository.getCurrentUser();
            assertNull(currentUser);
        };

        userRepository.signOut(callback);
    }

    @Test
    public void updateUserNameAndSurname() {
        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(3);
            callback.onComplete(new Result.Success());
            return null;
        }).when(userRemoteDataSource).updateNameAndSurname(any(), any(), any(), any());

        Callback callback = result -> {
            assertTrue(result instanceof Result.Success);
            User currentUser = userRepository.getCurrentUser();
            assertEquals("NewName", currentUser.getName());
            assertEquals("NewSurname", currentUser.getSurname());
        };

        userRepository.updateUserNameAndSurname("NewName", "NewSurname", callback);
    }

    @Test
    public void uploadPropic() {
        Uri localUri = mock(Uri.class);

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(2);
            callback.onComplete(new Result.UriSuccess("https://new.image.uri"));
            return null;
        }).when(userRemoteDataSource).uploadPropic(any(), any(), any());

        Callback callback = result -> {
            assertTrue(result instanceof Result.Success);
            User currentUser = userRepository.getCurrentUser();
            assertEquals("https://new.image.uri", currentUser.getPropic());
        };

        userRepository.uploadPropic(localUri, callback);
    }

    @Test
    public void storeUserFavoriteBuildings() {
        List<String> favoriteBuildings = new ArrayList<>();
        favoriteBuildings.add("Building1");
        favoriteBuildings.add("Building2");

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(2);
            callback.onComplete(new Result.Success());
            return null;
        }).when(userRemoteDataSource).storeUserFavoriteBuildings(any(), any(), any());

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(1);
            callback.onComplete(new Result.Success());
            return null;
        }).when(userLocalDataSource).saveUserFavoriteBuildings(any(), any());

        Callback callback = result -> assertTrue(result instanceof Result.Success);

        userRepository.storeUserFavoriteBuildings(favoriteBuildings, callback);
    }

    @Test
    public void readUserFavoriteBuildings() {
        ArrayList<String> favoriteBuildings = new ArrayList<>();
        favoriteBuildings.add("Building1");
        favoriteBuildings.add("Building2");

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(1);
            callback.onComplete(new Result.UserFavoriteBuildingsSuccess(favoriteBuildings));
            return null;
        }).when(userRemoteDataSource).getUserFavoriteBuildings(any(), any());

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(1);
            callback.onComplete(new Result.UserFavoriteBuildingsSuccess(favoriteBuildings));
            return null;
        }).when(userLocalDataSource).getUserFavoriteBuildings(any());

        Callback callback = result -> {
            assertTrue(result instanceof Result.Success);
            assertEquals(favoriteBuildings, ((Result.UserFavoriteBuildingsSuccess) result).getFavoriteBuildings());
        };

        userRepository.readUserFavoriteBuildings(callback);
    }

/*
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
    }*/

}