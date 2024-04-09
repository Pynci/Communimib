package it.unimib.communimib.ui.auth.loading;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import it.unimib.communimib.Callback;
import it.unimib.communimib.LiveDataTestUtil;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.UserRepository;

public class LoadingScreenViewModelTest {

    private LoadingScreenViewModel mockLoadingScreenViewModel;

    private UserRepository mockUserRepository;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws Exception {
        mockUserRepository = mock(UserRepository.class);
        mockLoadingScreenViewModel = new LoadingScreenViewModel(mockUserRepository);
    }

    @Test
    public void checkSession() throws InterruptedException {
        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(0);
            callback.onComplete(new Result.Success());
            return null;
        }).when(mockUserRepository).isSessionStillActive(any());

        mockLoadingScreenViewModel.checkSession();
        Result result = LiveDataTestUtil.getOrAwaitValue(mockLoadingScreenViewModel.getSessionResult());
        Assert.assertTrue(result instanceof Result.Success);
    }

    @Test
    public void checkEmailVerified() throws InterruptedException {
        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(0);
            callback.onComplete(new Result.Success());
            return null;
        }).when(mockUserRepository).isEmailVerified(any());

        mockLoadingScreenViewModel.checkEmailVerified();
        Result result = LiveDataTestUtil.getOrAwaitValue(mockLoadingScreenViewModel.getEmailCheckResult());
        Assert.assertTrue(result instanceof Result.Success);
    }

    @Test
    public void setAreAllDataAvaible() throws InterruptedException {

        mockLoadingScreenViewModel.setAreAllDataAvaible();
        Boolean result = LiveDataTestUtil.getOrAwaitValue(mockLoadingScreenViewModel.getAreAllDataAvaible());
        Assert.assertTrue(result);
    }
}