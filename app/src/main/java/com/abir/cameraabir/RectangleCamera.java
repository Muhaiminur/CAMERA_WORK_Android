package com.abir.cameraabir;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.abir.cameraabir.databinding.ActivityRectangleCameraBinding;
import com.bumptech.glide.Glide;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RectangleCamera extends AppCompatActivity {
    ActivityRectangleCameraBinding binding;
    Context context;
    ImageCapture imageCapture;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRectangleCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            context = this;
            camerawork();


        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    void camerawork() {
        try {
            cameraProviderFuture = ProcessCameraProvider.getInstance(this);
            cameraProviderFuture.addListener(() -> {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                }
            }, ContextCompat.getMainExecutor(this));
            onClick();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        Preview preview = new Preview.Builder()
                .build();
        //preview.setSurfaceProvider(binding.viewFinder.getSurfaceProvider());

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .build();
        ImageCapture.Builder builder = new ImageCapture.Builder();

        imageCapture = builder.build();

        preview.setSurfaceProvider(binding.cameraInput.getSurfaceProvider());
        /*imageCapture =
                new ImageCapture.Builder()
                        .setTargetRotation(this.getWindowManager().getDefaultDisplay().getRotation())
                        .build();*/
        cameraProvider.unbindAll();
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);
    }

    public void onClick() {
        binding.buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("paisi", "pa");
                SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
                //File file = new File(getBatchDirectoryName(), mDateFormat.format(new Date()) + ".jpg");
                File file = new File(context.getExternalCacheDir() + File.separator + System.currentTimeMillis() + ".jpg");

                ImageCapture.OutputFileOptions outputFileOptions =
                        new ImageCapture.OutputFileOptions.Builder(file).build();
                imageCapture.takePicture(outputFileOptions, executor,
                        new ImageCapture.OnImageSavedCallback() {
                            @Override
                            public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                                //Toast.makeText(SimpleCamera.this, "Image Saved successfully", Toast.LENGTH_SHORT).show();

                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (file.exists()) {
                                            Glide.with(context).load(file).into(binding.imagePreview);

                                        }
                                        Toast.makeText(context, "Image Saved successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //outputFileOptions.getMetadata().getLocation().
                                // insert your code here.
                            }

                            @Override
                            public void onError(ImageCaptureException error) {
                                // insert your code here.
                                Log.d("error", "not working" + error.toString());
                            }
                        }
                );
            }
        });


    }

}