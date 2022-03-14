package vib.track.cerberus.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import vib.track.cerberus.R;
import vib.track.cerberus.history.HistoryList;
import vib.track.cerberus.history.ScenarioDetail;

public class HomepageActivity extends AppCompatActivity {

    Button B_toHistory, B_toSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        B_toHistory = findViewById(R.id.buttonHistory);
        B_toSettings = findViewById(R.id.buttonSettings);
        //StartEvents();

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

    public void StartEvents(){
        Intent intent = new Intent(this, ScenarioDetail.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}