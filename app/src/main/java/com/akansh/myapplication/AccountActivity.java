package com.akansh.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends Fragment {

    public Button logoutbtn;

    public FirebaseAuth auth;
    public FirebaseAuth.AuthStateListener authlistener;

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view =  inflater.inflate(R.layout.fragment_predict_xml,container, false);


        return  view;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        logoutbtn = (Button ) getView().findViewById(R.id.logoutbtn);

        auth= FirebaseAuth.getInstance();
        authlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null){
                    startActivity (new Intent(getActivity(), LoginActivity.class));

                }
            }
        };

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth.signOut();
            }
        });

    }

    public   void onStart(){
        super.onStart();

        auth.addAuthStateListener(authlistener);

    }

}
