package com.akansh.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * Created by chanc on 25-10-2016.
 */

public  class fragment_monitor extends AppCompatActivity implements ViewSwitcher.ViewFactory {
    public static Switch  swt;
    public TextView monitor_txt;
    public FrameLayout monitor_frame;
    public Fragment frag;
    public FragmentTransaction ft;
 //   public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
 protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);

     setTitle("Home");
     setContentView(R.layout.fragment_monitor);
      //  View view =  inflater.inflate(R.layout.fragment_monitor,container, false);

    initCollapsingToolbar();
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar1);
        collapsingToolbar.setTitle("Heart Rate Monitor");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar1);
        appBarLayout.setExpanded(true);
        ImageSwitcher im=(ImageSwitcher) findViewById(R.id.backdrop1);
        int imageResource = getResources().getIdentifier("@drawable/heart_monitor", null, getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        im.setImageDrawable(res);
   /*     return  view;
    }

   public void onViewCreated (View view, Bundle savedInstanceState) {
        frag = null;

        super.onViewCreated(view, savedInstanceState);

*/


        swt = (Switch) findViewById(R.id.mySwitch);
        monitor_frame = (FrameLayout) findViewById(R.id.monitor_frame);
        frag=new fragment_monitor_txt();
        ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.monitor_frame,frag);
        ft.commit();

        swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    frag=new HeartRateMonitor();
                    ft=getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.monitor_frame,frag);
                    ft.commit();

                }
                else{
                    frag=new fragment_monitor_txt();
                    ft=getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.monitor_frame,frag);
                    ft.commit();
                }

            }

        });
        //  Intent intent = new Intent(getActivity().getApplication(), HeartRateMonitor.class);
        // startActivity(intent);

    }


    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar1);
        collapsingToolbar.setTitle(getString(R.string.app_name));
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar1);
        appBarLayout.setExpanded(true);
        ImageSwitcher mSwitcher = (ImageSwitcher) findViewById(R.id.backdrop1);
        mSwitcher.setFactory(this);
        mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in));
        mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out));

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                findViewById(R.id.subTitle).
                        setAlpha((float) 1.0 - (Math.abs(verticalOffset / (float)
                                appBarLayout.getTotalScrollRange())));

            }
        });


    }
    public View makeView() {
        ImageView i = new ImageView(this);
        i.setBackgroundColor(0xFF000000);
        i.setScaleType(ImageView.ScaleType.CENTER_CROP);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT,
                AppBarLayout.LayoutParams.MATCH_PARENT));
        return i;
    }



}
