package com.tamn.app2uproject;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.tamn.app2uproject.Adapter.GeneralQuestionsViewPager;

public class GeneralQuestionsActivity extends AppCompatActivity {
    TabLayout tabsGq ;
    ViewPager viewPagerGq ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarGq);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.general_questions);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabsGq = (TabLayout) findViewById(R.id.tabsGq);
        viewPagerGq = (ViewPager) findViewById(R.id.viewPagerGq);
        GeneralQuestionsViewPager adapter = new GeneralQuestionsViewPager(getSupportFragmentManager());
        viewPagerGq.setAdapter(adapter);
        tabsGq.setupWithViewPager(viewPagerGq);


    }

}
