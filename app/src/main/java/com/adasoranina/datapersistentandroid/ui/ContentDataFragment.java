package com.adasoranina.datapersistentandroid.ui;

import static com.adasoranina.datapersistentandroid.ui.PersistDataFragment.MODE;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.adasoranina.datapersistentandroid.R;
import com.adasoranina.datapersistentandroid.model.FileModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class ContentDataFragment extends Fragment implements PersistDataFragment.CommandContentListener, View.OnClickListener {

    private EditText inputTitle;
    private EditText inputContent;
    private Button buttonManipulate;

    @Nullable
    private FileModel fileModel;

    public static ContentDataFragment getInstance(BaseFragment.Mode mode) {
        ContentDataFragment fragment = new ContentDataFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MODE, mode);
        fragment.setArguments(bundle);

        return fragment;
    }

    private ContentDataFragment() {
        super(R.layout.fragment_content_data);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (view == null) {
            throw new NullPointerException();
        }

        inputTitle = view.findViewById(R.id.input_title);
        inputContent = view.findViewById(R.id.input_content);
        buttonManipulate = view.findViewById(R.id.button_manipulate_content);

        toggleStateEnableView(false);

        if (getParentFragment() instanceof PersistDataFragment) {
            ((PersistDataFragment) getParentFragment()).setCommandListener(this);
        }

        buttonManipulate.setOnClickListener(this);

        return view;
    }

    private void toggleStateEnableView(boolean state) {
        inputTitle.setEnabled(state);
        inputContent.setEnabled(state);
        buttonManipulate.setEnabled(state);
        toggleStateColorView();
    }

    private void toggleStateColorView() {
        toggleStateColorInputTitle();
        toggleStateColorInputContent();
    }

    private void toggleStateColorInputTitle() {
        boolean state = inputTitle.isEnabled();

        inputTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
        inputTitle.setBackgroundColor(ContextCompat.getColor(requireContext(),
                state ? android.R.color.transparent : android.R.color.darker_gray));
    }

    private void toggleStateColorInputContent() {
        boolean state = inputContent.isEnabled();

        inputContent.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
        inputContent.setBackgroundColor(ContextCompat.getColor(requireContext(),
                state ? android.R.color.transparent : android.R.color.darker_gray));
    }

    private void resetView() {
        inputTitle.setText("");
        inputContent.setText("");
        buttonManipulate.setText("");
    }

    @Override
    public void requestCommand(FileModel fileModel) {
        resetView();
        toggleStateEnableView(true);

        if (fileModel != null) {
            this.fileModel = fileModel;
            if (fileModel.getId() == R.id.button_create_file) {
                buttonManipulate.setText(getString(R.string.create));
            } else if (fileModel.getId() == R.id.button_update_file) {
                buttonManipulate.setText(getString(R.string.update));
            } else if (fileModel.getId() == R.id.button_read_file) {
                inputContent.setEnabled(false);
                buttonManipulate.setText(getString(R.string.read));
            } else if (fileModel.getId() == R.id.button_del_file) {
                inputContent.setEnabled(false);
                buttonManipulate.setText(getString(R.string.del_file));
            } else {
                throw new IllegalArgumentException(getString(R.string.command_not_found_message));
            }
        }

        toggleStateColorView();
    }

    private void executeCommand(FileModel fileModel) {
        if (fileModel.getId() == R.id.button_create_file) {
            createFile(fileModel.getTitle(), fileModel.getContent());
        } else if (fileModel.getId() == R.id.button_update_file) {
            updateFile(fileModel.getTitle(), fileModel.getContent());
        } else if (fileModel.getId() == R.id.button_read_file) {
            readFile(fileModel.getTitle());
        } else if (fileModel.getId() == R.id.button_del_file) {
            deleteFile(fileModel.getTitle());
        } else {
            throw new IllegalArgumentException(getString(R.string.command_not_found_message));
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_manipulate_content) {
            if (fileModel != null) {
                int id = fileModel.getId();
                String title = inputTitle.getText().toString().trim();
                String content = inputContent.getText().toString().trim();

                if (title.contains(" ")) {
                    title = title.replace(' ', '_');
                }

                FileModel file = new FileModel(id, title, content);
                executeCommand(file);

                toggleStateEnableView(false);
            }
        } else {
            throw new IllegalArgumentException(String.format(
                    getString(R.string.id_not_found_message),
                    view.getId()));
        }
        getMyParentFragment().closeKeyboard(view);
    }

    private void createFile(String fileName, String contentFile) {
        try {
            File path;
            if (getMyParentFragment().getMode(getArguments()) == BaseFragment.Mode.INTERNAL) {
                path = requireActivity().getFilesDir();
            } else {
                String state = Environment.getExternalStorageState();

                if (!Environment.MEDIA_MOUNTED.equals(state)) {
                    return;
                }

                // path = Environment.getExternalStorageDirectory();
                path = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            }

            File file = new File(path, fileName);

            boolean fileCreated = file.createNewFile();

            if (fileCreated) {
                FileOutputStream outputStream = new FileOutputStream(file, true);
                outputStream.write(contentFile.getBytes());
                outputStream.flush();
                outputStream.close();
            }

            getMyParentFragment().showToastMessage(String.format(
                    getString(R.string.create_success_message),
                    fileName));
        } catch (Exception e) {
            e.printStackTrace();
            getMyParentFragment().showToastMessage(String.format(
                    getString(R.string.create_fail_message),
                    fileName));
        }
    }

    private void updateFile(String fileName, String newContentFile) {
        try {
            File path;
            if (getMyParentFragment().getMode(getArguments()) == BaseFragment.Mode.INTERNAL) {
                path = requireActivity().getFilesDir();
            } else {
                String state = Environment.getExternalStorageState();

                if (!Environment.MEDIA_MOUNTED.equals(state)) {
                    return;
                }

                // path = Environment.getExternalStorageDirectory();
                path = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            }

            File file = new File(path, fileName);

            boolean fileCreated = file.createNewFile();

            if (fileCreated) {
                createFile(fileName, newContentFile);
                return;
            }

            FileOutputStream outputStream = new FileOutputStream(file, false);
            outputStream.write(newContentFile.getBytes());
            outputStream.flush();
            outputStream.close();

            getMyParentFragment().showToastMessage(String.format(
                    getString(R.string.update_success_message),
                    fileName));
        } catch (Exception e) {
            e.printStackTrace();
            getMyParentFragment().showToastMessage(String.format(
                    getString(R.string.update_fail_message),
                    fileName));
        }
    }

    private void readFile(String fileName) {
        File path = getMyParentFragment().getMode(getArguments()) == BaseFragment.Mode.INTERNAL ?
                requireActivity().getFilesDir() :
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, fileName);

        if (!file.exists()) {
            getMyParentFragment().showToastMessage(getString(R.string.file_not_found));
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder text = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                text.append(line).append("\n");
            }

            getMyParentFragment().showToastMessage(String.format(
                    getString(R.string.read_success_message),
                    fileName));

            inputContent.setText(text.toString());
        } catch (IOException e) {
            getMyParentFragment().showToastMessage(String.format(
                    getString(R.string.read_fail_message),
                    fileName));
        }
    }

    private void deleteFile(String fileName) {
        File path = getMyParentFragment().getMode(getArguments()) == BaseFragment.Mode.INTERNAL ?
                requireActivity().getFilesDir() :
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        File file = new File(path, fileName);

        if (!file.exists()) {
            getMyParentFragment().showToastMessage(getString(R.string.file_not_found));
            return;
        }

        boolean fileDeleted = file.delete();

        String message = String.format(getString(
                fileDeleted ?
                        R.string.del_success_message :
                        R.string.del_fail_message),
                fileName);

        getMyParentFragment().showToastMessage(message);
    }

    private BaseFragment getMyParentFragment() {
        return ((PersistDataFragment) getParentFragment());
    }

}
