/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tripad.tetanggaku.backgroundprocess.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.tripad.tetanggaku.R;

import java.io.IOException;

//import gcm.play.android.samples.com.gcmquickstart.util.GetMobileIPAddress;
//import gcm.play.android.samples.com.gcmquickstart.util.IPUtil;
//import gcm.play.android.samples.com.gcmquickstart.util.TetanggakuHttpURLConnection;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG) {
                // [START register_for_gcm]
                // Initially this call goes out to the network to retrieve the token, subsequent calls
                // are local.
                // [START get_token]
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                // [END get_token]
                Log.i(TAG, "GCM Registration Tokenn: " + token);



//                String projectId =
//                        getResources().getString(R.string.gcm_project_id);
//                GoogleCloudMessaging gcm =
//                        GoogleCloudMessaging.getInstance(this);
//                String regid = gcm.register(projectId);
//
//                Log.i(TAG, "GCM Registration regid: " + regid);

                //dEfDG_53jl4:APA91bHQsaDnVY2Nlg1-NRVYZmy2eB7HwJbx0Yye0c4LbVARfW5Cf0rB_jjf4H8aCDhBnB9TojweRfDU7Tvg0dZTA1Vuk5QKeO2RPkWZy7CAmTy8iNHXbLdRUET-onV4G3ftDrZeYw5Z
                //dEfDG_53jl4:APA91bHQsaDnVY2Nlg1-NRVYZmy2eB7HwJbx0Yye0c4LbVARfW5Cf0rB_jjf4H8aCDhBnB9TojweRfDU7Tvg0dZTA1Vuk5QKeO2RPkWZy7CAmTy8iNHXbLdRUET-onV4G3ftDrZeYw5Z
                // TODO: Implement this method to send any registration to your app's servers.
                sendRegistrationToServer(token,sharedPreferences);

                // Subscribe to topic channels
                subscribeTopics(token);

                // You should store a boolean that indicates whether the generated token has been
                // sent to your server. If the boolean is false, send the token to your server,
                // otherwise your server should have already received the token.
                sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
                sharedPreferences.edit().putString(QuickstartPreferences.CURRENT_TOKEN, token).apply();

                // [END register_for_gcm]
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
            sharedPreferences.edit().putString(QuickstartPreferences.CURRENT_TOKEN, "").apply();
            //sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_REGID_TO_SERVER, true).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

        // stopSelf(); untuk stop service
    }


    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token,SharedPreferences sharedPreferences) {
        // Add custom implementation, as needed.
//        Log.i("Test sendRegist", "Masuk Sini<1> : " + token);
//        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = new Date();
//        String formattedDate= df.format(date);
//        String ip = new GetMobileIPAddress().getMobileIP();
//        //token,nik,lastlogin,lastip,imei
//        boolean respon = sendOpenbravo(token, "192037827", formattedDate, ip, telephonyManager.getDeviceId());
//
//        sharedPreferences.edit().putBoolean(QuickstartPreferences.TOKEN_EXISTS_IN_SERVER, respon).apply();
    }
