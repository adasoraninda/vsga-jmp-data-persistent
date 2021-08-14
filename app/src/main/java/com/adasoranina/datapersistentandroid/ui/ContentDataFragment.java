package com.adasoranina.datapersistentandroid.ui;

import static com.adasoranina.datapersistentandroid.ui.PersistDataFragment.MODE;

import android.os.Bundle;
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
import com.adasoranina.datapersistentandroid.model.ExternalStorage;
import com.adasoranina.datapersistentandroid.model.FileModel;
import com.adasoranina.datapersistentandroid.model.IEStorage;
import com.adasoranina.datapersistentandroid.model.InternalStorage;
import com.adasoranina.datapersistentandroid.model.MessageCallback;

public class ContentDataFragment extends Fragment implements PersistDataFragment.CommandContentListener, View.OnClickListener {

    private EditText inputTitle;
    private EditText inputContent;
    private Button buttonManipulate;
    private IEStorage ieStorage;

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

        ieStorage = getMyParentFragment().getMode(getArguments()) == BaseFragment.Mode.INTERNAL ?
                new InternalStorage(requireContext()) :
                new ExternalStorage(requireContext());

        toggleStateEnableView(false);
        buttonManipulate.setOnClickListener(this);

        if (getParentFragment() instanceof PersistDataFragment) {
            ((PersistDataFragment) getParentFragment()).setCommandListener(this);
        }

        return view;
    }

    private BaseFragment getMyParentFragment() {
        return ((PersistDataFragment) getParentFragment());
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
            ieStorage.createFile(fileModel.getTitle(), fileModel.getContent(), new MessageCallback() {
                @Override
                public void success(String message) {
                    getMyParentFragment().showToastMessage(message);
                }

                @Override
                public void error(String message) {
                    getMyParentFragment().showToastMessage(message);
                }
            });

        } else if (fileModel.getId() == R.id.button_update_file) {
            ieStorage.updateFile(fileModel.getTitle(), fileModel.getContent(), new MessageCallback() {
                @Override
                public void success(String message) {
                    getMyParentFragment().showToastMessage(message);
                }

                @Override
                public void error(String message) {
                    getMyParentFragment().showToastMessage(message);
                }
            });
        } else if (fileModel.getId() == R.id.button_read_file) {
            ieStorage.readFile(fileModel.getTitle(), new MessageCallback() {
                @Override
                public void success(String message) {
                    getMyParentFragment().showToastMessage(String.format(
                            getString(R.string.read_success_message),
                            fileModel.getTitle()));
                    inputContent.setText(message);
                }

                @Override
                public void error(String message) {
                    getMyParentFragment().showToastMessage(message);
                }
            });
        } else if (fileModel.getId() == R.id.button_del_file) {
            ieStorage.deleteFile(fileModel.getTitle(), new MessageCallback() {
                @Override
                public void success(String message) {
                    getMyParentFragment().showToastMessage(message);
                }

                @Override
                public void error(String message) {
                    getMyParentFragment().showToastMessage(message);
                }
            });
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

}
