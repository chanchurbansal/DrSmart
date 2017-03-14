package com.akansh.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.CircularArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by chanc on 25-10-2016.
 */

public class fragment_home extends Fragment {

    public ImageButton imageView;
    public ImageButton monitorbutton;
    public FirebaseAuth.AuthStateListener authListener;
    public TextView total_pred,right_pred;
    public CircleDisplay circleDisplay1,circleDisplay2;
    public    Intent intent;
    private static String LOG_TAG = "CardViewActivity";
    private ProgressDialog pd;
    private FirebaseAuth auth;
    public int count=0,pred_right,pred_total;
    private DatabaseReference databaseReference;
    public ArrayList<UserInfo> userHistory;


    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view =  inflater.inflate(R.layout.home_xml,container, false);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Home");
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);
        ImageSwitcher im=(ImageSwitcher) getActivity().findViewById(R.id.backdrop);
        int imageResource = getResources().getIdentifier("@drawable/home_image", null, getActivity().getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        im.setImageDrawable(res);

        return  view;
    }

    public void onViewCreated (View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");
        userHistory=new ArrayList<com.akansh.myapplication.UserInfo>();
        pd = new ProgressDialog(view.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Please wait while we fetch data for you.");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);

        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

                pd.dismiss();
                ((MainActivity)getActivity()).onBackPressed();

            }
        });

        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity

                    startActivity(new Intent(getActivity(), LoginActivity.class));
                  //  finish();
                }

            }
        };

        Log.i(LOG_TAG, " from databse");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(user.getUid());

        Log.i(LOG_TAG, " user got here ");
        //pd.show();
        circleDisplay1=(CircleDisplay) getActivity().findViewById(R.id.circleDisplay1);
        total_pred =(TextView) getActivity().findViewById(R.id.total_pred);

        circleDisplay2=(CircleDisplay) getActivity().findViewById(R.id.circleDisplay2);
        right_pred =(TextView) getActivity().findViewById(R.id.right_pred);

        // circleDisplay1.setOnClickListener(this);

        circleDisplay1.setColor(Color.alpha(R.color.bg_main));
        circleDisplay1.setAnimDuration(1000);
        circleDisplay1.setValueWidthPercent(55f);
        circleDisplay1.setTextSize(15f);
        circleDisplay1.setDrawText(true);
        circleDisplay1.setDrawInnerCircle(true);
        circleDisplay1.setFormatDigits(1);
        circleDisplay1.setTouchEnabled(false);
        circleDisplay1.setUnit("%");
        circleDisplay1.setStepSize(0.5f);
        circleDisplay1.inside_txt="Result:";



        circleDisplay2.setColor(Color.alpha(R.color.bg_main));
        circleDisplay2.setAnimDuration(1000);
        circleDisplay2.setValueWidthPercent(55f);
        circleDisplay2.setTextSize(15f);
        circleDisplay2.setDrawText(true);
        circleDisplay2.setDrawInnerCircle(true);
        circleDisplay2.setFormatDigits(1);
        circleDisplay2.setTouchEnabled(false);
        circleDisplay2.setUnit("%");
        circleDisplay2.setStepSize(0.5f);

        circleDisplay2.inside_txt="Result:";


        count=0;
        pred_right=0;
        pred_total=0;
        total_pred.setText("Diagnosis for which Actual Value is entered by user");
        right_pred.setText("Accurate Diagnosis\nin User History");
        if(user!=null) {
            databaseReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("Locations updated", "location: ");
                    for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {

                        userHistory.add(locationSnapshot.getValue(com.akansh.myapplication.UserInfo.class));
                        if (userHistory.get(count).actualVal.equals("NIL")) {
                        } else {
                            if (userHistory.get(count).actualVal.equals(userHistory.get(count).predVal)) {
                                pred_right++;
                            }
                            pred_total++;
                        }
                        count++;
                        Log.d("Locations updated", "location: " + userHistory.size());

                    }
                    //total_pred.setText("Times \nDiagnosed :"+count);
                    circleDisplay1.showValue(((float) pred_total / (float) count) * 100f, 100f, true);

                    //right_pred.setText("Real Data\nFrom User: "+count);
                    circleDisplay2.showValue(((float) pred_right / (float) pred_total) * 100f, 100f, true);
                    Log.d("total predicted  :", "" + count);
                    Log.d("predicted  right :", "" + pred_right);
                    Log.d("actual val given   :", "" + pred_total);

                    if (pd.isShowing()) {

                        pd.dismiss();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
/*
        Log.d("total predicted  :", ""+count);
        Log.d("predicted  right :", ""+pred_right);
        Log.d("actual val given   :", ""+pred_total);
*/



        imageView = (ImageButton) getActivity().findViewById(R.id.imageButton);
        monitorbutton =(ImageButton) getActivity().findViewById(R.id.monitorbutton);
        imageView.setBackgroundResource(R.drawable.imagebutton_img);
        monitorbutton.setBackgroundResource(R.drawable.heart_monitor);
        //set the ontouch listener
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Fragment frag=new MainDiet();
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,frag);
                ft.commit();
            }
        });



        //set the ontouch listener
        monitorbutton.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getActivity().getApplication(), fragment_monitor.class);
                startActivity(intent);
            }
        });


    }



}
