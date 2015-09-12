package com.tripad.tetanggaku.backgroundprocess.chatting;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.tripad.tetanggaku.slidingtab.home.TabTimeline;

import org.apache.http.client.ClientProtocolException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SendChatIntentService extends IntentService {
    public static final String CHAT_STRING = "myChat";
    public static final String TARGET_STRING = "myTarget";
    public static final String RESPONSE_STRING = "myResponse";




    private static final String TAG = "SendChatIntentService";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
//    public static void startActionFoo(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, SendOBIntentService.class);
//        intent.setAction(ACTION_FOO);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
//    }
//
//    /**
//     * Starts this service to perform action Baz with the given parameters. If
//     * the service is already performing a task this action will be queued.
//     *
//     * @see IntentService
//     */
//    // TODO: Customize helper method
//    public static void startActionBaz(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, SendOBIntentService.class);
//        intent.setAction(ACTION_BAZ);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
//    }

    public SendChatIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String chatString = intent.getStringExtra(CHAT_STRING);
        String targetString = intent.getStringExtra(TARGET_STRING);

        String responseString="response is null";

        Log.i("MASUK", "masuk Intent service : "+targetString);

        Log.i("MySendChatService:", TAG);

        try {
            responseString = sendChat(chatString, targetString);
            //responseString = chatString;

        }catch (Exception e) {
            e.printStackTrace();
        }


        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(TabTimeline.MySendChatReceiver.PROCESS_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(RESPONSE_STRING, responseString);


        sendBroadcast(broadcastIntent);


    }

    private String sendChat(String... params) {
        String msg = "";

        try {
            HashMap<String, String> paramPost = new HashMap<String,String>();
            paramPost.put("l", "Openbravo");
            paramPost.put("p", "openbravo");
            paramPost.put("msg", params[0]);
            //paramPost.put("regid","dj-07G5Cy04:APA91bHNeJXcEhBNdEgnuwzSuXpQN5Nh5vhz8sBfyaNj0c66Hv3yQjtgvtLWZLh8qiAnfB1Yi5cxdu1QsNIFyxLyaC9b1wC5oXKSvUE8X3siFEw6mfCrm5JkB9v60eS-py365_ebKnF3");
            paramPost.put("regid", params[1]);
           // Log.e(TAG, "sendChat status sampe sini : "+params[0]+" token : "+params[1]);

//            msg = performPostCall("http://192.168.1.168:8585/obdev/ws/com.tripad.tetanggaku.sync.schat?l=Openbravo&p=openbravo&msg="+params[0]+"&regid="+params[1]
//                    , null);
            msg = performPostCall("http://192.168.1.168:8585/obdev/ws/com.tripad.tetanggaku.sync.schat", paramPost);
            //testposter : http://192.168.1.168:8585/obdev/ws/com.tripad.tetanggaku.sync.schat?l=Openbravo&p=openbravo&msg=test&regid=epi9atrMUAw:APA91bHV5Ir8LWZIvbIMxpyGdxNezXH-HV-aqcXrnSHaKrafvjFM3H0hX1fapagBFt90QG63Rh6_IGsKFfWmZ7acSOBEBzvs9PbyYBwSYQCwmM3eKYCiYGmUFUf3GfH7_OXnhSt872IU
        }
        catch (Exception z) {
            z.printStackTrace();
        }


        return msg;
    }

    private String  performPostCall (String requestURL,
                                    HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(6000);
            conn.setConnectTimeout(6000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostData(postDataParams));

            //Log.e(TAG, "sendChat url : " + getPostData(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="Error Kirim, Response Code : "+responseCode;

            }
        } catch (Exception e) {
            response = "Gagal Kirim Pesan, Tidak Terhubung ke Server Openbravo / Internet Server down";
        }

        Log.e(TAG, "sendChat response : " + response);

        return response;
    }

    private String getPostData(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }



    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
