package com.adasoranina.datapersistentandroid.model;

public class FileModel {
    // id di isi dengan res id dari button/command
    private int id;
    private String title;
    private String content;

    public FileModel(int id) {
        this.id = id;
    }

    public FileModel(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