/*
    private boolean sendOpenbravo(String... params) {
            String msg = "";
            String serverurl = new IPUtil(getApplicationContext()).getIp();
        //if (serverurl == null) serverurl = "192.168.1.168:8585";
            boolean isexists =false;
            try {
                HashMap<String, String> paramPost = new HashMap<String,String>();
//                paramPost.put("msg",isiPesan.getText().toString());
                String xml = "<ob:Openbravo xmlns:ob=\"http://www.openbravo.com\">" +
                        "<Tmob_Userdata>" +
                        "<active>true</active>" +
                        "<token>"+params[0]+"</token>" +
                        "<nik>"+params[1]+"</nik>" +
                        //"<note>User terkoneksi pertama kali</note>" +
                        "<lastlogin>"+params[2]+"</lastlogin>" +
                        "<lastip>"+params[3]+"</lastip>" +
                        "<imei>"+params[4]+"</imei>" +
                        "</Tmob_Userdata> " +
                        "</ob:Openbravo>";

                //paramPost.put("POST",xml);
              //  paramPost.put("l","Openbravo");
              //  paramPost.put("p","openbravo");
                //msg = new TetanggakuHttpURLConnection().performPostXML("http://192.168.1.2:8585/openbravotripad/ws/com.tripad.tetanggaku.sync.udata?p=openbravo&l=Openbravo&cl=23C59575B9CF467C9620760EB255B389&or=0",xml,"POST");
               // boolean isexists =new TetanggakuHttpURLConnection().isTokenExists("http://192.168.1.2:8585/openbravotripad/ws/dal/Tmob_Userdata?p=openbravo&l=Openbravo", params[0]);
               // if (isexists) {
                    //nanti update last login
               //     msg = "token ini :"+params[0]+" "+isexists;
               // }
               // else {
                    //msg = new TetanggakuHttpURLConnection().performPostXML("http://192.168.1.168:8585/obdev/ws/com.tripad.tetanggaku.sync.udata?p=openbravo&l=Openbravo&cl=4028E6C72959682B01295A070852010D&or=0",xml,"POST");
                msg = new TetanggakuHttpURLConnection().performPostXML("http://"+serverurl+"/ws/com.tripad.tetanggaku.sync.udata?p=openbravo&l=Openbravo&cl=4028E6C72959682B01295A070852010D&or=0",xml,"POST");

                    //isexists =new TetanggakuHttpURLConnection().isTokenExists("http://192.168.1.168:8585/obdev/ws/dal/Tmob_Userdata?p=openbravo&l=Openbravo", params[0]);
                isexists =new TetanggakuHttpURLConnection().isTokenExists("http://"+serverurl+"/ws/dal/Tmob_Userdata?p=openbravo&l=Openbravo", params[0]);
                //}
                Log.i("Test POST", "PESAN POST1 : " + msg+" : "+isexists);
                //msg = performPostCall("http://tripad.s20.eatj.com/TetanggakuServerAPI/SendMessage", paramPost);
            }
            catch (Exception z) {
                z.printStackTrace();
            }


            return isexists;
    }
    */
/*
    private class SendOpenbravo extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String msg = "";
            try {
                HashMap<String, String> paramPost = new HashMap<String,String>();
//                paramPost.put("msg",isiPesan.getText().toString());
                String xml = "<ob:Openbravo xmlns:ob=\"http://www.openbravo.com\">" +
                        "<Tmob_Userdata>" +
                        "<active>true</active>" +
                        "<token>"+params[0]+"</token>" +
                        "<nik>"+params[1]+"</nik>" +
                        "<note>first insert</note>" +
                        "<lastlogin>"+params[2]+"</lastlogin>" +
                        "<lastip>"+params[3]+"</lastip>" +
                        "<imei>"+params[4]+"</imei>" +
                        "</Tmob_Userdata> " +
                        "</ob:Openbravo>";

                //paramPost.put("POST",xml);
                paramPost.put("l","Openbravo");
                paramPost.put("p","openbravo");
                //msg = new TetanggakuHttpURLConnection().performPostXML("http://192.168.1.2:8585/openbravotripad/ws/com.tripad.tetanggaku.sync.udata?p=openbravo&l=Openbravo&cl=23C59575B9CF467C9620760EB255B389&or=0",xml,"POST");
                boolean isexists =new TetanggakuHttpURLConnection().isTokenExists("http://192.168.1.1:8585/openbravotripad/ws/dal/Tmob_Userdata?p=openbravo&l=Openbravo", params[0]);
                //if (isexists) {
                    //nanti update last login
                    msg = "token ini :"+params[0]+" "+isexists;
                //}
                //else {
                //    msg = new TetanggakuHttpURLConnection().performPostXML("http://192.168.1.1:8585/openbravotripad/ws/com.tripad.tetanggaku.sync.udata?p=openbravo&l=Openbravo&cl=23C59575B9CF467C9620760EB255B389&or=0",xml,"POST");
                //}
                Log.i("Test POST", "PESAN POST1 : " + msg);
                //msg = performPostCall("http://tripad.s20.eatj.com/TetanggakuServerAPI/SendMessage", paramPost);
            }
            catch (Exception z) {
                z.printStackTrace();
            }


            return msg;
        }


        @Override
        protected void onPostExecute(String msg) {
            //mDisplay.append(msg + "\n");
        }
    }*/

