package com.example.aura;

public class LawItem {
    private String title;
    private String description;
    private String year;

    public LawItem(String title, String description, String year) {
        this.title = title;
        this.description = description;
        this.year = year;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getYear() { return year; }
}
