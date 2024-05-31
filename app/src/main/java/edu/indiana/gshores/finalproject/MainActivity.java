package edu.indiana.gshores.finalproject;

/*
 *MainActivity.java
 *
 * Contains the Java code for the Main Activity/Home Screen
 * Directs the user to different parts of the app
 *
 * Created by Gabe Shores
 * Created on: 2/24/24
 * Last Modified by: Gabe Shores
 * Last Modified on: 2/29/24 ~ Fixed so high score properly updates on reentry
 * Assignment/Project: A290 Android Development
 * Part of: Final Project, refers to activity_main.xml
 */
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //The SharedPreferences object which saves to disk the theme and high score
    private SharedPreferences preferences;
    //The high score and its respective textview
    private int highScore;
    private TextView highScoreText;

    /**
     * Called when the activity is created and instantiates views and other values
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Adding onClickListeners to the buttons on the home screen
        View exitButton = findViewById(R.id.btn_Exit);
        exitButton.setOnClickListener(this::onClick);

        View aboutButton = findViewById(R.id.btn_About);
        aboutButton.setOnClickListener(this::onClick);

        View themesButton = findViewById(R.id.btn_Themes);
        themesButton.setOnClickListener(this::onClick);

        View playButton = findViewById(R.id.btn_Play);
        playButton.setOnClickListener(this::onClick);

        //Accesses the high score on the home screen
        highScoreText = findViewById(R.id.txt_HighScoreMain);

        //Gets the GamePreferences and updates the high score with the value, 0 if none exists
        preferences = getSharedPreferences("GamePreferences", Context.MODE_PRIVATE);
            highScore = preferences.getInt("highScore", 0);

        highScoreText.setText("High Score: " + highScore);

        //Makes the ThemeList
        new ThemeList(this);
    }

    /**
     * Handles when any of the buttons on the home screen is clicked
     * @param view
     */
    @Override
    public void onClick(View view) {
        final int id = view.getId();

        if(id == R.id.btn_Play) {
            //Play game and finish up this activity
            startActivity(new Intent(this, GameActivity.class));
            finish();
        }
        //Go to the About section
        else if(id == R.id.btn_About)
            startActivity(new Intent(this, AboutActivity.class));
        //Go to the Themes section
        else if(id == R.id.btn_Themes)
            startActivity(new Intent(this, ThemesActivity.class));
        //Exit the application
        else if (id == R.id.btn_Exit)
            finish();
    }

    /**
     * When this section is resumed, makes sure to update the high score
     */
    @Override
    protected void onResume() {
        super.onResume();
        //Default value of 0 if no value exists in the sharedPreferences file
        highScore = preferences.getInt("highScore", 0);
        highScoreText.setText("High Score: " + highScore);
    }
}