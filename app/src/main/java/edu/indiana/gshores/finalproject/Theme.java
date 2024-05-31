package edu.indiana.gshores.finalproject;

/*
 *Theme.java
 *
 * Code for the Theme object which stores its different colors and name
 * all in one place
 *
 * Created by Gabe Shores
 * Created on: 2/29/24
 * Last Modified by: Gabe Shores
 * Last Modified on: 2/29/24 ~ Added comments
 * Assignment/Project: A290 Android Development
 * Part of: Final Project
 */
public class Theme {
    private String name;
    private int backgroundColor;
    private int snakeColor;
    private int appleColor;


    /**
     * Constructor for Theme class, assigning colors.
     * intended for use by ThemeList.java
     * @param name
     * @param backgroundColor
     * @param snakeColor
     * @param appleColor
     */
    public Theme(String name, int backgroundColor, int snakeColor, int appleColor) {
        this.name = name;
        this.backgroundColor = backgroundColor;
        this.snakeColor = snakeColor;
        this.appleColor = appleColor;
    }

    /**
     * Getter for name
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for background color
     * @return int backgroundColor
     */
    public int getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Getter for snake color
     * @return int snakeColor
     */
    public int getSnakeColor() {
        return snakeColor;
    }

    /**
     * Getter for apple color
     * @return int appleColor
     */
    public int getAppleColor() {
        return appleColor;
    }
}
