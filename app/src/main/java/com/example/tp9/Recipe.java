package com.example.tp9;

public class Recipe {
    private int id;
    private String title;
    private String steps;
    private String keywords;

    public Recipe(int id, String title, String steps, String keywords) {
        this.id = id;
        this.title = title;
        this.steps = steps;
        this.keywords = keywords;
    }

    public Recipe(String title, String steps, String keywords) {
        this.title = title;
        this.steps = steps;
        this.keywords = keywords;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSteps() {
        return steps;
    }

    public String getKeywords() {
        return keywords;
    }

    @Override
    public String toString() {
        return title + " (" + keywords + ")";
    }
}