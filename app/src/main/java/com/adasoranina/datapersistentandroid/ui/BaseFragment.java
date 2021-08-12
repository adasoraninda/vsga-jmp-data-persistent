package com.adasoranina.datapersistentandroid.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    public static final String MODE = "MODE";
    public static final int CODE_REQUEST_STORAGE = 101;

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
        INTERNAL, EXTERNAL
    }

    protected Mode getMode(Bundle bundle) {
        if (bundle == null) {
            throw new NullPointerException();
        }

        return (Mode) bundle.get(MODE);
    }

    protected void showToastMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void closeKeyboard(View view) {
        InputMethodManager imm =
                (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected void isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requireActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        requireActivity(),
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                        },
                        CODE_REQUEST_STORAGE);
            }
        }
    }

}
