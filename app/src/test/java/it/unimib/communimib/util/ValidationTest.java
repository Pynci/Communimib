package it.unimib.communimib.util;

import org.junit.Assert;
import org.junit.Test;

public class ValidationTest {

    @Test
    public void checkEmailSuccess() {
        String firstCheckResult = Validation.checkEmail("giulia@unimib.it");
        Assert.assertEquals("ok", firstCheckResult);

        String secondCheckResult = Validation.checkEmail("marco@campus.unimib.it");
        Assert.assertEquals("ok", secondCheckResult);
    }

    @Test
    public void checkEmailEmptyFailure() {
        String checkResult = Validation.checkEmail("");
        Assert.assertEquals(ErrorMapper.EMPTY_FIELD, checkResult);
    }

    @Test
    public void checkEmailInvalidFailure() {
        String checkResult = Validation.checkEmail("sbabaenasdo");
        Assert.assertEquals(ErrorMapper.INVALID_FIELD, checkResult);
    }

    @Test
    public void checkEmailNotUniversityFailure() {
        String checkResult = Validation.checkEmail("marco@gmail.com");
        Assert.assertEquals(ErrorMapper.NOT_UNIVERSITY_EMAIL, checkResult);
    }

    @Test
    public void checkPasswordShortFailure() {
        String checkResult = Validation.checkPassword("corta");
        Assert.assertEquals(ErrorMapper.TOO_SHORT_FIELD, checkResult);
    }

    @Test
    public void checkPasswordNumberFailure() {
        String checkResult = Validation.checkPassword("Password!");
        Assert.assertEquals(ErrorMapper.NUMBER_MISSING, checkResult);
    }

    @Test
    public void checkPasswordCapitalCharacterFailure() {
        String checkResult = Validation.checkPassword("password8!");
        Assert.assertEquals(ErrorMapper.CAPITAL_CASE_MISSING, checkResult);
    }

    @Test
    public void checkPasswordSpecialCharacterFailure() {
        String checkResult = Validation.checkPassword("Password8");
        Assert.assertEquals(ErrorMapper.SPECIAL_CHAR_MISSING, checkResult);
    }

    @Test
    public void checkConfirmPasswordSuccess() {
        String checkResult = Validation.checkConfirmPassword("password", "password");
        Assert.assertEquals("ok", checkResult);
    }

    @Test
    public void checkConfirmPasswordNotEqualFailure() {
        String checkResult = Validation.checkConfirmPassword("password", "juventus");
        Assert.assertEquals(ErrorMapper.NOT_EQUAL_PASSWORD, checkResult);
    }

    @Test
    public void checkConfirmPasswordEmptyFailure() {
        String checkResult = Validation.checkConfirmPassword("", "password");
        Assert.assertEquals(ErrorMapper.EMPTY_FIELD, checkResult);
    }

    @Test
    public void checkFieldSuccess() {
        String checkResult = Validation.checkField("Marco");
        Assert.assertEquals("ok", checkResult);
    }

    @Test
    public void checkFieldEmptyFailure() {
        String checkResult = Validation.checkField("");
        Assert.assertEquals(ErrorMapper.EMPTY_FIELD, checkResult);
    }

    @Test
    public void checkFieldNumberFailure() {
        String checkResult = Validation.checkField("Marco1");
        Assert.assertEquals(ErrorMapper.NUMBER_NOT_ALLOWED, checkResult);
    }

    @Test
    public void checkFieldSpecialCharFailure() {
        String checkResult = Validation.checkField("Marco$");
        Assert.assertEquals(ErrorMapper.SPECIAL_CHAR_NOT_ALLOWED, checkResult);
    }
}