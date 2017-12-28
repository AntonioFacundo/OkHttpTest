package iplace.net.okhttptest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    String response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.btnPost);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               new AsyncPost().execute();
            }
        });
    }

    class AsyncPost extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            final String url = "http://hmkcode.appspot.com/jsonservlet";
            Cliente cliente = new Cliente();
            cliente.setName("Juan");
            cliente.setCountry("Peru");
            cliente.setTwitter("asd@asd");

            try {
                response = post(url, convertToJson(cliente));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView res = findViewById(R.id.tvResponce);
            res.setText(response);
        }
    }


    String convertToJson(Object obj){
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        return json;
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String jsonResponce = response.body().string();
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<Cliente>>(){}.getType();
        Collection<Cliente> respuesta = gson.fromJson(jsonResponce, collectionType);

        Iterator<Cliente> iterator = respuesta.iterator();
        List<Cliente> list = new ArrayList<>();
        while(iterator.hasNext()){
             list.add(iterator.next());
        }

        return list.get(0).getName();

    }
}
