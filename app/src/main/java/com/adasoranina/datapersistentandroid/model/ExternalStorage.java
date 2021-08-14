package com.adasoranina.datapersistentandroid.model;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.adasoranina.datapersistentandroid.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class ExternalStorage implements IEStorage {

    private final Context context;

    public ExternalStorage(Context context) {
        this.context = context;
    }

    @Override
    public void createFile(String fileName, String contentFile, MessageCallback callback) {
        try {
            if (getState()) {
                callback.error(context.getString(R.string.state_file_error_message));
            }

            File path = getPathFile();
            File file = new File(path, fileName);

            boolean fileCreated = file.createNewFile();

            if (fileCreated) {
                FileOutputStream outputStream = new FileOutputStream(file, true);
                outputStream.write(contentFile.getBytes());
                outputStream.flush();
                outputStream.close();
            }

            callback.success(String.format(
                    context.getString(R.string.create_success_message),
                    fileName));
        } catch (Exception e) {
            e.printStackTrace();
            callback.error(String.format(
                    context.getString(R.string.create_fail_message),
                    fileName));
        }
    }

    @Override
    public void updateFile(String fileName, String newContentFile, MessageCallback callback) {
        try {
            if (getState()) {
                callback.error(context.getString(R.string.state_file_error_message));
            }

            File path = getPathFile();
            File file = new File(path, fileName);

            boolean fileCreated = file.createNewFile();

            if (fileCreated) {
                createFile(fileName, newContentFile, callback);
                return;
            }

            FileOutputStream outputStream = new FileOutputStream(file, false);
            outputStream.write(newContentFile.getBytes());
            outputStream.flush();
            outputStream.close();

            callback.success(String.format(
                    context.getString(R.string.update_success_message),
                    fileName));
        } catch (Exception e) {
            e.printStackTrace();
            callback.error(String.format(
                    context.getString(R.string.update_fail_message),
                    fileName));
        }
    }

    @Override
    public void readFile(String fileName, MessageCallback callback) {
        if (getState()) {
            callback.error(context.getString(R.string.state_file_error_message));
        }

        File path = getPathFile();
        File file = new File(path, fileName);

        if (!file.exists()) {
            callback.error(context.getString(R.string.file_not_found));
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder text = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                text.append(line).append("\n");
            }

            callback.success(text.toString());
        } catch (IOException e) {
            e.printStackTrace();
            callback.error(String.format(
                    context.getString(R.string.read_fail_message),
                    fileName));
        }
    }

    @Override
    public void deleteFile(String fileName, MessageCallback callback) {
        if (getState()) {
            callback.error(context.getString(R.string.state_file_error_message));
        }

        File path = getPathFile();
        File file = new File(path, fileName);

        if (!file.exists()) {
            callback.error(context.getString(R.string.file_not_found));
            return;
        }

        boolean fileDeleted = file.delete();

        if (fileDeleted) {
            callback.success(String.format(
                    context.getString(R.string.del_success_message),
                    fileName));
        } else {
            callback.success(String.format(
                    context.getString(R.string.del_fail_message),
                    fileName));
        }

    }

    private File getPathFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        } else {
            return Environment.getExternalStorageDirectory();
        }
    }

    private boolean getState() {
        return !Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

}
