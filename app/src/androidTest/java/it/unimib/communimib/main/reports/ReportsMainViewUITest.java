package it.unimib.communimib.main.reports;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import java.util.concurrent.CountDownLatch;

import it.unimib.communimib.R;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.ui.main.MainActivity;
import it.unimib.communimib.util.ServiceLocator;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ReportsMainViewUITest {
    private static boolean isLoggedIn = false;
    private ActivityScenario<MainActivity> scenario;
    @Before
    public void setUp() {

        if(!isLoggedIn) {
            CountDownLatch latch = new CountDownLatch(1);
            IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(ApplicationProvider.getApplicationContext());

            userRepository.signIn("m.ferioli@campus.unimib.it", "Prova123!", result -> {
                if (result.isSuccessful()) {
                    latch.countDown();
                    isLoggedIn = true;
                }
            });

            try {
                latch.await();
                scenario = ActivityScenario.launch(MainActivity.class);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testButtonVisibility() {
        // Controllo che il bottone menu sia visibile
        Espresso.onView(withId(R.id.floatingActionButton_menu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        //Clico sul bottone del menu
        Espresso.onView(withId(R.id.floatingActionButton_menu)).perform(click());

        // Controllo che il bottone preferiti sia visibile
        Espresso.onView(withId(R.id.floatingActionButton_favorite)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Controllo che il bottone degli filti-edifici sia visibile
        Espresso.onView(withId(R.id.floatingActionButton_favorite)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Controllo che il bottone di creazione sia visibile
        Espresso.onView(withId(R.id.floatingActionButton_favorite)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

}
