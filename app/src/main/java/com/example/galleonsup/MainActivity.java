package com.example.galleonsup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.galleonsup.databinding.NavHeaderMainBinding;
import com.example.galleonsup.model.User;
import com.example.galleonsup.utils.StaticTags;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST = 0;
    private AppBarConfiguration mAppBarConfiguration;
    private int PERMISSION_ALL = 1;
    private static final String[] PERMISSIONS_LIST = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET
    };
    boolean doubleBackToExitPressedOnce = false;
    User user;
    NavController navController;

    @SuppressLint({"RestrictedApi", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_profile,R.id.nav_attendance, R.id.nav_evaluation, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        user = User.getInstance();
        if(user.getUserId()==null)
        {
            user.setValuesFromSharedPreference(getSharedPreferences(StaticTags.USER_PREFERENCE,MODE_PRIVATE));
        }

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.text_name);
        TextView navId = headerView.findViewById(R.id.id_and_area);
        navUsername.setText(user.getName());
        navId.setText("ID: "+user.getUserId()+","+user.getArea());
        
/*
        // for changing menuitem text and color
        Menu menu = navigationView.getMenu();
        MenuItem nav_camara = menu.findItem(R.id.nav_attendance);
        SpannableString s = new SpannableString("My red MenuItem");
        s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
        nav_camara.setTitle(s);
 */



        checkPermission();
        //GPS_Start();
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    private void checkPermission() {
        if (!hasPermissions(this, PERMISSIONS_LIST)) {
            Log.e("per","error perm");
            ActivityCompat.requestPermissions(this, PERMISSIONS_LIST, PERMISSION_ALL);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() < 1 && navController.getCurrentDestination().getId() == R.id.nav_profile) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
        else {
            super.onBackPressed();
        }

    }
}