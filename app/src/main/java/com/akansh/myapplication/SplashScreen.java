package com.akansh.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;

/**
 * Created by chanc on 03-11-2016.
 */

public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 3000;
    public final String ip_add="minor.chanchurbansal.me";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        if(isWorkingInternetPersent()) {

                new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */


                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        Intent i = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        // close this activity
                        finish();
                    }
                }, SPLASH_TIME_OUT);

        }
        else{
            showAlertDialog(SplashScreen.this, "Internet Connection",
                    "You don't have internet connection", false);
        }
    }


    public boolean isWorkingInternetPersent() {

        //return false;
       ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(SplashScreen.this.CONNECTIVITY_SERVICE);
        if (connectivityManager != null
                && connectivityManager.getActiveNetworkInfo()!=null
                && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnected()) {

            return true;


        }
        return false;
    }
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog.Builder alertDialogB = new AlertDialog.Builder(context);

        // Setting Dialog Message
        alertDialogB.setMessage(message);

        // Setting alert dialog icon
        // alertDialog.setIcon((status) ? R.mipmap.ic_launcher : R.mipmap.ic_launcher);

        // Setting OK Button
        alertDialogB.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                finish();
                System.exit(0);
            }
        });

        // Showing Alert Message
        AlertDialog alertDialog = alertDialogB.create();
        alertDialog.show();
    }
}
