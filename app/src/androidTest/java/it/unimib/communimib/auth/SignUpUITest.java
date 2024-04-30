package it.unimib.communimib.auth;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.unimib.communimib.R;
import it.unimib.communimib.ui.auth.loading.AuthActivity;
import it.unimib.communimib.util.ErrorMapper;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignUpUITest {

    @Rule
    public ActivityScenarioRule<AuthActivity> activityScenarioRule = new ActivityScenarioRule<>(AuthActivity.class);

    @Before
    public void setUp() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            NavHostFragment navHostFragment = (NavHostFragment)
                    activity.getSupportFragmentManager().findFragmentById(R.id.activityAuth_navHostFragment);

            NavController navController = navHostFragment.getNavController();

            navController.navigate(R.id.signupFragment);
        });
    }

    @Test
    public void testEmailValidationNotAnEmail() {

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_emailAddress))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_emailAddress))
                .perform(typeText("emailnonvalida"), closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_password))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_emailError))
                .check(matches(isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_emailError))
                .check(matches(withText(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.INVALID_FIELD))));
    }

    @Test
    public void testEmailValidationNullEmail() {

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_emailAddress))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_emailAddress))
                .perform(typeText(""), closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_password))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_emailError))
                .check(matches(isDisplayed()));

        // Verificare che la label di errore mostri il testo corretto
        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_emailError))
                .check(matches(withText(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.EMPTY_FIELD))));
    }

    @Test
    public void testEmailValidationNotUniversityEmail() {

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_emailAddress))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_emailAddress))
                .perform(typeText("m.ferioli@gmail.com"), closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_password))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_emailError))
                .check(matches(isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_emailError))
                .check(matches(withText(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.NOT_UNIVERSITY_EMAIL))));
    }

    @Test
    public void testPasswordLength() {

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_password))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_password))
                .perform(typeText("corto"), closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_emailAddress))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_passwordError))
                .check(matches(isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_passwordError))
                .check(matches(withText(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.TOO_SHORT_FIELD))));
    }

    @Test
    public void testPasswordNumberMissing() {

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_password))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_password))
                .perform(typeText("password"), closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_emailAddress))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_passwordError))
                .check(matches(isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_passwordError))
                .check(matches(withText(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.NUMBER_MISSING))));
    }

    @Test
    public void testPasswordCapitalCaseMissing() {

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_password))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_password))
                .perform(typeText("password1"), closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_emailAddress))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_passwordError))
                .check(matches(isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_passwordError))
                .check(matches(withText(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.CAPITAL_CASE_MISSING))));
    }

    @Test
    public void testPasswordSpecialChar() {

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_password))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_password))
                .perform(typeText("Password1"), closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_emailAddress))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_passwordError))
                .check(matches(isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_passwordError))
                .check(matches(withText(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.SPECIAL_CHAR_MISSING))));
    }

    @Test
    public void testEmptyConfirmPassword() {

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_confirmPassword))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_confirmPassword))
                .perform(typeText(""), closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_emailAddress))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_confirmPasswordError))
                .check(matches(isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_confirmPasswordError))
                .check(matches(withText(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.EMPTY_FIELD))));
    }

    @Test
    public void testNotEqualConfirmPassword() {

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_password))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_password))
                .perform(typeText("Password1!"), closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_confirmPassword))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_confirmPassword))
                .perform(typeText("Password2!"), closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_emailAddress))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_confirmPasswordError))
                .check(matches(isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_confirmPasswordError))
                .check(matches(withText(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.NOT_EQUAL_PASSWORD))));
    }

    @Test
    public void testEmptyName() {

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_name))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_name))
                .perform(typeText(""), closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_emailAddress))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_nameError))
                .check(matches(isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_nameError))
                .check(matches(withText(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.EMPTY_FIELD))));
    }

    @Test
    public void testNameWithNumber() {

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_name))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_name))
                .perform(typeText("Marco1"), closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_emailAddress))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_nameError))
                .check(matches(isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_nameError))
                .check(matches(withText(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.NUMBER_NOT_ALLOWED))));
    }

    @Test
    public void testNameWithSpecialChar() {

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_name))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_name))
                .perform(typeText("Marco!"), closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_emailAddress))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_nameError))
                .check(matches(isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_nameError))
                .check(matches(withText(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.SPECIAL_CHAR_NOT_ALLOWED))));
    }

    @Test
    public void testEmptySurname() {

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_surname))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_surname))
                .perform(typeText(""), closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_emailAddress))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_surnameError))
                .check(matches(isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_surnameError))
                .check(matches(withText(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.EMPTY_FIELD))));
    }

    @Test
    public void testSurnameWithNumber() {

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_surname))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_surname))
                .perform(typeText("Ferioli1"), closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_emailAddress))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_surnameError))
                .check(matches(isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_surnameError))
                .check(matches(withText(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.NUMBER_NOT_ALLOWED))));
    }

    @Test
    public void testSurnameWithSpecialChar() {

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_surname))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_surname))
                .perform(typeText("Ferioli!"), closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_emailAddress))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_surnameError))
                .check(matches(isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_surnameError))
                .check(matches(withText(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.SPECIAL_CHAR_NOT_ALLOWED))));
    }
}