package com.questio.projects.questio.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.cengalabs.flatui.FlatUI;
import com.cengalabs.flatui.views.FlatEditText;
import com.questio.projects.questio.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public class InventoryActivity extends AppCompatActivity {
    @Bind(R.id.inventory_toolbar)
    Toolbar toolbar;

    @Bind(R.id.inventory_filter_name2)
    EditText filterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlatUI.initDefaultValues(this);
        FlatUI.setDefaultTheme(FlatUI.SKY);
        setContentView(R.layout.inventory_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
