package com.adasoranina.datapersistentandroid.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.adasoranina.datapersistentandroid.R;

public class MainActivity extends AppCompatActivity implements BaseFragment.FragmentInstanceListener {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);

        setFragment(new HomeFragment());
    }

    @Override
    public void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_root, fragment);

        if (fragment instanceof HomeFragment) {
            transaction.commit();
        } else {
            transaction
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void setTitle(String title, boolean displayBackButton) {
        toolbar.setTitle(title);

        Drawable icon = ContextCompat.getDrawable(this, R.drawable.ic_round_arrow_back);
        toolbar.setNavigationIcon(displayBackButton ? icon : null);
        toolbar.setNavigationOnClickListener(v -> getSupportFragmentManager().popBackStack());
    }
}