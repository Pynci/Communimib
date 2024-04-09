package it.unimib.communimib.ui.auth.signup;

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

public class SignupViewModelTest {

    private SignupViewModel signupViewModel;
    private UserRepository mockUserRepository;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws Exception {
        mockUserRepository = mock(UserRepository.class);
        signupViewModel = new SignupViewModel(mockUserRepository);
    }


    @Test
    public void signUpSuccess() throws InterruptedException {
        signupViewModel.cleanViewModel();
        String email = "marcolino@unimib.it";
        String password = "Password8!";
        String name = "Marco";
        String surname = "Ferioli";

        // Risposta simulata per il metodo signUp del repository degli utenti
        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(4); // ottieni la callback dal parametro di signUp
            callback.onComplete(new Result.Success());
            return null;
        }).when(mockUserRepository).signUp(eq(email), eq(password), eq(name), eq(surname), any());

        signupViewModel.signUp(email, password, password, name, surname);
        Result result = LiveDataTestUtil.getOrAwaitValue(signupViewModel.getSignUpResult());
        Assert.assertTrue(result instanceof Result.Success);
    }

}