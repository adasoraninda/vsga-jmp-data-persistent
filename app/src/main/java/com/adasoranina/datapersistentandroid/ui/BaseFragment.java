package com.adasoranina.datapersistentandroid.ui;

import android.content.Context;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    protected FragmentInstanceListener fragmentInstanceListener;

    public BaseFragment(@LayoutRes int layoutRes) {
        super(layoutRes);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        fragmentInstanceListener = ((MainActivity) context);
        super.onAttach(context);
    }


    public interface FragmentInstanceListener {
        void setFragment(Fragment fragment);

        void setTitle(String title, boolean displayBackButton);
    }

    public enum Mode {
        INTERNAL, EXTERNAL, HOME
    }

}
