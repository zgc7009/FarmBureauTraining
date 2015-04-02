package com.smashingboxes.fb_labs_app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;


public class LoginActivity extends Activity {

    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.login_edittext);
    }

}
