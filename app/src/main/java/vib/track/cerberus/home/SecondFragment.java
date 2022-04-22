package vib.track.cerberus.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import vib.track.cerberus.R;
import vib.track.cerberus.history.HistoryActivity;
import vib.track.cerberus.home.HomepageActivity;
import vib.track.cerberus.home.credits;
import vib.track.cerberus.history.HistoryList;

public class SecondFragment extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Switch switch1;
    private TextView textViewInterval;
    private TextView textViewCycle;
    private EditText editTextInterval;
    private EditText editTextCycle;
    DrawerLayout drawerLayout;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXTINTERVAL = "textInterval";
    public static final String TEXTCYCLE = "textCycle";
    public static final String SWITCH1 = "swtich1";

    private Button applySettingsButton;
    private Button saveButton;
    private String textInterval;
    private String textCycle;
    private boolean switchOnOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_second);

        // assign variables
        drawerLayout = findViewById(R.id.drawer_layout);

        textViewInterval = (TextView) findViewById(R.id.textViewInterval);
        textViewCycle = (TextView) findViewById(R.id.textViewCycle);

        editTextInterval = (EditText) findViewById(R.id.editTextInterval);
        editTextCycle = (EditText) findViewById(R.id.editTextCycle);

        applySettingsButton = (Button) findViewById(R.id.apply_button);
        saveButton = (Button) findViewById(R.id.save_button);
        switch1 = (Switch) findViewById(R.id.switch1);

        applySettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewInterval.setText(editTextInterval.getText().toString());
                textViewCycle.setText(editTextCycle.getText().toString());
            }
        });

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

        editor.putString(TEXTINTERVAL, textViewInterval.getText().toString());
        editor.putString(TEXTCYCLE, textViewCycle.getText().toString());
        editor.putBoolean(SWITCH1, switch1.isChecked());
        editor.apply();

        Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        textInterval = sharedPreferences.getString(TEXTINTERVAL, "");
        textCycle = sharedPreferences.getString(TEXTCYCLE, "");
        switchOnOff = sharedPreferences.getBoolean(SWITCH1, false);
    }

    public void updateViews() {
        textViewInterval.setText(textInterval);
        textViewCycle.setText(textCycle);
        switch1.setChecked(switchOnOff);
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
        // redirect to history
        HomepageActivity.redirectActivity(this, HistoryActivity.class);
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
