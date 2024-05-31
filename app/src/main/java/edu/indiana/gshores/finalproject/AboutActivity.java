package edu.indiana.gshores.finalproject;

/*
 * AboutActivity.java
 *
 * Controls the about section, mainly adding the back button to the toolbar programmatically
 *
 * Created by Gabe Shores
 * Created on: 2/24/24
 * Last Modified by: Gabe Shores
 * Last Modified on: 2/29/24 ~ Edited header and comments
 * Assignment/Project: A290 Android Development
 * Part of: Final Project, refers to activity_about.xml
 */
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class AboutActivity extends AppCompatActivity{

    /**
     * Called when the activity is created. Adds the back button
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.tbr_About);

        setSupportActionBar(toolbar);

        // Enable the back button in the ActionBar
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            //Handle the back button
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
