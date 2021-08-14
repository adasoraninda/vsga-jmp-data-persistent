package com.adasoranina.datapersistentandroid.model;

public interface IEStorage {

    void createFile(String fileName, String content, MessageCallback callback);

    void updateFile(String fileName, String content, MessageCallback callback);

    void readFile(String fileName, MessageCallback callback);

    void deleteFile(String fileName, MessageCallback callback);

}
