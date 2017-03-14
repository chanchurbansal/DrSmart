package com.akansh.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.zzc;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chanc on 21-11-2016.
 */

public class prediction extends Fragment {

    //public final String ip_add = getResources().getString(R.string.server_ip);
    public TextView result;
    public Button onSubmit;
    public String params;
    public String url;
    RequestQueue queue;
    public Spinner spinnerList;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    public ArrayList<String> su, su2;
    ProgressDialog pd;
    public TextView[] textArray;
    public TableRow[] tr_head;
    protected String diseaseName;
    public EditText[] textInput;
    public String[] att_names;
    public String[] checkValues;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    public boolean authenticateData()
    {
        for (int i=0;i<checkValues.length;i++)
        {
            if(att_names[i].equals(textInput[i].getText().toString()) || textInput[i].getText().toString().equals(""))
                continue;
            String s[]=checkValues[i].split(" ");
            if(s[0].equals("type"))
            {
                if(s[1].equals("int"))
                {
                    try{
                        int j=Integer.parseInt(textInput[i].getText().toString());
                    }
                    catch (Exception e)
                    {
                        textInput[i].setTextColor(Color.RED);
                        return false;
                    }
                }
                if(s[1].equals("float"))
                {
                    try{
                        float j=Float.parseFloat(textInput[i].getText().toString());
                    }
                    catch (Exception e)
                    {
                        textInput[i].setTextColor(Color.RED);
                        return false;
                    }
                }
            }


            if(s[0].equals("range"))
            {
                if(s[1].equals("int"))
                {
                    int lower=Integer.parseInt(s[2]);
                    int up=Integer.parseInt(s[3]);
                    try{
                        int j=Integer.parseInt(textInput[i].getText().toString());
                        if(j<lower || j>up)
                        {
                            textInput[i].setTextColor(Color.RED);
                            return false;
                        }
                    }
                    catch (Exception e)
                    {
                        textInput[i].setTextColor(Color.RED);
                        return false;
                    }
                }
                if(s[1].equals("float"))
                {
                    float lower= Float.parseFloat(s[2]);
                    float up=Float.parseFloat(s[3]);
                    try{
                        float j=Float.parseFloat(textInput[i].getText().toString());
                        if(j<lower || j>up)
                        {
                            textInput[i].setTextColor(Color.RED);
                            return false;
                        }

                    }
                    catch (Exception e)
                    {
                        textInput[i].setTextColor(Color.RED);
                        return false;
                    }
                }
            }
        }

        return true;
    }
    public View onCreateView (final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        final View view = inflater.inflate(R.layout.prediction_xml, container, false);

        super.onViewCreated(view, savedInstanceState);
        //setHasOptionsMenu(true);
        ((MainActivity)getActivity()).enablePredictionHome(true);
        //Bundle bundle = getIntent().getExtras();
        diseaseName = getArguments().getString("diseaseName");
        String image_disease= "@drawable/"+diseaseName.toLowerCase();
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(diseaseName);
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);
        ImageSwitcher im=(ImageSwitcher) getActivity().findViewById(R.id.backdrop);
        int imageResource = getResources().getIdentifier(image_disease, null, getActivity().getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        im.setImageDrawable(res);

        final String diseaseFileName = getArguments().getString("diseaseFileName");

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        if (auth.getCurrentUser() == null) {
            // user auth state is changed - user is null
            // launch login activity

            startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
        }
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

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());


        url = "http://" + getResources().getString(R.string.server_ip) + "/getAttributes/?q=" + diseaseFileName;
        final TableLayout tl = (TableLayout) view.findViewById(R.id.TableLayout_predict);

