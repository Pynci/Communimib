package it.unimib.communimib.ui.auth.usermanagement;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import it.unimib.communimib.Callback;
import it.unimib.communimib.LiveDataTestUtil;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.UserRepository;
import it.unimib.communimib.ui.auth.signin.SigninViewModel;

public class EmailManagementViewModelTest {

    private EmailManagementViewModel emailManagementViewModel;
    private UserRepository mockUserRepository;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws Exception {
        mockUserRepository = mock(UserRepository.class);
        emailManagementViewModel = new EmailManagementViewModel(mockUserRepository);
    }

    @Test
    public void resetPassword() throws InterruptedException {
        emailManagementViewModel.cleanViewModel();
        String email = "luca@unimib.it";

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(1);
            callback.onComplete(new Result.Success());
            return null;
        }).when(mockUserRepository).resetPassword(eq(email), any());

        emailManagementViewModel.resetPassword(email);
        Result result = LiveDataTestUtil.getOrAwaitValue(emailManagementViewModel.getResetPasswordSendingResult());
        assertTrue(result instanceof Result.Success);
    }

    @Test
    public void sendEmailVerification() throws InterruptedException {
        emailManagementViewModel.cleanViewModel();

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(0);
            callback.onComplete(new Result.Success());
            return null;
        }).when(mockUserRepository).sendEmailVerification(any());

        emailManagementViewModel.sendEmailVerification();
        Result result = LiveDataTestUtil.getOrAwaitValue(emailManagementViewModel.getEmailVerificationSendingResult());
        assertTrue(result instanceof Result.Success);
    }

    @Test
    public void startEmailPolling() throws InterruptedException {
        emailManagementViewModel.cleanViewModel();

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(0);
            callback.onComplete(new Result.Success());
            return null;
        }).when(mockUserRepository).startEmailPolling(any());

        emailManagementViewModel.startEmailPolling();
        Result result = LiveDataTestUtil.getOrAwaitValue(emailManagementViewModel.getEmailVerificationResult());
        assertTrue(result instanceof Result.Success);
    }
}