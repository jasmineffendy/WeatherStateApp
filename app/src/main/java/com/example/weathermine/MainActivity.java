package com.example.weathermine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.example.weathermine.Retrofit.ApiService;
import com.example.weathermine.databinding.ActivityMainBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class MainActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    private ActivityMainBinding binding;
    private RequestQueue requestQueue;

    private ArrayList<WeatherModel> arrayListWeather = new ArrayList<>();
    private WeatherAdapter weatherAdapter;

    private static final String JSON_URL = "https://api.open-meteo.com/v1/forecast?latitude=-7.98&longitude=112.63&daily=weathercode&current_weather=true&timezone=auto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.requestQueue = Volley.newRequestQueue(this);

        binding.btnVolley.setOnClickListener(this);
        binding.btnRetrofit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnVolley:
                JsonObjectRequest jr = new JsonObjectRequest(
                        Request.Method.GET,
                        JSON_URL,
                        null,
                        this,
                        this
                );
                this.requestQueue.add(jr);
                break;
            case R.id.btnRetrofit:
                getDataFromApi();
                break;
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            JSONObject crnWeather = response.getJSONObject("current_weather");
            int weatherCode = Integer.parseInt(crnWeather.getString("weathercode"));

            JSONObject daily = response.getJSONObject("daily");
            JSONArray time = daily.getJSONArray("time");
            JSONArray wCode = daily.getJSONArray("weathercode");

            String latitude = response.getString("latitude");
            String longitude = response.getString("longitude");

            binding.recyclerView.setHasFixedSize(true);

            for (int i = 1; i < time.length(); i++) {
                String codeWeather = CodeWeather(Integer.parseInt(wCode.getString(i)));
                int imgcode = CodeWeatherImage((Integer.parseInt(wCode.getString(i))));
                arrayListWeather.add(new WeatherModel(codeWeather, time.getString(i), imgcode));
            }

            weatherAdapter = new WeatherAdapter(arrayListWeather);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
            binding.recyclerView.setLayoutManager(layoutManager);
            binding.recyclerView.setAdapter(weatherAdapter);

            binding.txtKondisiCrn.setText(CodeWeather(weatherCode));
            binding.txtTemperatureCrn.setText((crnWeather.getString("temperature")) + "°");
            binding.txtWsCr.setText("Windspeed : " + (crnWeather.getString("windspeed")));
            binding.txtLonglatCrn.setText(latitude +","+ longitude);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void getDataFromApi() {
        ApiService.endpoint().getData().enqueue(new Callback<WeatherModelRetrofit>() {
            @Override
            public void onResponse(Call<WeatherModelRetrofit> call, retrofit2.Response<WeatherModelRetrofit> response) {
                if (response.isSuccessful()){
                    WeatherModelRetrofit data = response.body();
                    binding.txtLonglatCrn.setText(String.valueOf(data.getLatitude()) + ","+String.valueOf(data.getLongitude()));
                    binding.txtWsCr.setText("Windspeed : " + String.valueOf(data.getCurrent_weather().getWindspeed()));
                    binding.txtTemperatureCrn.setText(String.valueOf(data.getCurrent_weather().getTemperature()) + "°");
                    binding.txtKondisiCrn.setText(CodeWeather(data.getCurrent_weather().getWeathercode()));

                    binding.recyclerView.setHasFixedSize(true);

                    for (int i = 1; i < data.getDaily().getTime().size(); i++) {
                        String codeWeather = CodeWeather(data.getDaily().getWeathercode().get(i));
                        int imgcode = CodeWeatherImage(data.getDaily().getWeathercode().get(i));
                        arrayListWeather.add(new WeatherModel(codeWeather, data.getDaily().getTime().get(i), imgcode));
                    }

                    weatherAdapter = new WeatherAdapter(arrayListWeather);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    binding.recyclerView.setLayoutManager(layoutManager);
                    binding.recyclerView.setAdapter(weatherAdapter);
                }
            }

            @Override
            public void onFailure(Call<WeatherModelRetrofit> call, Throwable t) {
                Log.d("MainActivity", t.toString());
            }
        });
    }

    public int CodeWeatherImage(int code) {
        int generateCode = 0;
        switch (code) {
            case 0:
                generateCode = R.drawable.cerah;
                break;
            case 1:
            case 2:
            case 3:
                generateCode = R.drawable.sedikit_berawan;
                break;
            case 45:
            case 48:
                generateCode = R.drawable.kabut;
                break;
            case 51:
            case 53:
            case 55:
                generateCode = R.drawable.gerimis;
//                generateCode = "Gerimis";
                break;
            case 56:
            case 57:
                generateCode = R.drawable.gerimis_dingin;
                break;
            case 61:
            case 63:
            case 65:
                generateCode = R.drawable.hujan;
                break;
            case 66:
            case 67:
//                generateCode = "Hujan Beku";
                break;
            case 71:
            case 73:
            case 75:
//                generateCode = "Hujan Salju";
                break;
            case 77:
//                generateCode = "Butiran Salju";
                break;
            case 80:
            case 81:
            case 82:
                generateCode = R.drawable.hujan_sedang;
//                generateCode = "Curah Hujan sedang";
                break;
            case 85:
            case 86:
//                generateCode = "Hujan Salju lebat";
                break;
            default:
//                generateCode = "Kondisi belum ditambahkan";
        }

        return generateCode;
    }

    public String CodeWeather(int code) {
        String generateCode = null;
        switch (code) {
            case 0:
                binding.imgCrn.setImageResource(R.drawable.cerah);
                generateCode = "Langit Cerah";
                break;
            case 1:
            case 2:
            case 3:
                binding.imgCrn.setImageResource(R.drawable.sedikit_berawan);
                generateCode = "Sebagian Berawan";
                break;
            case 45:
            case 48:
                binding.imgCrn.setImageResource(R.drawable.kabut);
                generateCode = "Kabut";
                break;
            case 51:
            case 53:
            case 55:
                binding.imgCrn.setImageResource(R.drawable.gerimis);
                generateCode = "Gerimis";
                break;
            case 56:
            case 57:
                binding.imgCrn.setImageResource(R.drawable.gerimis_dingin);
                generateCode = "Gerimis Dingin";
                break;
            case 61:
            case 63:
            case 65:
                binding.imgCrn.setImageResource(R.drawable.hujan);
                generateCode = "Hujan";
                break;
            case 66:
            case 67:
                generateCode = "Hujan Beku";
                break;
            case 71:
            case 73:
            case 75:
                generateCode = "Hujan Salju";
                break;
            case 77:
                generateCode = "Butiran Salju";
                break;
            case 80:
            case 81:
            case 82:
                binding.imgCrn.setImageResource(R.drawable.hujan_sedang);
                generateCode = "Curah Hujan sedang";
                break;
            case 85:
            case 86:
                generateCode = "Hujan Salju lebat";
                break;
            default:
                generateCode = "Kondisi belum ditambahkan";
        }

        return generateCode;
    }
}