package vib.track.cerberus.data;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import vib.track.cerberus.HomepageActivity;
import vib.track.cerberus.R;
import vib.track.cerberus.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerButton = this.findViewById(R.id.rp_regBtn);

        //new register button
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goTo_LoginActivity();// function declaration on line 152
            }
        });

    }

    public void goTo_LoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}