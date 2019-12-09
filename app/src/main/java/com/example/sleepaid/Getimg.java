package com.example.sleepaid;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Getimg extends AsyncTask<String,Void,String> {
    Handler mHandler;

    public Getimg(Handler handler){
        this.mHandler = handler;
    }


    @Override
    protected  String doInBackground(String... params){
        //Post method
        try{
            String method = (String)params[0];
            String music = (String)params[1];
            String data = URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode(method, "UTF-8");
            data += "&" + URLEncoder.encode("music", "UTF-8") + "=" + URLEncoder.encode(music, "UTF-8");

            String link = "http://10.0.2.2:8888/demo/index.php";
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            System.out.println(data);
            wr.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }

            Log.d("My Result:", sb.toString());

            return sb.toString();
        }
        catch(Exception e){
            return new String("Exception: "+e.getMessage());
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Message msg = mHandler.obtainMessage();
        msg.obj = result;
        if(result.contains("http")){
            msg.what = 1;

        }else{
            msg.what = 2;
        }
        mHandler.sendMessage(msg);
    }

    // Other functions
}
