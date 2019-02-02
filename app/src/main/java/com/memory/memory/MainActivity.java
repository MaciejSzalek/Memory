package com.memory.memory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ViewPager mPager;
    public PagerAdapter mPagerAdapter;
    public TabLayout tabLayout;
    private EventBus bus = EventBus.getDefault();

    private static final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.toolbar_title));
        toolbar.setTitle("Memory");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mPager = findViewById(R.id.pager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addTitle("Product");
        mPagerAdapter.addTitle("ToDo");
        mPager.setAdapter(mPagerAdapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mPager);

        bus.register(this);
        //sendStringToFragment("Test EventBus");
    }
    public void sendStringToFragment(String str){
        Events.EventProduct eventProduct = new Events.EventProduct(str);
        bus.post(eventProduct);
    }


    @Subscribe
    public void getProduct(Events.EventProduct events){}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if(resultCode == Activity.RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    sendStringToFragment(result.get(0));

                }
                break;
            }
        }
    }
}
