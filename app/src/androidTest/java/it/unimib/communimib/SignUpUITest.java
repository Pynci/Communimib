package it.unimib.communimib;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.unimib.communimib.ui.auth.loading.AuthActivity;

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
    public void testEmailValidationErrorMessage() {

        // Cliccare sul campo di testo per inserire l'email
        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_emailAddress))
                .perform(click());

        // Inserire un'email non valida
        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_emailAddress))
                .perform(typeText("emailnonvalida"), closeSoftKeyboard());

        // Togliere il focus dal campo di testo
        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_editText_password))
                .perform(click());

        // Verificare che la label di errore diventi visibile
        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_emailError))
                .check(matches(isDisplayed()));

        // Verificare che la label di errore mostri il testo corretto
        Espresso.onView(ViewMatchers.withId(R.id.fragmentSignup_textView_emailError))
                .check(matches(withText("Valore inserito non valido")));
    }
}