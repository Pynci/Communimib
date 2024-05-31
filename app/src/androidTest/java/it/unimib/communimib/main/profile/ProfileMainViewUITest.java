package it.unimib.communimib.main.profile;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isFocusable;
import static androidx.test.espresso.matcher.ViewMatchers.isNotClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isNotFocusable;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import it.unimib.communimib.R;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.ui.main.MainActivity;
import it.unimib.communimib.util.ServiceLocator;

public class ProfileMainViewUITest {

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
                        }
                    });
            countDownLatch.await();

        }
        scenario = ActivityScenario.launch(MainActivity.class);

        onView(withId(R.id.activityMainButtonMenu_bottomNavigation)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(BottomNavigationView.class);
            }

            @Override
            public String getDescription() {
                return "Click on menu item to navigate to profile";
            }

            @Override
            public void perform(UiController uiController, View view) {
                BottomNavigationView bottomNavigationView = (BottomNavigationView) view;
                bottomNavigationView.setSelectedItemId(R.id.currentUserProfileFragment);
            }
        });

        onView(withId(R.id.currentUserProfileFragment)).check(matches(isDisplayed()));

    }

    @Test
    public void testEnableProfileEditing(){
        onView(withId(R.id.fragmentProfile_textView_name)).check(matches(isNotFocusable()));
        onView(withId(R.id.fragmentProfile_textView_surname)).check(matches(isNotFocusable()));
        onView(withId(R.id.fragmentProfile_imageView_profileImage)).check(matches(isNotClickable()));

        onView(withId(R.id.fragmentProfile_imageButton_editProfile)).perform(click());


        onView(withId(R.id.fragmentProfile_textView_name)).check(matches(isFocusable()));
        onView(withId(R.id.fragmentProfile_textView_surname)).check(matches(isFocusable()));
        onView(withId(R.id.fragmentProfile_cardView_propic)).check(matches(isClickable()));
    }

    @Test
    public void testLogOutButton(){

        onView(withId(R.id.fragmentProfile_imageButton_logout)).check(matches(isDisplayed()));

        onView(withId(R.id.fragmentProfile_imageButton_logout)).check(matches(isClickable()));
    }

    @Test
    public void testCurrentSelection(){

        onView(withId(R.id.profileDoubleItem_recyclerView)).check(matches(isDisplayed()));
    }
}
