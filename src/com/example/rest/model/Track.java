package com.example.rest.model;

public class Track {
    public long id;
    public String title;

    @Override
    public String toString() {
        return "Track{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
