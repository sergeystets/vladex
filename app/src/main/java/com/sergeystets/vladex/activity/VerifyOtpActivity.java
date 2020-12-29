package com.sergeystets.vladex.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.sergeystets.vladex.R;

import java.util.Timer;
import java.util.TimerTask;

import static com.sergeystets.vladex.util.Utils.TAG;

public class VerifyOtpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_otp_activity);

        final Intent intent = getIntent();
        final TextView otpWasSentView = findViewById(R.id.otp_was_sent_note);
        final TextView expirationView = findViewById(R.id.otp_will_expire_note);

        final String phone = intent.getStringExtra(SignInActivity.EXTRA_PHONE);
        otpWasSentView.setText(getString(R.string.otp_was_sent_note, phone));

        long expirationSeconds = intent.getLongExtra(SignInActivity.EXTRA_OTP_EXPIRATION, 0L);
        Log.i(TAG, "Will start countdown for OTP starting from : " + expirationSeconds);
        final long[] expiration = {expirationSeconds};
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (expiration[0] >= 0) {
                            String expirationMessage = getString(R.string.it_will_expire_in_note, expiration[0]--);
                            expirationView.setText(expirationMessage);
                        }
                        if (expiration[0] < 0) {
                            timer.cancel();
                        }
                    }
                });
            }
        }, 1000, 1000);
    }
}