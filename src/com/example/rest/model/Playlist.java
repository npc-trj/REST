package com.example.rest.model;

public class Playlist {
    public long id;
    public String title;

    @Override
    public String toString() {
        return "Playlist{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
