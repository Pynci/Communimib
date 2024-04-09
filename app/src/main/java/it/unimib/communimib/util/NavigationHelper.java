package it.unimib.communimib.util;

import android.app.Activity;
import android.view.View;

public class NavigationHelper {

    private NavigationHelper(){

    }

    public static void navigateTo(Activity currentActivity, View rootView, int destination, boolean finishActivity){
        androidx.navigation.Navigation.findNavController(rootView).navigate(destination);
        if(finishActivity)
            currentActivity.finish();
    }
}
