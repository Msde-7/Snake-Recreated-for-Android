package edu.indiana.gshores.finalproject;

/*
 * ThemesActivity.java
 *
 * Activity allowing the user to select a theme
 * from radio buttons and then saves it
 *
 * Created by Gabe Shores
 * Created on: 2/25/24
 * Last Modified by: Gabe Shores
 * Last Modified on: 2/29/24 ~ Names of themes linked to the Theme and ThemeList names
 * Assignment/Project: A290 Android Development
 * Part of: Final Project, refers to activity_themes.xml
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RadioGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class ThemesActivity extends AppCompatActivity {

    //The sharedPreferences containing the theme and high score
    private SharedPreferences preferences;
    //the Radio Group of themes
    private RadioGroup themeGroup;

    /**
     * Called when the activity and sets up views and toolbar
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);

        //Adds a back button to the toolbar
        Toolbar toolbar = findViewById(R.id.tbr_Themes);
        setSupportActionBar(toolbar);
        // Enable the back button on the ActionBar
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        themeGroup = findViewById(R.id.rdo_ThemeGroup);
        themeGroup.setOnCheckedChangeListener(this::onCheckedChange);

        preferences = getSharedPreferences("GamePreferences", MODE_PRIVATE);
    }

    /**
     * Updates the theme in the preferences when a radio button is selected
     * @param radioGroup
     * @param id
     */
    private void onCheckedChange(RadioGroup radioGroup, int id) {
        SharedPreferences.Editor editor = preferences.edit();
        if(id == R.id.rdo_Default)
            editor.putString("theme", "default");
        else if(id == R.id.rdo_BlackWhite)
            editor.putString("theme", "blackWhite");
        else if(id == R.id.rdo_Halloween)
            editor.putString("theme", "halloween");
        else if(id == R.id.rdo_GameBoy)
            editor.putString("theme", "gameBoy");
        else if(id == R.id.rdo_IU)
            editor.putString("theme", "IU");
        else if(id == R.id.rdo_BlackYellow)
            editor.putString("theme", "blackYellow");
        else
            Log.d("LifeCycle", "Invalid radio button selected");

        //Finalizes changes
        editor.commit();
    }

    /**
     * Exits activity when back button is pressed
     * @param  item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Returns to main activity
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Checks the correct radio button checked based on the theme in the preference file
     */
    @Override
    protected void onResume() {
        super.onResume();
        //Sets curTheme as default if not found in preferences
        String curTheme = preferences.getString("theme", "default");
        switch(curTheme) {
            case "default":
                themeGroup.check(R.id.rdo_Default);
                break;
            case "blackWhite":
                themeGroup.check(R.id.rdo_BlackWhite);
                break;
            case "gameBoy":
                themeGroup.check(R.id.rdo_GameBoy);
                break;
            case "halloween":
                themeGroup.check(R.id.rdo_Halloween);
                break;
            case "IU":
                themeGroup.check(R.id.rdo_IU);
                break;
            case "blackYellow":
                themeGroup.check(R.id.rdo_BlackYellow);
                break;
            default:
                Log.d("LifeCycle", "Invalid radio button selected");
        }
    }


}
