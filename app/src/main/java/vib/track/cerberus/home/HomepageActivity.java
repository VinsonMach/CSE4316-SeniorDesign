package vib.track.cerberus.home;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import vib.track.cerberus.R;
import vib.track.cerberus.databinding.ActivityHomepageBinding;
import vib.track.cerberus.history.HistoryList;

public class HomepageActivity extends AppCompatActivity {

    Button B_toHistory, B_toSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        B_toHistory = findViewById(R.id.buttonHistory);
        B_toSettings = findViewById(R.id.buttonSettings);

        B_toHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new HistoryList());
            }
        });

        B_toSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new SecondFragment());
            }
        });

    }
    private  void replaceFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ftran = fm.beginTransaction();
        ftran.replace(R.id.HomeFrame_FragView, fragment);
        ftran.commit();
    }

}