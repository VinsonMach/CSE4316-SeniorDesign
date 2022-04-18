package vib.track.cerberus.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


import vib.track.cerberus.R;
import vib.track.cerberus.home.HomepageActivity;
import vib.track.cerberus.home.credits;
import vib.track.cerberus.history.HistoryList;

public class SecondFragment extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Switch switch1;
    DrawerLayout drawerLayout;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCH1 = "swtich1";

    private Button saveButton;
    private boolean switchOnOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_second);

        // assign variables
        drawerLayout = findViewById(R.id.drawer_layout);

        Spinner mySpinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> myAdapter = ArrayAdapter.createFromResource(this,
                R.array.vibrations, android.R.layout.simple_spinner_item);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        mySpinner.setOnItemSelectedListener(this);

        saveButton = (Button) findViewById(R.id.save_button);
        switch1 = (Switch) findViewById(R.id.switch1);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        loadData();
        updateViews();

    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SWITCH1, switch1.isChecked());
        editor.apply();

        Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        switchOnOff = sharedPreferences.getBoolean(SWITCH1, false);
    }

    public void updateViews() {
        switch1.setChecked(switchOnOff);
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
        recreate();
    }

    public void clickCredits(View view){
        // redirect to credits
        HomepageActivity.redirectActivity(this, credits.class);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
