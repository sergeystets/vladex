package com.sergeystets.vladex.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sergeystets.vladex.R;
import com.sergeystets.vladex.api.VladexApi;
import com.sergeystets.vladex.model.VerifyPhoneResponse;

import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Properties;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static com.sergeystets.vladex.util.Utils.TAG;

public class SignInActivity extends AppCompatActivity {

    public static final String EXTRA_PHONE = " com.sergeystets.vladex.PHONE";
    public static final String EXTRA_OTP_EXPIRATION = " com.sergeystets.vladex.OTP_EXPIRATION";

    private final Properties properties = new Properties();
    private String uri;

    @Inject
    VladexApi vladexApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);

        final AssetManager assetManager = this.getAssets();
        try (final InputStream inputStream = assetManager.open("application.properties")) {
            properties.load(inputStream);
            uri = properties.getProperty("app.vladex.api.url");
        } catch (IOException e) {
            Log.e(TAG, "Failed to load application.properties", e);
            return;
        }

        final EditText phoneNumber = findViewById(R.id.phone_number);
        final Button next = findViewById(R.id.singin_next);
        final SignInActivity activity = this;

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), VerifyOtpActivity.class);
                new VerifyPhoneTask(vladexApi, uri, phoneNumber.getText().toString(), activity, intent).execute();
            }
        });
    }

    private static class VerifyPhoneTask extends AsyncTask<Void, Void, VerifyPhoneResponse> {

        private final String serverUrl;
        private final String phoneNumber;
        private final Intent intent;
        private final WeakReference<SignInActivity> activity;
        private final VladexApi vladexApi;
        private HttpStatusCodeException e;

        public VerifyPhoneTask(VladexApi vladexApi, String serverUrl, String phoneNumber, SignInActivity activity, Intent intent) {
            this.vladexApi = vladexApi;
            this.serverUrl = serverUrl;
            this.phoneNumber = phoneNumber;
            this.activity = new WeakReference<>(activity);
            this.intent = intent;
        }

        protected VerifyPhoneResponse doInBackground(Void... arg0) {
            return verifyPhone(phoneNumber);
        }

        private VerifyPhoneResponse verifyPhone(final String phoneNumber) {
            Log.i(TAG, "Verifying phone: " + phoneNumber);
            try {
                final VerifyPhoneResponse response = vladexApi.verifyPhone(serverUrl, phoneNumber);
                Log.i(TAG, "Verify phone response: " + response);
                return response;
            } catch (HttpStatusCodeException e) {
                this.e = e;
                return null;
            }
        }

        protected void onPostExecute(VerifyPhoneResponse response) {
            if (e != null) {
                Toast.makeText(activity.get(), "Error:" + e.getResponseBodyAsString(), Toast.LENGTH_LONG).show();
            } else {
                intent.putExtra(EXTRA_PHONE, phoneNumber);
                intent.putExtra(EXTRA_OTP_EXPIRATION, response.getOtpExpirationSeconds());
                activity.get().startActivity(intent);
            }
        }
    }
}
