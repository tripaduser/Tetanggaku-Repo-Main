package com.tripad.tetanggaku.slidingtab.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tripad.tetanggaku.R;
import com.tripad.tetanggaku.backgroundprocess.chatting.SendChatIntentService;
import com.tripad.tetanggaku.backgroundprocess.gcm.QuickstartPreferences;


/**
 * Created by mfachmirizal on 30-Aug-15.
 */
public class TabTimeline extends Fragment {
    static boolean isdebug = true;
    private Button testButton;

    private MySendChatReceiver receiver;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_timeline,container,false);

        IntentFilter filter = new IntentFilter(MySendChatReceiver.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MySendChatReceiver();
        getActivity().getApplicationContext().registerReceiver(receiver, filter);

        testButton = (Button) v.findViewById(R.id.testButton);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

                //Toast.makeText(getActivity().getApplicationContext(), sharedPreferences.getString(QuickstartPreferences.CURRENT_TOKEN, ""), Toast.LENGTH_SHORT).show();

//                sharedPreferences.edit().remove(QuickstartPreferences.SENT_TOKEN_TO_SERVER).commit();
//                sharedPreferences.edit().remove(QuickstartPreferences.CURRENT_TOKEN).commit();
//                sharedPreferences.edit().remove(QuickstartPreferences.REGISTRATION_COMPLETE).commit();
                //sharedPreferences.edit().clear().commit();
                try {
                    Intent sendChatIntent = new Intent(getActivity().getApplicationContext(), SendChatIntentService.class);

                    sendChatIntent.putExtra(SendChatIntentService.CHAT_STRING, "HELLOOOOOOOOOOOOOOO, TEST~");
                    sendChatIntent.putExtra(SendChatIntentService.TARGET_STRING, sharedPreferences.getString(QuickstartPreferences.CURRENT_TOKEN, ""));
                    getActivity().getApplicationContext().startService(sendChatIntent);
                }
                catch (Exception s) {
                    s.printStackTrace();
                }
            }
        });
        return v;
    }


    public class MySendChatReceiver extends BroadcastReceiver{

        public static final String PROCESS_RESPONSE = "com.tripad.tetanggaku.intent.action.SEND_CHAT_RESPONSE";

        @Override
        public void onReceive(Context context, Intent intent) {
            String responseString = intent.getStringExtra(SendChatIntentService.RESPONSE_STRING);

            Toast.makeText(getActivity().getApplicationContext(),"Status Kirim Chat : "+responseString,Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onDestroy() {
        this.getActivity().getApplicationContext().unregisterReceiver(receiver);
        super.onDestroy();
    }


}
