package com.adasoranina.datapersistentandroid.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adasoranina.datapersistentandroid.R;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (view == null) {
            throw new NullPointerException();
        }

        Button buttonInternal = view.findViewById(R.id.button_internal);
        Button buttonExternal = view.findViewById(R.id.button_external);

        buttonInternal.setOnClickListener(this);
        buttonExternal.setOnClickListener(this);

        fragmentInstanceListener.setTitle(getString(R.string.app_name), false);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_internal) {
            fragmentInstanceListener.setFragment(
                    PersistDataFragment.getInstance(Mode.INTERNAL));
        } else if (view.getId() == R.id.button_external) {
            fragmentInstanceListener.setFragment(
                    PersistDataFragment.getInstance(Mode.EXTERNAL));
        } else {
            throw new IllegalArgumentException(String.format(
                    getString(R.string.id_not_found_message),
                    view.getId()));
        }
    }

}
