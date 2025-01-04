package com.example.aura.Courses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MentorCourse {
    private String title;
    private String description;
    private int lessonsCount;
    private String imageUrl;
    private List<String> modules;
    private HashMap<String, List<String>> lessonsByModule; // Map of modules to lessons

    // Constructor
    public MentorCourse(String title, String description, int lessonsCount, String imageUrl, List<String> modules) {
        this.title = title;
        this.description = description;
        this.lessonsCount = lessonsCount;
        this.imageUrl = imageUrl;
        this.modules = modules;
        this.lessonsByModule = new HashMap<>();
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getLessonsCount() {
        return lessonsCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<String> getModules() {
        return modules;
    }

    public HashMap<String, List<String>> getLessonsByModule() {
        return lessonsByModule;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLessonsCount(int lessonsCount) {
        this.lessonsCount = lessonsCount;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setModules(List<String> modules) {
        this.modules = modules;
    }

    public void setLessonsByModule(HashMap<String, List<String>> lessonsByModule) {
        this.lessonsByModule = lessonsByModule;
    }

    // Methods to manage lessons
    public void addLesson(String moduleName, String lesson) {
        if (!lessonsByModule.containsKey(moduleName)) {
            throw new IllegalArgumentException("Module " + moduleName + " does not exist.");
        }
        lessonsByModule.get(moduleName).add(lesson);
        lessonsCount++; // Update total lessons count
    }

    public List<String> getLessons(String moduleName) {
        return lessonsByModule.getOrDefault(moduleName, null);
    }

    public void removeLesson(String moduleName, String lesson) {
        if (lessonsByModule.containsKey(moduleName)) {
            lessonsByModule.get(moduleName).remove(lesson);
            lessonsCount--; // Update total lessons count
        }
    }

    public void addModule(String moduleName) {
        if (!modules.contains(moduleName)) {
            modules.add(moduleName);
            lessonsByModule.put(moduleName, new ArrayList<>());
        }
    }

    public void removeModule(String moduleName) {
        if (modules.contains(moduleName)) {
            modules.remove(moduleName);
            lessonsByModule.remove(moduleName);
        }
    }
}
