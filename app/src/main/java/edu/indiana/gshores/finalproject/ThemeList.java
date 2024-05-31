package edu.indiana.gshores.finalproject;

/*
 *ThemeList.java
 *
 * A static list (Array) of all the themes, allowing them to be accessed
 * in any class, mainly GameActivity.java
 *
 * Created by Gabe Shores
 * Created on: 2/29/24
 * Last Modified by: Gabe Shores
 * Last Modified on: 2/29/24 ~ Changed themes to an array
 * Assignment/Project: A290 Android Development
 * Part of: Final Project
 */

import androidx.core.content.ContextCompat;
import android.content.Context;
public class ThemeList {
    public static Theme[] themes;

    /**
     * Creates all the themes with the colors given from the colors.xml file
     * @param context
     */
    public ThemeList(Context context) {
        themes = new Theme[6];

        //Redundant parsing of blacks and whites maintained for readability and storage of the colors
        themes[0] = new Theme("blackWhite", ContextCompat.getColor(context, R.color.white), ContextCompat.getColor(context, R.color.black), ContextCompat.getColor(context, R.color.black));
        themes[1] = new Theme("default", ContextCompat.getColor(context, R.color.default_background), ContextCompat.getColor(context, R.color.default_snake), ContextCompat.getColor(context, R.color.default_apple));
        themes[2] = new Theme("gameBoy", ContextCompat.getColor(context, R.color.gameBoy_background), ContextCompat.getColor(context, R.color.gameBoy_snake), ContextCompat.getColor(context, R.color.gameBoy_apple));
        themes[3] = new Theme("halloween", ContextCompat.getColor(context, R.color.halloween_background), ContextCompat.getColor(context, R.color.halloween_snake),ContextCompat.getColor(context, R.color.halloween_apple));
        themes[4] = new Theme("IU", ContextCompat.getColor(context, R.color.IU_background), ContextCompat.getColor(context, R.color.IU_snake), ContextCompat.getColor(context, R.color.IU_apple));
        themes[5] = new Theme("blackYellow", ContextCompat.getColor(context,R.color.blackYellow_background), ContextCompat.getColor(context, R.color.blackYellow_snake), ContextCompat.getColor(context, R.color.blackYellow_apple));
    }

    /**
     * Returns the string from the given name
     * @param name
     * @return
     */
    public static Theme getTheme(String name) {
        for(Theme theme : themes)
            if(theme.getName().equals(name))
                return theme;

        return null;
    }
}
