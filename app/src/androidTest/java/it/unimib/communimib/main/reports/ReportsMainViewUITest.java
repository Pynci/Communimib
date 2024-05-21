package it.unimib.communimib.main.reports;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        scenario = ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void testButtonVisibility() {
        // Controllo che il bottone menu sia visibile
        onView(withId(R.id.floatingActionButton_menu)).check(matches(ViewMatchers.isDisplayed()));

        //Click sul bottone del menu
        onView(withId(R.id.floatingActionButton_menu)).perform(click());

        // Controllo che il bottone preferiti sia visibile
        onView(withId(R.id.floatingActionButton_favorite)).check(matches(ViewMatchers.isDisplayed()));

        // Controllo che il bottone degli filti-edifici sia visibile
        onView(withId(R.id.floatingActionButton_favorite)).check(matches(ViewMatchers.isDisplayed()));

        // Controllo che il bottone di creazione sia visibile
        onView(withId(R.id.floatingActionButton_favorite)).check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testFavoriteDialogAppear() {

        //Clico sul bottone del menu
        onView(withId(R.id.floatingActionButton_menu)).perform(click());

        //Clico sul bottone dei preferiti
        onView(withId(R.id.floatingActionButton_favorite)).perform(click());

        //Controllo che il dialog sia visibile
        onView(withText(R.string.favorite_explain_1))
                .check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testCreationDialogAppear() {

        //Clico sul bottone del menu
        onView(withId(R.id.floatingActionButton_menu)).perform(click());

        //Clico sul bottone di creazione
        onView(withId(R.id.floatingActionButton_add_new_report)).perform(click());

        //Controllo che il dialog sia visibile
        onView(withText(R.string.crea_una_nuova_segnalazione))
                .check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testBuildingFilterDialogAppear() {

        //Clico sul bottone del menu
        onView(withId(R.id.floatingActionButton_menu)).perform(click());

        //Clico sul bottone dei filtri
        onView(withId(R.id.floatingActionButton_filter_buildings)).perform(click());

        //Controllo che il dialog sia visibile
        onView(withText(R.string.filtra_per_gli_edfici_preferiti))
                .check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testErrorsCreateReportDialog() {

        //Clico sul bottone del menu
        onView(withId(R.id.floatingActionButton_menu)).perform(click());

        //Clico sul bottone di creazione
        onView(withId(R.id.floatingActionButton_add_new_report)).perform(click());

        //Clico sul bottone di creazione
        onView(withId(R.id.confirm_new_report)).perform(click());

        //Controllo che l'errore sul titolo sia visibile
        onView(withId(R.id.new_report_title_error))
                .check(matches(ViewMatchers.isDisplayed()));

        //Controllo che l'errore sulla descrizione sia visibile
        onView(withId(R.id.new_report_description_error))
                .check(matches(ViewMatchers.isDisplayed()));

        //Controllo che l'errore sugli edifici sia visibile
        onView(withId(R.id.new_report_error_spinner_buildings))
                .check(matches(ViewMatchers.isDisplayed()));

        //Controllo che l'errore sulle categorie sia visibile
        onView(withId(R.id.new_report_error_spinner_categories))
                .check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void checkSpinnerSelectionItem() {

        //Clico sul bottone del menu
        onView(withId(R.id.floatingActionButton_menu)).perform(click());

        //Clico sul bottone di creazione
        onView(withId(R.id.floatingActionButton_add_new_report)).perform(click());

        String buildingToSelect = "U1";

        // Seleziona l'elemento dallo Spinner degli edifici
        onView(withId(R.id.buildings_spinner))
                .perform(ViewActions.click()); // Apre lo Spinner

        onData(allOf(is(instanceOf(String.class)), is(buildingToSelect))).perform(click());

        onView(withId(R.id.buildings_spinner)).check(matches(withSpinnerText(containsString(buildingToSelect))));

        String categoryToSelect = "Guasto";

        // Seleziona l'elemento dallo Spinner degli edifici
        onView(withId(R.id.categories_spinner))
                .perform(ViewActions.click()); // Apre lo Spinner

        onData(allOf(is(instanceOf(String.class)), is(categoryToSelect))).perform(click());

        onView(withId(R.id.categories_spinner)).check(matches(withSpinnerText(containsString(categoryToSelect))));
    }
}
