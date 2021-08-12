package com.adasoranina.datapersistentandroid.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adasoranina.datapersistentandroid.R;
import com.adasoranina.datapersistentandroid.model.FileModel;

public class PersistDataFragment extends BaseFragment implements View.OnClickListener {

    private CommandContentListener cmdListener;

    public static PersistDataFragment getInstance(BaseFragment.Mode mode) {
        PersistDataFragment fragment = new PersistDataFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MODE, mode);
        fragment.setArguments(bundle);

        return fragment;
    }

    private PersistDataFragment() {
        super(R.layout.fragment_persist_data);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (view == null) {
            throw new NullPointerException();
        }

        Button buttonCreateFile = view.findViewById(R.id.button_create_file);
        Button buttonUpdateFile = view.findViewById(R.id.button_update_file);
        Button buttonReadFile = view.findViewById(R.id.button_read_file);
        Button buttonDeleteFile = view.findViewById(R.id.button_del_file);

        buttonCreateFile.setOnClickListener(this);
        buttonUpdateFile.setOnClickListener(this);
        buttonReadFile.setOnClickListener(this);
        buttonDeleteFile.setOnClickListener(this);

        fragmentInstanceListener.setTitle(getMode(getArguments()).name(), true);

        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.frame_content, ContentDataFragment.getInstance(getMode(getArguments())))
                    .commit();
        }

        isStoragePermissionGranted();

        return view;
    }

    public void setCommandListener(CommandContentListener cmdListener) {
        this.cmdListener = cmdListener;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_create_file) {
            cmdListener.requestCommand(new FileModel(view.getId()));
        } else if (view.getId() == R.id.button_update_file) {
            cmdListener.requestCommand(new FileModel(view.getId()));
        } else if (view.getId() == R.id.button_read_file) {
            cmdListener.requestCommand(new FileModel(view.getId()));
        } else if (view.getId() == R.id.button_del_file) {
            cmdListener.requestCommand(new FileModel(view.getId()));
        } else {
            throw new IllegalArgumentException(String.format(
                    getString(R.string.id_not_found_message),
                    view.getId()));
        }
        closeKeyboard(view);
    }

    public interface CommandContentListener {
        void requestCommand(@Nullable FileModel fileModel);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == BaseFragment.CODE_REQUEST_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToastMessage(getString(R.string.permission_granted));
            } else {
                requireActivity().onBackPressed();
            }
        }
    }
}
