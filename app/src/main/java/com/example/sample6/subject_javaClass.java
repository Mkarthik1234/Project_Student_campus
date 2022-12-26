package com.example.sample6;

public class subject_javaClass {
    String name,description;

    public subject_javaClass() {
    }

    public subject_javaClass(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setDescription(String description) {
        this.description = description;
    }
}
