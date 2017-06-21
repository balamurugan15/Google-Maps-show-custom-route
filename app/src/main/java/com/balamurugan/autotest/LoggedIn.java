package com.balamurugan.autotest;


//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class LoggedIn extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{


    private ImageView profileImage;
    private Context mContext = this;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    protected SwipeRefreshLayout swipeRefreshLayout;
    private AppBarLayout appBarLayout;

    boolean doubleBackToExitPressedOnce = false;
    public int selectedTemp;

    protected NavigationView navigationView;
    protected DrawerLayout drawerLayout;
    private Toolbar toolbar;
    public Context context = this;
    private CoordinatorLayout coordinatorLayout;
    private Button logButton;

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    private int mMaxScrollSize;



    boolean noInternet = false;
    boolean expired = false;

    String title;
    ProgressBar pBar;


    TextView tv;


    class profile{
        public String sname;
        public String branch;
        public String insti;
        public String sem;
        public String regno;
        public String dob;
        public String fname;
        public String sex;

        public  profile(){
            sname = branch = insti = sem = regno = dob = fname = sex = null; }
    }

    profile stud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_logged_in);



        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_coordinator1);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout1);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar1);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        drawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout1);

        Button mapbtn =(Button) findViewById(R.id.buttonMaps);

        logButton =(Button) findViewById(R.id.buttonDone);


        /*setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayShowTitleEnabled(false); */

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }



        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator1);




        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Logged out successfully!",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoggedIn.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        mapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoggedIn.this, MapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        profileImage = (ImageView) findViewById(R.id.stu_avatar);

        Bitmap photo = BitmapFactory.decodeResource(this.getResources(), R.drawable.stu_image);
        profileImage.setImageBitmap(photo);

        appBarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appBarLayout.getTotalScrollRange();

       /* TextView title = (TextView) findViewById(R.id.stu_name);
        TextView subtitle = (TextView) findViewById(R.id.school_name);
        title.setText("Hello!");
        subtitle.setText(" "); */

        tv = (TextView) findViewById(R.id.stu_name);
        tv.setText("Rahul");
        tv = (TextView) findViewById(R.id.school_name);
        tv.setText("Honda City SV");


        stud = new profile();

      //  setTitle("Profile");

     //   pBar = (ProgressBar) findViewById(R.id.logged_progress);
      //  tv = (TextView)findViewById(R.id.infotext);
   /*     swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        OnRefresh();
                                    }
                                }
        ); */

       // new InflateProfile().execute();

      //  if(noInternet){
      //      box.showInternetOffLayout();
      //  }






    }








    void fillTextview(){




    }




    @Override
    protected void onPause() {
        super.onPause();
    }

    void fillstud(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("profile", 0);
        stud.sname = pref.getString("sname", null);
        stud.regno = pref.getString("regno", null);
        stud.branch = pref.getString("branch", null);
        stud.sem = pref.getString("sem", null);
        stud.sex = pref.getString("sex", null);
        stud.fname = pref.getString("fname", null);
        stud.dob = pref.getString("dob", null);
        stud.insti = pref.getString("insti", null);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;
            profileImage.animate().scaleY(0).scaleX(0).setDuration(200).start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            profileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }




    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent intent = new Intent(LoggedIn.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY  | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        //  Toast.makeText(this, "Press BACK again to go to home page", Toast.LENGTH_SHORT).show();
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Press BACK again to go home.", Snackbar.LENGTH_LONG);
        snackbar.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
