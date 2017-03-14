package com.akansh.myapplication;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.Manifest;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

//import static com.akansh.myapplication.R.id.imageView;
//import static com.akansh.myapplication.R.id.imageuser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewSwitcher.ViewFactory {

    public boolean isHome = true;
    public boolean isPredictionFragment = false;
    public ProgressBar progressBar;
    public FirebaseAuth.AuthStateListener authListener;
    public FirebaseAuth auth;
    public TextView inputEmail1, nameuser;
    DrawerArrowDrawable drawerArrow;// = new DrawerArrowDrawable(this);
    public ImageView imageuser;
    public String email;
    public String personPhotoUrl;
    public ActionBarDrawerToggle toggle;
    public String userName;
    public boolean isDietHome = false;
    public boolean isDietChart = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    //RequestQueue queue;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitleColor(Color.DKGRAY);
        drawerArrow = new DrawerArrowDrawable(this);
        drawerArrow.setColor(Color.BLACK);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("I", "Here Id :");
            }
        });
        toolbar.setNavigationIcon(drawerArrow);
        //get firebase auth instance
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

                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }

            }
        };

        if (user != null) {
        initCollapsingToolbar();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerArrowDrawable(drawerArrow);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        isHome = true;
        displaySelectedScreen(R.id.nav_home);
        isHome = true;

        View header = navigationView.getHeaderView(0);
        imageuser = (ImageView) header.findViewById(R.id.imageuser);
        inputEmail1 = (TextView) header.findViewById(R.id.emailuser);
        nameuser = (TextView) header.findViewById(R.id.nameuser);


            email = user.getEmail().toString();
            //userName = user.getDisplayName();
            inputEmail1.setText(email);
            //nameuser.setText(userName);
            personPhotoUrl = user.getPhotoUrl().toString();
            if (personPhotoUrl == null) {
                personPhotoUrl = Uri.parse("android.resource://com.akansh.myapplication/" + R.drawable.photo).toString();
            }

            Glide.with(MainActivity.this).load(personPhotoUrl)
                    .asBitmap()
                    //.thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)

                    .into(new BitmapImageViewTarget(imageuser) {
                              @Override
                              protected void setResource(Bitmap resource) {
                                  RoundedBitmapDrawable circularBitmapDrawable =
                                          RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), resource);
                                  circularBitmapDrawable.setCircular(true);
                                  imageuser.setImageDrawable(circularBitmapDrawable);
                              }
                          }
                    );

            //.into(imageuser);
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);

            List<String> permissions = new ArrayList<String>();

            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
                permissions.add(Manifest.permission.WAKE_LOCK);

            }
            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 1);

            }
        }

        auth.addAuthStateListener(authListener);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onBackPressed() {


        //Toast.makeText(MainActivity.this,(isHome ? ("true") : "false"),Toast.LENGTH_LONG).show();
        Log.i("I", "Home : " + isHome);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (isPredictionFragment == true) {
            enablePredictionHome(false);
            toggle.setDrawerIndicatorEnabled(true);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_predict);
            displaySelectedScreen(R.id.nav_predict);

        } else if (isDietChart) {
            isDietChart = false;
            isDietHome = true;
            Fragment frag = new MainDiet();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, frag);
            ft.commit();
        } else if (isDietHome == true) {
            enableDietHome(false);
            toggle.setDrawerIndicatorEnabled(true);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_home);
            displaySelectedScreen(R.id.nav_home);

            isHome = true;
        } else if (isHome == false) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_home);
            displaySelectedScreen(R.id.nav_home);

            isHome = true;
        } else {
            super.onBackPressed();
        }
        //toggle.setDrawerIndicatorEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*@Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        int itemId = item.getItemId();
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.i("I", "Item : " + id);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            auth.signOut();
            return true;
        }
        if (id == android.R.id.home) {
            this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //Toast.makeText(MainActivity.this,"YOUR MESSAGE",Toast.LENGTH_LONG).show();
        int id = item.getItemId();
        if (isPredictionFragment == true) {
            enablePredictionHome(false);
            toggle.setDrawerIndicatorEnabled(true);


        }

        if (id == R.id.nav_home) {
            isHome = true;
            //Toast.makeText(MainActivity.this,"Home Selected",Toast.LENGTH_LONG).show();
        } else {
            isHome = false;
            //Toast.makeText(MainActivity.this,"Home Not Selected",Toast.LENGTH_LONG).show();
        }
        displaySelectedScreen(id);
        return true;
    }

    private void displaySelectedScreen(int itemId) {
        Fragment frag = null;

        if (itemId == R.id.nav_home) {
            //Toast.makeText(MainActivity.this,"YOUR MESSAGE",Toast.LENGTH_LONG).show();
            frag = new fragment_home();

        } else if (itemId == R.id.nav_history) {
            //Toast.makeText(MainActivity.this,"YOUR MESSAGE",Toast.LENGTH_LONG).show();
            frag = new CardViewActivity();

        } else if (itemId == R.id.nav_changedetails) {
            //Toast.makeText(MainActivity.this,"YOUR MESSAGE",Toast.LENGTH_LONG).show();
            frag = new fragment_log_out();

        } else if (itemId == R.id.nav_logout) {
            //Toast.makeText(MainActivity.this,"YOUR MESSAGE",Toast.LENGTH_LONG).show();
            auth.signOut();

        } /*else if (itemId == R.id.nav_monitor) {

            Intent i =new Intent(getApplication(),fragment_monitor.class);
            startActivity(i);
            //frag = new fragment_monitor();
        }*/ else if (itemId == R.id.nav_predict) {

            frag = new fragment_predict();
        }

        if (frag != null) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, frag);
            ft.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void enablePredictionHome(Boolean v) {

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ObjectAnimator.ofFloat(drawerArrow, "progress", v ? 1 : 0).start();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        isPredictionFragment = v;
        if (v) {
            toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("I", "Back Pressed :");
                    onBackPressed();
                }
            });
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.syncState();

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("I", "Back Pressed :");
                    onBackPressed();
                }
            });
        } else {

            toggle.setToolbarNavigationClickListener(null);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toggle.syncState();
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("I", "Drawer Toggle :");
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else
                        drawer.openDrawer(GravityCompat.START);
                }
            });

        }


    }

    public void enableDietHome(Boolean v) {

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ObjectAnimator.ofFloat(drawerArrow, "progress", v ? 1 : 0).start();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        isHome = !v;
        isDietHome = true;
        if (v) {
            toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("I", "Back Pressed :");
                    onBackPressed();
                }
            });
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.syncState();

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("I", "Back Pressed :");
                    onBackPressed();
                }
            });
        } else {

            toggle.setToolbarNavigationClickListener(null);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toggle.syncState();
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("I", "Drawer Toggle :");
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else
                        drawer.openDrawer(GravityCompat.START);
                }
            });

        }


    }


    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getString(R.string.app_name));
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);
        ImageSwitcher mSwitcher = (ImageSwitcher) findViewById(R.id.backdrop);
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
