package com.sergeystets.vladex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);
        final EditText phoneNumber = findViewById(R.id.phone_number);
        Button next = findViewById(R.id.singin_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignInActivity.this, "Phone number: " + phoneNumber.getText() + " is not registered", Toast.LENGTH_LONG).show();
            }
        });

    }
}