        StringRequest getRequest2 = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {

                        pd.dismiss();
                        String[] _s = response.toString().split("/");
                        att_names = _s[0].split(",");
                        final String[] att_value = _s[1].split(",");
                        checkValues=att_value;

                        final String fileName = diseaseFileName;

                        textArray = new TextView[att_names.length];
                        tr_head = new TableRow[att_names.length];
                        textInput = new EditText[att_names.length];
                        LayoutParams lp = new LayoutParams(
                                LayoutParams.WRAP_CONTENT,
                                LayoutParams.WRAP_CONTENT
                        );
                        LayoutParams lp1 = new LayoutParams(

                                LayoutParams.MATCH_PARENT,
                                LayoutParams.WRAP_CONTENT
                        );
                        lp.setMargins(0, 0, 0, 0);
                        lp1.setMargins(0, 0, 0, 0);
                        //att_length=att_names.length;
                        for (int i = 0; i < att_names.length; i++) {
                            att_value[i] = att_value[i].substring(0, att_value[i].length() - 2);
                            //checkValues[i] = checkValues[i].substring(0, checkValues[i].length() - 2);
                            String []a=att_value[i].split(" ");

                            tr_head[i] = new TableRow(getActivity().getApplicationContext());


                            textInput[i] = new EditText(getActivity().getApplicationContext());
                            textInput[i].setTextColor(Color.GRAY);
                            textInput[i].setLayoutParams(lp);
                            textInput[i].setImeOptions(EditorInfo.IME_ACTION_NEXT);
                            textInput[i].setGravity(Gravity.LEFT);
                            textInput[i].setSingleLine(true);
                            textInput[i].setMaxLines(1);
                            textInput[i].setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                            Log.i("PREDICTION",att_value[i]+"12");
                            if(att_value[i].equals("hidden\n") || att_value[i].equals("hidden"))
                            {


                                Log.i("PREDICTION","Hide");
                                textInput[i].setVisibility(View.GONE);
                            }
                            tr_head[i].addView(textInput[i]);

                            final int j = i;
                            final String x = att_names[i]; // string for help i.e. hint
                            textInput[i].setText(x);
                            textInput[i].setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    if (textInput[j].getText().toString().equals(x)) {
                                        textInput[j].setText("");
                                        textInput[j].setTextColor(Color.BLACK);
                                    }
                                    textInput[j].setTextColor(Color.BLACK);
                                }
                            });

                            textInput[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {

                                @Override
                                public void onFocusChange(View v, boolean hasFocus) {
                                    if (!hasFocus && TextUtils.isEmpty(textInput[j].getText().toString())) {
                                        textInput[j].setTextColor(Color.GRAY);
                                        textInput[j].setText(x);
                                    } else if (hasFocus && textInput[j].getText().toString().equals(x)) {
                                        textInput[j].setText("");
                                        textInput[j].setTextColor(Color.BLACK);
                                    }
                                }
                            });


                            tr_head[i].setLayoutParams(lp1);
                            //tr_head[i].setGravity(Gravity.CENTER);

                            if(a[0].equals("choice"))
                            {
                                List<String> name=new ArrayList<String>();
                                ArrayList<String> value=new ArrayList<String>();
                                name.add(x);
                                value.add(x);
                                for(int it =1; it<a.length;it++) {
                                    String s[] = a[it].split("=");
                                    name.add(s[0]);
                                    value.add(s[1]);
                                    Log.i("Here", s[0] + " " + s[1]);
                                }
                                final ArrayList<String> v1=value;
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                                        android.R.layout.simple_spinner_item, name);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                Spinner spin=new Spinner(getContext());
                                spin.setAdapter(dataAdapter);
                                spin.setLayoutParams(lp1);

                                spin.setGravity(Gravity.CENTER);
                                final int it=i;

                                spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        Log.i("Pred",""+i);
                                        textInput[it].setText(v1.get(i));
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                      textInput[it].setText("");
                                    }
                                });
                                textInput[i].measure(0,0);
                                int h=textInput[i].getMeasuredHeight();
                                spin.setMinimumHeight(h);
                                textInput[i].setVisibility(View.GONE);
                                tr_head[i].addView(spin);
                            }


                            tl.addView(tr_head[i]);
                        }

                        result = new TextView(getActivity().getApplicationContext());
                        result.setLayoutParams(lp);
                        result.setTextColor(Color.BLACK);

                        onSubmit = new Button(getActivity().getApplicationContext());
                        onSubmit.setLayoutParams(lp);
                        onSubmit.setTextColor(Color.BLACK);
                        onSubmit.setText("Submit");

                        TableRow tr = new TableRow(getActivity().getApplicationContext());
                        tr.addView(onSubmit);
                        tr.addView(result);

                        tl.addView(tr);

                        onSubmit.setOnClickListener(new View.OnClickListener() {

                            public void onClick(View v) {
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                //imm.hideSoftInputFromWindow(EditTextName.getWindowToken(), 0);
                                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                if(authenticateData()) {
                                    params = "q=" + fileName + "&data=[";
                                    String s;
                                    for (int i = 0; i < textInput.length; i++) {
                                        s = textInput[i].getText().toString();

                                        if (s.equals(att_names[i])) {
                                            s = "";
                                            Log.i("TEST", s + " : " + att_names[i]);

                                        }
                                        params = params + s + ",";

                                    }
                                    params = params.substring(0, params.length() - 1);

                                    params = params + "]";

                                    url = "http://" + getResources().getString(R.string.server_ip) + "/predict/?" + params;


                                    StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    // display response
                                                    pd.dismiss();
                                                    Log.d("Response", response.toString());
                                                    String reply = response.toString();




                                                    AlertDialog.Builder ab=new AlertDialog.Builder(getActivity());
                                                    ab.setCancelable(false);
                                                    ab.setPositiveButton("Go To Diet Chart", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            Fragment frag=new MainDiet();
                                                            FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                                                            ft.replace(R.id.content_frame,frag);
                                                            ft.commit();
                                                            dialogInterface.dismiss();
                                                        }
                                                    });
                                                    ab.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            ((MainActivity)getActivity()).onBackPressed();
                                                            dialogInterface.dismiss();
                                                        }
                                                    });



                                                    //d.show();
                                                    //To
                                                    // ast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
                                                    //if (response.toString().charAt(0) == resultYes)
                                                    result.setVisibility(View.GONE);
                                                    result.setText(response.toString());
                                                    String[] s = reply.split("/");
                                                    reply = s[0];
                                                    result.setText(reply);
                                                    ab.setTitle("Your Diagnosis is "+reply.toUpperCase());

                                                    AlertDialog dialog=ab.create();
                                                    dialog.show();
                                                    //dialog.getButton(dialog.BUTTON_NEGATIVE).setGravity(Gravity.LEFT);
                                                    //dialog.getButton(dialog.BUTTON_NEGATIVE).set
                                                    //LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) dialog.getButton(dialog.BUTTON_NEGATIVE).getLayoutParams();
                                                    //positiveButtonLL.gravity = Gravity.LEFT;
                                                    //dialog.getButton(dialog.BUTTON_NEGATIVE).setLayoutParams(positiveButtonLL);

                                                    dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(Color.rgb(0,150,136));
                                                    dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(Color.rgb(0,150,136));

                                                    String[] p = s[1].split(",");

                                                    ArrayList<String> q = new ArrayList<String>();
                                                    for (int i = 0; i < p.length; i++)
                                                        q.add(p[i]);
                                                    //------------
                                                    saveInfo(reply, q);
                                                    //------------
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    pd.dismiss();
                                                    Log.d("Error.Response", error.toString());
                                                    Toast.makeText(getActivity(), "error" + error.toString(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                    );

                                    getRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                    queue.add(getRequest);
                                    pd.show();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(),"Please Enter Correct Data",Toast.LENGTH_LONG).show();
                                }
                                //queue.start();
                                //Toast.makeText(getActivity(),"YOUR MESSAGE",Toast.LENGTH_LONG).show();
                            }
                        });


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        Log.d("Error.Response", error.toString());
                        //Toast.makeText(getActivity(),"error"+error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
        );

        getRequest2.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(getRequest2);
        pd.show();
        //queue.start();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(getActivity().getApplicationContext()).addApi(AppIndex.API).build();
        return view;

    }




    public String getDateCurrentTimeZone() {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currenTimeZone = new Date();
            return sdf.format(currenTimeZone);
        } catch (Exception e) {
        }
        return "";
    }

    private void saveInfo(String res,ArrayList<String> res_all) {

        su = new ArrayList<String>();
        su2 = new ArrayList<String>();
        String ts = getDateCurrentTimeZone();
        for (int i = 0; i < textInput.length; i++) {

            if(att_names[i].equals(textInput[i].getText().toString().trim()))
                su.add("");
            else

                su.add(textInput[i].getText().toString().trim());
            su2.add(att_names[i]);

        }
        //Log.d("response","Time : "+getDateCurrentTimeZone() );
        UserInfo userinfo = new UserInfo(su, su2, res, diseaseName, ts,res_all);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child(user.getUid()).child(ts + ":" + diseaseName).setValue(userinfo);


    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


}
