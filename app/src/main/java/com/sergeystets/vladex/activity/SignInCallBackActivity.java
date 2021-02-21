package com.sergeystets.vladex.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sergeystets.vladex.R;

import static com.sergeystets.vladex.util.Utils.TAG;

public class SignInCallBackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_callback_activity);
        final Intent deepLinkIntent = getIntent();
        final Uri appLinkData = deepLinkIntent.getData();
        final String dummyUrl = "http://localhost?" + (appLinkData != null ? appLinkData.getFragment() : "");
        final Uri dummyUri = Uri.parse(dummyUrl);
        final String accessToken = dummyUri.getQueryParameter("access_token");
        Log.i(TAG, "Access token: " + accessToken);
    }
}
