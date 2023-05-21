package com.example.weathermine;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weathermine.databinding.ItemWeatherBinding;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private final ArrayList<WeatherModel> listWeather;

    public WeatherAdapter(ArrayList<WeatherModel> listWeather) {
        this.listWeather = listWeather;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWeatherBinding binding = ItemWeatherBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WeatherViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        holder.bind(listWeather.get(position));
    }

    @Override
    public int getItemCount() {
        return listWeather.size();
    }

    static class WeatherViewHolder extends RecyclerView.ViewHolder {
        final ItemWeatherBinding binding;

        WeatherViewHolder(ItemWeatherBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(WeatherModel weatherModel) {
            binding.txtKondisi.setText(weatherModel.getKondisi());
            binding.txtDate.setText(weatherModel.getDate());
            binding.ivCuaca.setImageResource(weatherModel.getImg());
        }
    }
}