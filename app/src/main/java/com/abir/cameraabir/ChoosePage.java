package com.abir.cameraabir;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.abir.cameraabir.databinding.ActivityChoosePageBinding;

public class ChoosePage extends AppCompatActivity {
    ActivityChoosePageBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChoosePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            context = this;
            onclick();

        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    void onclick() {
        try {
            binding.cameraNormal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(context, SimpleCamera.class));
                }
            });
            binding.cameraRectangle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(context, RectangleCamera.class));
                }
            });
            binding.cameraCircle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(context, RoundCamera.class));
                }
            });

        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }
}