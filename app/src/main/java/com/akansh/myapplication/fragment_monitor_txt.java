package com.akansh.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * Created by chanc on 25-10-2016.
 */

public class fragment_monitor_txt extends Fragment {

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view =  inflater.inflate(R.layout.fragment_monitor_text,container, false);


        return  view;
    }

    public void onViewCreated (View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        //  Intent intent = new Intent(getActivity().getApplication(), HeartRateMonitor.class);
        // startActivity(intent);

    }

}
