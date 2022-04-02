package com.example.jsonflicker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Xml;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    ListView lv1;
    TextView result;
    //ImageView image;
    ArrayList arr =new ArrayList();
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv1=findViewById(R.id.lv1);
        result=findViewById(R.id.result);
        //image=findViewById(R.id.imageView);
        MyAsyncTask asyncTask =new MyAsyncTask();
        asyncTask.execute("http://192.168.1.107:45455/api/getEtudiants");
    }
    class MyAsyncTask extends AsyncTask<String, String, String> {
        String newData="";
        Bitmap b;
        JSONObject Jobj;

        @Override
        protected void onPostExecute(String s) {

            try {
                //Picasso.get().load(Jobj.getString("m")).into(image);
                result.setText(newData);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            publishProgress("Open connection.");
            String s="";
            try {
                URL url=new URL(strings[0]);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.connect();
                publishProgress("start reading!!");
                InputStream in=new BufferedInputStream(urlConnection.getInputStream());
                newData=Stream2String(in);

                /*StringReader fis = new StringReader(newData);
                JSONObject myJson=new JSONObject(newData);
                JSONArray arrayJ=myJson.getJSONArray("items");
                JSONObject Jobj1=arrayJ.getJSONObject(2);
                Jobj=Jobj1.getJSONObject("media");*/

            }catch (Exception exp){
                publishProgress("cannot connect to server");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            result.setText(values[0]);
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPreExecute() {
            newData="";
            publishProgress("connecting attempt ongoing please wait !.");
        }
        public String Stream2String(InputStream in){
            BufferedReader buReader=new BufferedReader(new InputStreamReader(in));
            String text="",line;
            try {
                while ((line=buReader.readLine())!=null){
                    text+=line;
                }
            }catch (Exception exp){}
            return text;
        }
    }
}