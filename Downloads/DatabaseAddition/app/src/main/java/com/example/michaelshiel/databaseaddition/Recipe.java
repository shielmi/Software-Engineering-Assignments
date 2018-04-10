package com.example.michaelshiel.databaseaddition;

import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.Map;

/**
 * Created by michaelshiel on 05/04/2018.
 */

public class Recipe {
    private String name;
    private String calories;
    private String prepTime;
    private String cookTime;
    private String protein;
    private String carbs;
    private String fat;
    private String measurement;
    private Map<String, Map<String,String>> ingredients;
    private Map<String,String> instructions;
    private String survingSuggestion;
    private String writer;
    private String url;

    public Recipe() {
    }

    public Recipe(String name, String calories, String prepTime, String cookTime, String protein, String carbs, String fat, String measurement,
                  Map<String, Map<String,String>> ingredients, Map<String,String> instructions, String survingSuggestion, String writer, String URL, DatabaseReference r) {

        this.name = name;
        this.calories = calories;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.measurement = measurement;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.survingSuggestion = survingSuggestion;
        this.writer = writer;
        this.url = URL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(String prepTime) {
        this.prepTime = prepTime;
    }

    public String getCookTime() {
        return cookTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public Map<String, Map<String, String>> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<String, Map<String, String>> ingredients) {
        this.ingredients = ingredients;
    }

    public Map<String, String> getInstructions() {
        return instructions;
    }

    public void setInstructions(Map<String, String> instructions) {
        this.instructions = instructions;
    }

    public String getSurvingSuggestion() {
        return survingSuggestion;
    }

    public void setSurvingSuggestion(String survingSuggestion) {
        this.survingSuggestion = survingSuggestion;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
