package it.unimib.communimib;

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

import it.unimib.communimib.ui.auth.loading.AuthActivity;
import it.unimib.communimib.util.ErrorMapper;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignInUITest {

    @Rule
    public ActivityScenarioRule<AuthActivity> activityScenarioRule = new ActivityScenarioRule<>(AuthActivity.class);

    @Test
    public void testEmailValidationNotAnEmail() {

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignin_EditText_emailAddress))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignin_EditText_emailAddress))
                .perform(typeText("emailnonvalida"), closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignin_EditText_password))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignin_textView_emailError))
                .check(matches(isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignin_textView_emailError))
                .check(matches(withText(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.INVALID_FIELD))));
    }

    @Test
    public void testEmailValidationEmpty() {

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignin_EditText_emailAddress))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignin_EditText_emailAddress))
                .perform(typeText(""), closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignin_EditText_password))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignin_textView_emailError))
                .check(matches(isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignin_textView_emailError))
                .check(matches(withText(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.EMPTY_FIELD))));
    }

    public void testEmailValidationEmailNotUniversity() {

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignin_EditText_emailAddress))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignin_EditText_emailAddress))
                .perform(typeText("g@gmail.com"), closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignin_EditText_password))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignin_textView_emailError))
                .check(matches(isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignin_textView_emailError))
                .check(matches(withText(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.NOT_UNIVERSITY_EMAIL))));
    }

    @Test
    public void testPasswordEmpty() {

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignin_EditText_password))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignin_EditText_password))
                .perform(typeText(""), closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignin_EditText_emailAddress))
                .perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignin_textView_passwordError))
                .check(matches(isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignin_textView_passwordError))
                .check(matches(withText(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.EMPTY_FIELD))));
    }

    @Test
    public void testInvalidDataSnackbar() {

        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignin_button_signin))
                .perform(click());

        Espresso.onView(withText(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.NOT_ACCEPTED_PARAMETERS)))
                .check(ViewAssertions.matches(isDisplayed()));
    }
}
