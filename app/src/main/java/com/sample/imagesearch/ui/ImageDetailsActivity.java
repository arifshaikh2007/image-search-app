package com.sample.imagesearch.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.sample.data.models.ImageDataModel;
import com.sample.imagesearch.R;

public class ImageDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);
        if (savedInstanceState == null) {
            Fragment fragment = new ImageDetailsFragment();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.image_details_fragment_container, fragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showActionbar();
    }

    private void showActionbar() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            final Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                final ImageDataModel imageDataModel = bundle.getParcelable("image_details");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle((imageDataModel != null) ?imageDataModel.getTitle() : "");
                getSupportActionBar().show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}