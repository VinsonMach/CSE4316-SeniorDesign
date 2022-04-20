package vib.track.cerberus.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import vib.track.cerberus.R;
import vib.track.cerberus.history.HistoryActivity;

public class HomepageActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // assign variables
        drawerLayout = findViewById(R.id.drawer_layout);

    }

    public void clickMenu(View view){
        // open drawer
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawer) {
        // open drawer layout
        drawer.openDrawer(GravityCompat.START);
    }

    public void clickLogo(View view){
        // close drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawer) {
        // close drawer layout
        // check condition
        if (drawer.isDrawerOpen(GravityCompat.START)){
            // close drawer when drawer is open
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public void clickHistory(View view){
        // recreate history activity
        redirectActivity(this, HistoryActivity.class);
    }

    public void clickSettings(View view){
        // redirect activity to Settings
        redirectActivity(this,SecondFragment.class);
    }

    public void clickCredits(View view){
        // close app
        redirectActivity(this,credits.class);
    }

    public void clickLogout(View view){
        // close app
        logout(this);
    }

    public static void logout(Activity activity) {
        // initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // set title
        builder.setTitle("Logout");
        // set message
        builder.setMessage("Are you sure that you want to logout?");
        // positive yes button
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // finish activity
                activity.finishAffinity();
                // exit app
                System.exit(0);
            }
        });
        // negative no button
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss dialog
                dialogInterface.dismiss();
            }
        });

        // show dialog
        builder.show();
    }

    public static void redirectActivity(Activity activity,Class aClass) {
        // initialize intent
        Intent intent = new Intent(activity, aClass);
        // set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // start activity
        activity.startActivity(intent);
    }

    @Override
    protected void onPause(){
        super.onPause();
        // close drawer
        closeDrawer(drawerLayout);
    }

}
