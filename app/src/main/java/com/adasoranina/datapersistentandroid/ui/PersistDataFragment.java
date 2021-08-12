package com.adasoranina.datapersistentandroid.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adasoranina.datapersistentandroid.R;

public class PersistDataFragment extends BaseFragment {

    public static final String MODE = "MODE";

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

        fragmentInstanceListener.setTitle(getTitle(), true);

        return view;
    }

    private String getTitle() {
        if (getArguments() == null) {
            throw new NullPointerException();
        }

        Mode mode = (Mode) getArguments().get(MODE);

        return mode.name();
    }

}