//    public String  performPostCall (String requestURL,
//                                    HashMap<String, String> postDataParams) {
//
//        URL url;
//        String response = "";
//        try {
//            url = new URL(requestURL);
//
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(15000);
//            conn.setConnectTimeout(15000);
//            conn.setRequestProperty("Content-Type", "text/xml"); //tambah ini
//            conn.setAllowUserInteraction(false);
//            conn.setDefaultUseCaches(false);
//            conn.setInstanceFollowRedirects(true);
//            conn.setUseCaches(false);
//            conn.setRequestMethod("POST");
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//
//
//            Log.i("Test POST Data", "ISI : " + getPostData(postDataParams));
//            OutputStream os = conn.getOutputStream();
//
//            BufferedWriter writer = new BufferedWriter(
//                    new OutputStreamWriter(os, "UTF-8"));
//            writer.write(getPostData(postDataParams));
//
//            writer.flush();
//            writer.close();
//            os.close();
//            int responseCode=conn.getResponseCode();
//            Log.i("Test POST ResponseCode", "ResponseCode: " + responseCode);
//
//            //if (responseCode == HttpsURLConnection.HTTP_OK) {
//                String line;
//                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                while ((line=br.readLine()) != null) {
//                    response+=line;
//                }
//            /*}
//            else {
//                response="";
//
//            }*/
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return response;
//    }
//
//    public String  performPostCall2 (String requestURL,
//                                    String postDataParams /*HashMap<String, String> postDataParams*/ ) {
//
//        URL url;
//        HttpURLConnection conn = null;
//        String response = "";
//        try {
//            url = new URL(requestURL);
//            conn = (HttpURLConnection) url.openConnection();
//
//            conn.setReadTimeout(15000);
//            conn.setConnectTimeout(15000);
//            conn.setRequestProperty("Content-Type", "text/xml"); //tambah ini
//            conn.setAllowUserInteraction(false);
//            conn.setDefaultUseCaches(false);
//            conn.setInstanceFollowRedirects(true);
//            conn.setUseCaches(false);
//            conn.setRequestMethod("POST");
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//
//
//            Log.i("Test POST Data", "ISI : " + postDataParams);
//            OutputStream os = conn.getOutputStream();
//            if (postDataParams != null) {
//                BufferedWriter writer = new BufferedWriter(
//                        new OutputStreamWriter(os, "UTF-8"));
//
//                writer.write(postDataParams);
//                writer.flush();
//                writer.close();
//            }
//
//            /*DataOutputStream out = new DataOutputStream(
//                    conn.getOutputStream());
//            out.writeBytes(postDataParams);
//            out.flush();
//            out.close();*/
//            //OutputStream os = conn.getOutputStream();
////            BufferedWriter writer = new BufferedWriter(
////                    new OutputStreamWriter(os, "UTF-8"));
////            writer.write(postDataParams);
////
////            writer.flush();
////            writer.close();
////            os.close();
//            int responseCode=conn.getResponseCode();
//            Log.i("Test POST ResponseCode", "ResponseCode: " + responseCode);
//            if (responseCode == HttpsURLConnection.HTTP_OK) {
//                String line;
//                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                while ((line=br.readLine()) != null) {
//                    response+=line;
//                }
//            }
//            else {
//                //response="";
//                String line;
//                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//                while ((line=br.readLine()) != null) {
//                    response += line;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally {
//            try {
//                if (conn.getInputStream() != null) conn.getInputStream().close();
//                if (conn.getOutputStream() != null) conn.getOutputStream().close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return response;
//    }
//
//    private String getPostData(HashMap<String, String> params) throws UnsupportedEncodingException {
//        StringBuilder result = new StringBuilder();
//        boolean first = true;
//        for(Map.Entry<String, String> entry : params.entrySet()){
//            if (first)
//                first = false;
//            else
//                result.append("&");
//
//            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
//            result.append("=");
//            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
//        }
//
//        return result.toString();
//    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        for (String topic : TOPICS) {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]

}
