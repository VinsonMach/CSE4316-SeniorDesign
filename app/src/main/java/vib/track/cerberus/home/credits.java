package vib.track.cerberus.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import vib.track.cerberus.R;
import vib.track.cerberus.history.HistoryList;
import vib.track.cerberus.home.HomepageActivity;
import vib.track.cerberus.home.SecondFragment;

public class credits extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_credits);

        // assign variables
        drawerLayout = findViewById(R.id.drawer_layout2);
    }

    public void clickMenu(View view){
        // open drawer
        HomepageActivity.openDrawer(drawerLayout);
    }

    public void clickLogo(View view){
        // close drawer
        HomepageActivity.closeDrawer(drawerLayout);
    }

    public void clickHistory(View view){
        // redirect to history
        HomepageActivity.redirectActivity(this, HistoryList.class);
    }

    public void clickSettings(View view){
        // recreate settings activity
        HomepageActivity.redirectActivity(this, SecondFragment.class);
    }

    public void clickCredits(View view){
        // redirect to credits
        recreate();
    }

    public void clickLogout(View view){
        // redirect to history
        HomepageActivity.logout(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        // close drawer
        HomepageActivity.closeDrawer(drawerLayout);
    }
}
