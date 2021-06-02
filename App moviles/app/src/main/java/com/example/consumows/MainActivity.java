package com.example.consumows;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    TextView Tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<revistas> ListRevistas = new ArrayList<revistas>();
        String Url = "https://revistas.uteq.edu.ec/ws/issues.php?j_id=";
        Button btn = findViewById(R.id.boton);
        Button btn2 = findViewById(R.id.boton2);
        EditText TvId = findViewById(R.id.txtJ_id);
        Tv = findViewById(R.id.Resultado);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = TvId.getText().toString();
                VolleyRevistas(Url, id);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = TvId.getText().toString();
                RetroRevistas(id);
            }
        });
    }

    private void VolleyRevistas(String Url, String id) {

        StringRequest llamado = new StringRequest(Url + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        JSONArray ArrayJson = new JSONArray(response);
                        Tv.setText(ArrayJson.toString(4));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue ejecVolley = Volley.newRequestQueue(this);
        ejecVolley.add(llamado);
    }

    public void RetroRevistas(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://revistas.uteq.edu.ec/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        Irevistas revist = retrofit.create(Irevistas.class);
        Call<List<revistas>> call = revist.RetroRevistas(id);
        call.enqueue(new Callback<List<revistas>>() {
            @Override
            public void onResponse(Call<List<revistas>> call, retrofit2.Response<List<revistas>> response) {
                if (!response.isSuccessful()) {
                    Tv.setText("Codigo: " + response.code());
                    return;
                }
                List<revistas> ListaRevistas = response.body();
                JSONArray jsonArray = new JSONArray();
                for (revistas revista: ListaRevistas){
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("issue_id",revista.getIssue_id());
                        obj.put("volume",revista.getVolume());
                        obj.put("number",revista.getNumber());
                        obj.put("year",revista.getYear());
                        obj.put("date_published",revista.getDate_published());
                        obj.put("title",revista.getTitle());
                        obj.put("doi",revista.getDoi());
                        obj.put("cover",revista.getCover());
                        jsonArray.put(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Tv.setText(jsonArray.toString(4));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<revistas>> call, Throwable t) {
                Tv.setText(t.getMessage());
            }
        });
    }
}