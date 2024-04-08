package it.unimib.communimib.ui.auth.signin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

public class SigninViewModelTest {

    private SigninViewModel signinViewModel;
    private UserRepository mockUserRepository;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws Exception {
        mockUserRepository = mock(UserRepository.class);
        signinViewModel = new SigninViewModel(mockUserRepository);
    }

    @Test
    public void signIn() throws InterruptedException {
        signinViewModel.cleanViewModel();
        String email = "marcolino@unimib.it";
        String password = "Password8!";

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(2);
            callback.onComplete(new Result.Success());
            return null;
        }).when(mockUserRepository).signIn(eq(email), eq(password), any());

        signinViewModel.signIn(email, password);
        Result result = LiveDataTestUtil.getOrAwaitValue(signinViewModel.getSignInResult());
        Assert.assertTrue(result instanceof Result.Success);
    }

    @Test
    public void isEmailVerified() throws InterruptedException {
        signinViewModel.cleanViewModel();

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(0);
            callback.onComplete(new Result.Success());
            return null;
        }).when(mockUserRepository).isEmailVerified(any());

        signinViewModel.isEmailVerified();
        Result result = LiveDataTestUtil.getOrAwaitValue(signinViewModel.getEmailVerifiedResult());
        Assert.assertTrue(result instanceof Result.Success);
    }
}