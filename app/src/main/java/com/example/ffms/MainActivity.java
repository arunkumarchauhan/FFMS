package com.example.ffms;
import com.ubidots.*;
import java.io.*;
import android.os.AsyncTask;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
public class MainActivity extends AppCompatActivity {

    TextView pH;
    TextView temp;
    Button refresh;
    private String apiKey = "A1E-LWil4hkuKK3STM4NkkglAHFGlUebQM";
    private Request req;
    private String Value="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refresh = (Button) findViewById(R.id.button);
           pH=(TextView)findViewById(R.id.PhValue);
         temp=(TextView)findViewById(R.id.TemperatureValue);
        setTempandPh();


        refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  setTempandPh();
                }
            });

    }

private void setTempandPh()
{
    createHTTPConection("tempvalue");
   // temp.setText(Value);
    createHTTPConection("phvalue");
 //   pH.setText(Value);
}

    void createHTTPConection(final String varId) {
        OkHttpClient client = new OkHttpClient();
        req = new Request.Builder().addHeader("X-Auth-Token", apiKey)
                .url("https://things.ubidots.com/api/v1.6/devices/ffms/"+varId+"/values/?page_size=1")
                .build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("Chart", "Network error");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                // Here we handle the response
                String body="";

                System.out.print("\n\n\n\n\\n\n\n\n\n\n\n\n\n\n\n\n");
                if(response.isSuccessful()) {
                    body = response.body().string();

                //    Log.d("Chart", "\n\n\n\n\n\n\n\n\n\n\nn\n\n\n\n" + body);
                }

                try {


                    JSONObject jObj = new JSONObject(body);
                  //  System.out.println(jObj.get("results"));
                    JSONArray jRes = jObj.getJSONArray("results");
                    for (int i=0; i < jRes.length(); i++)
                    {
                        JSONObject obj = jRes.getJSONObject(i);
                        Log.d("JSONObject","JsOBJ"+obj.toString());
                        Value=obj.get("value").toString();
                        System.out.println(Value);

                    }
                    if(varId.equals("tempvalue"))
                    temp.setText(Value);
                    else if(varId.equals("phvalue"))
                    pH.setText(Value);


                } catch (Exception jse) {
                    jse.printStackTrace();

                    System.out.print(jse.toString());
                }
            }
        });

    }


}
