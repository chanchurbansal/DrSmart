package com.akansh.myapplication;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.akansh.myapplication.R.id.listView;

/**
 * Created by chanc on 20-10-2016.
 */

public class fragment_predict extends Fragment {
//---------------------------------
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    public ArrayList<String> su,su2;
//-----------------------------------
    public final String ip_add="minor.chanchurbansal.me";//"138.197.8.217:8000";
    public TextView result;
    public Button onSubmit;
    public String params;
    public String url;
    RequestQueue queue;
    public Spinner spinnerList;
    ListView listView1;
    ProgressDialog pd;
    public TextView[] textArray;
    public TableRow[] tr_head;
    protected  String diseaseName;
    public EditText[] textInput;
    public String[] att_names;
    public ArrayList<Diseases> dataSet;

    @Override
    public View onCreateView (final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_predict_xml, container, false);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Diagnosis");
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);
        ImageSwitcher im=(ImageSwitcher) getActivity().findViewById(R.id.backdrop);
        int imageResource = getResources().getIdentifier("@drawable/materialdesign_introduct", null, getActivity().getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        im.setImageDrawable(res);


        pd = new ProgressDialog(view.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Please wait while we fetch data for you.");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);

        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

                pd.dismiss();
                queue.cancelAll(new RequestQueue.RequestFilter() {
                    @Override
                    public boolean apply(Request<?> request) {
                        return true;
                    }
                });

                ((MainActivity)getActivity()).onBackPressed();

            }
        });

        super.onViewCreated(view, savedInstanceState);
        //spinnerList = (Spinner) view.findViewById(R.id.spinner);
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        getActivity().setTitle("Diagnosis");

        url = "http://" + ip_add + "/getDiseases";

        if(dataSet==null)
        {
            dataSet=new ArrayList<Diseases>();
        }
        StringRequest getRequest1 = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onResponse(String response) {
                        // display response
                        pd.dismiss();
                        Log.d("Response", response.toString());
                        String[] _s = response.toString().split("/");
                        final String[] arraySpinner = _s[0].split(",");
                        final String[] diseaseFileName = _s[1].split(",");
                        final String[] accuracy = _s[2].split(",");
                        //Toast.makeText(getActivity(),diseaseFileName[0]+diseaseFileName[1],Toast.LENGTH_LONG).show();







                        for(int i=0;i<arraySpinner.length;i++){
                            dataSet.add(new Diseases(arraySpinner[i],diseaseFileName[i],Float.parseFloat(accuracy[i])));
                        }
                        LinearLayout list=(LinearLayout)view.findViewById(R.id.relativeList);
                        MyAdapterAccuracy adapter1 = new MyAdapterAccuracy(dataSet,getActivity().getApplicationContext(),getFragmentManager());

                        int adapterCount = adapter1.getCount();

                        for (int i = 0; i < adapterCount; i++) {
                            View item = adapter1.getView(i, null, null);
                            list.addView(item);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        pd.dismiss();
                        //Toast.makeText(getActivity(),"error"+error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
        );

        getRequest1.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(getRequest1);

        queue.start();
        pd.show();


        return view;
    }



}
