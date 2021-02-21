package com.sergeystets.vladex.activity;

import android.content.ComponentName;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sergeystets.vladex.R;
import com.sergeystets.vladex.api.VladexApi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static com.sergeystets.vladex.util.Utils.TAG;

public class SignInActivity extends AppCompatActivity {

    private final Properties properties = new Properties();

    @Inject
    VladexApi vladexApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);

        final AssetManager assetManager = this.getAssets();
        String authUrl;
        try (final InputStream inputStream = assetManager.open("application.properties")) {
            properties.load(inputStream);
            authUrl = properties.getProperty("app.vladex.auth.url");
        } catch (IOException e) {
            Log.e(TAG, "Failed to load application.properties", e);
            return;
        }

        final SignInActivity context = this;
        final Button signIn = findViewById(R.id.sign_in);
        final Uri url = Uri.parse(authUrl + "/oauth/authorize"
                + "?response_type=token"
                + "&client_id=vladex-mobile"
                + "&scope=API"
                + "&redirect_uri=app://vladex-messenger"
                + "&grant_type=implicit");

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CustomTabsServiceConnection connection = new CustomTabsServiceConnection() {
                    @Override
                    public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient client) {
                        final CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        final CustomTabsIntent intent = builder.build();
                        client.warmup(0L); // This prevents back grounding after redirection
                        intent.launchUrl(context, url);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                    }
                };
                CustomTabsClient.bindCustomTabsService(context, "com.android.chrome", connection);
            }
        });
    }
}
