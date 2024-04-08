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
import it.unimib.communimib.util.ErrorMapper;

public class SignupViewModelTest {

    private SignupViewModel signupViewModel;
    UserRepository mockUserRepository;

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


    @Test
    public void checkEmailSuccess() {
        String firstCheckResult = signupViewModel.checkEmail("giulia@unimib.it");
        Assert.assertEquals("ok", firstCheckResult);

        String secondCheckResult = signupViewModel.checkEmail("marco@campus.unimib.it");
        Assert.assertEquals("ok", secondCheckResult);
    }

    @Test
    public void checkEmailEmptyFailure() {
        String checkResult = signupViewModel.checkEmail("");
        Assert.assertEquals(ErrorMapper.EMPTY_FIELD, checkResult);
    }

    @Test
    public void checkEmailInvalidFailure() {
        String checkResult = signupViewModel.checkEmail("sbabaenasdo");
        Assert.assertEquals(ErrorMapper.INVALID_FIELD, checkResult);
    }

    @Test
    public void checkEmailNotUniversityFailure() {
        String checkResult = signupViewModel.checkEmail("marco@gmail.com");
        Assert.assertEquals(ErrorMapper.NOT_UNIVERSITY_EMAIL, checkResult);
    }

    @Test
    public void checkPasswordShortFailure() {
        String checkResult = signupViewModel.checkPassword("corta");
        Assert.assertEquals(ErrorMapper.TOO_SHORT_FIELD, checkResult);
    }

    @Test
    public void checkPasswordNumberFailure() {
        String checkResult = signupViewModel.checkPassword("Password!");
        Assert.assertEquals(ErrorMapper.NUMBER_MISSING, checkResult);
    }

    @Test
    public void checkPasswordCapitalCharacterFailure() {
        String checkResult = signupViewModel.checkPassword("password8!");
        Assert.assertEquals(ErrorMapper.CAPITAL_CASE_MISSING, checkResult);
    }

    @Test
    public void checkPasswordSpecialCharacterFailure() {
        String checkResult = signupViewModel.checkPassword("Password8");
        Assert.assertEquals(ErrorMapper.SPECIAL_CHAR_MISSING, checkResult);
    }

    @Test
    public void checkConfirmPasswordSuccess() {
        String checkResult = signupViewModel.checkConfirmPassword("password", "password");
        Assert.assertEquals("ok", checkResult);
    }

    @Test
    public void checkConfirmPasswordNotEqualFailure() {
        String checkResult = signupViewModel.checkConfirmPassword("password", "juventus");
        Assert.assertEquals(ErrorMapper.NOT_EQUAL_PASSWORD, checkResult);
    }

    @Test
    public void checkConfirmPasswordEmptyFailure() {
        String checkResult = signupViewModel.checkConfirmPassword("", "password");
        Assert.assertEquals(ErrorMapper.EMPTY_FIELD, checkResult);
    }

    @Test
    public void checkFieldSuccess() {
        String checkResult = signupViewModel.checkField("Marco");
        Assert.assertEquals("ok", checkResult);
    }

    @Test
    public void checkFieldEmptyFailure() {
        String checkResult = signupViewModel.checkField("");
        Assert.assertEquals(ErrorMapper.EMPTY_FIELD, checkResult);
    }

    @Test
    public void checkFieldNumberFailure() {
        String checkResult = signupViewModel.checkField("Marco1");
        Assert.assertEquals(ErrorMapper.NUMBER_NOT_ALLOWED, checkResult);
    }

    @Test
    public void checkFieldSpecialCharFailure() {
        String checkResult = signupViewModel.checkField("Marco$");
        Assert.assertEquals(ErrorMapper.SPECIAL_CHAR_NOT_ALLOWED, checkResult);
    }
}