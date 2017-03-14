package com.akansh.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.google.firebase.auth.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CardViewActivity extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    private ProgressDialog pd;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    public ArrayList<String> su;
    public String actualval;
    public String preval;
    public ArrayList<com.akansh.myapplication.UserInfo> userHistory;


    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view =  inflater.inflate(R.layout.activity_card_view,container, false);
        Log.i(LOG_TAG, " In card view activity ");
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("User History");
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);
        ImageSwitcher im=(ImageSwitcher) getActivity().findViewById(R.id.backdrop);
        int imageResource = getResources().getIdentifier("@drawable/cyan_desktop", null, getActivity().getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        im.setImageDrawable(res);
        return  view;

    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState){
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

        super.onViewCreated(view,savedInstanceState);
        userHistory=new ArrayList<com.akansh.myapplication.UserInfo>();
        /*mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
*/
        final Activity ac=getActivity();
        Log.i(LOG_TAG, " view created here ");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(user.getUid());
        Log.i(LOG_TAG, " user got here ");
        pd.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
        int i=0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Locations updated", "location: ");
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {

                    userHistory.add(locationSnapshot.getValue(com.akansh.myapplication.UserInfo.class));

                    Log.d("Locations updated", "location: "+userHistory.size());

                }

                if(pd.isShowing()) {
                    mRecyclerView = (RecyclerView) ac.findViewById(R.id.my_recycler_view);
                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(ac);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new MyRecyclerViewAdapter(getContext(), userHistory,getActivity());
                    mRecyclerView.setAdapter(mAdapter);
                    pd.dismiss();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.i(LOG_TAG, " Data get done ");
        /*mRecyclerView = (RecyclerView) ac.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(ac);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getContext(),userHistory);
        mRecyclerView.setAdapter(mAdapter);
        */
        //mAdapter = new MyRecyclerViewAdapter(getContext(),userHistory);
        //mRecyclerView.setAdapter(mAdapter);


        //Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteItem(index);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private ArrayList<UserInfo> getDataSet() {

        return userHistory;
    }
}