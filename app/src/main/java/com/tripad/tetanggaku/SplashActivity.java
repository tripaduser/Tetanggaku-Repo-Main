package com.tripad.tetanggaku;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.tripad.tetanggaku.backgroundprocess.gcm.QuickstartPreferences;
import com.tripad.tetanggaku.backgroundprocess.gcm.RegistrationIntentService;

public class SplashActivity extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "SplashActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    TextView tv_status;
    ProgressBar pb_checkingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tv_status = (TextView) findViewById(R.id.tv_status);
        pb_checkingProgressBar =(ProgressBar) findViewById(R.id.pb_checkingProgressBar);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                pb_checkingProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
//                boolean sentTokenInServer = sharedPreferences
//                        .getBoolean(QuickstartPreferences.TOKEN_EXISTS_IN_SERVER, false);
                //setComponentVisibility(false);
                if (sentToken) {
                    tv_status.setText(getString(R.string.gcm_send_message));
                    Toast.makeText(context,sharedPreferences.getString(QuickstartPreferences.CURRENT_TOKEN,""),Toast.LENGTH_LONG).show();
                    Intent intentHomePage = new Intent(context, TetanggakuActivity.class);
                    startActivity(intentHomePage);
                    finish();
//                    setComponentVisibility(true);
//                    multiEditTextToken.setText(sharedPreferences.getString(QuickstartPreferences.CURRENT_TOKEN,""));
                } else {
                    tv_status.setText(getString(R.string.token_error_message));
                    Toast.makeText(context,sharedPreferences.getString(QuickstartPreferences.CURRENT_TOKEN,""),Toast.LENGTH_LONG).show();
//                    setComponentVisibility(false);
//                    multiEditTextToken.setText("Token Error Retrieved or Error Connecting to Openbravo Server ");
                }
            }
        };

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
