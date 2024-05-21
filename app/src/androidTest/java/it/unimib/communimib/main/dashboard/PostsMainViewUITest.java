package it.unimib.communimib.main.dashboard;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.checkerframework.checker.units.qual.C;
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

            userRepository.signIn("g.vitale16@campus.unimib.it", "Aria123!",
                    result -> {
                        if (result.isSuccessful()){
                            countDownLatch.countDown();
                            isLogged = true;
                        }
                    });

            countDownLatch.await();

        }
        scenario = ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void testCreationButtonVisibility(){

        onView(withId(R.id.button_new_post)).check(matches(ViewMatchers.isDisplayed()));

        onView(withId(R.id.button_new_post)).perform(click());
    }


}
