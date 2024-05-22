package it.unimib.communimib.main.dashboard;

import static android.view.MotionEvent.BUTTON_PRIMARY;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import android.util.Log;
import android.view.View;

import androidx.navigation.Navigation;
import androidx.navigation.NavigatorProvider;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import it.unimib.communimib.R;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.ui.main.MainActivity;
import it.unimib.communimib.util.ServiceLocator;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PostsMainViewUITest {

    private static boolean isLogged = false;
    private ActivityScenario<MainActivity> scenario;

    @Before
    public void setUp() throws InterruptedException {

        if(!isLogged){
            CountDownLatch countDownLatch = new CountDownLatch(1);
            IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(ApplicationProvider.getApplicationContext());

            userRepository.signIn("g.vitale16@campus.unimib.it", "Applicazione123!",
                    result -> {
                        if (result.isSuccessful()){
                            countDownLatch.countDown();
                            isLogged = true;
                            Log.d("risultato", "fine");
                        } else {
                            Log.d("risultato", "errore");
                        }
                    });
            Log.d("risultato", "in attesa");
            countDownLatch.await();

        }
        scenario = ActivityScenario.launch(MainActivity.class);

    }

    @Test
    public void testCreationButtonVisibility(){
/*
        onView(withId(R.id.activityMainButtonMenu_bottomNavigation)).perform(click(R.id.dashboardFragment, BUTTON_PRIMARY));

        onView(withId(R.id.dashboardFragment)).check(matches(ViewMatchers.isDisplayed()));
*/
        onView(withId(R.id.button_new_post)).check(matches(ViewMatchers.isDisplayed()));

        onView(withId(R.id.button_new_post)).perform(click());
    }

    @Test
    public void testCreationPostDialogAppears(){

        onView(withId(R.id.button_new_post)).perform(click());

        onView(withText(R.string.crea_un_nuovo_post)).check(matches(ViewMatchers.isDisplayed()));
    }
    @Test
    public void checkCategorySpinnerSelection(){

        onView(withId(R.id.button_new_post)).perform(click());

        onView(withId(R.id.categories_spinner)).perform(ViewActions.click());

        onData(allOf(is(instanceOf(String.class)), is("Eventi"))).perform(click());
        onView(withId(R.id.categories_spinner)).check(matches(withSpinnerText(containsString("Eventi"))));
    }

     @Test
    public void testEnableButtonCreationPostDialog(){

         onView(withId(R.id.button_new_post)).perform(click());

         onView(withId(R.id.button_confirm)).check(matches(ViewMatchers.isNotEnabled()));

         onView(withId(R.id.editText_post_title)).perform(typeText("titolo"));

         onView(withId(R.id.editText_post_description)).perform(typeText("descrizione"));

         onView(withId(R.id.categories_spinner)).perform(click());

         onData(allOf(is(instanceOf(String.class)), is("Eventi"))).perform(click());
         onView(withId(R.id.categories_spinner)).check(matches(withSpinnerText(containsString("Eventi"))));

         onView(withId(R.id.button_confirm)).check(matches(ViewMatchers.isEnabled()));
     }

     @Test
    public void checkImageLoaderCreationPost(){

        onView(withId(R.id.button_new_post)).perform(click());

        onView(withId(R.id.imageButton_add_images)).perform(click());


     }

     // test recycler view orizzontale categorie

    // test image loader

    // test visibilità button per tornare su

    // test visibilità


}
