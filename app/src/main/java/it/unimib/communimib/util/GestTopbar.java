package it.unimib.communimib.util;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import it.unimib.communimib.R;

public class GestTopbar {
    private GestTopbar(){}
    public static void gestisciTopbar(AppCompatActivity activity) {
        ActionBar actionBar = activity.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.arrow_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
