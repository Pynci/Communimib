package it.unimib.communimib.ui.auth.signup;

import static org.junit.Assert.*;

import androidx.lifecycle.Observer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import it.unimib.communimib.database.FakeUserDAO;
import it.unimib.communimib.datasource.user.FakeAuthDataSource;
import it.unimib.communimib.datasource.user.FakeUserRemoteDataSource;
import it.unimib.communimib.datasource.user.UserLocalDataSource;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.UserRepository;
import it.unimib.communimib.util.ErrorMapper;

public class SignupViewModelTest {

    private SignupViewModel signupViewModel;
    private Result result;

    @Before
    public void setUp() throws Exception {
        UserRepository fakeUserRepository = new UserRepository(
             new FakeAuthDataSource(),
             new FakeUserRemoteDataSource(),
             new UserLocalDataSource(new FakeUserDAO())
        );
        signupViewModel = new SignupViewModel(fakeUserRepository);
    }

    /*
    @Test
    public void signUpSuccess() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        signupViewModel.cleanViewModel();
        signupViewModel.signUp(
                "marcolino@unimib.it",
                "Password8!",
                "Password8!",
                "Marco",
                "Ferioli"
        );
        Observer<Result> observer = result -> {
            this.result = result;
            countDownLatch.countDown();
        };
        signupViewModel.getSignUpResult().observeForever(observer);

        countDownLatch.await();
        Result signupResult = signupViewModel.getSignUpResult().getValue();
        Assert.assertTrue(signupResult instanceof Result.Success);

    }

     */

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