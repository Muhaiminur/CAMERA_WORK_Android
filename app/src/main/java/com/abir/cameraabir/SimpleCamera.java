package com.abir.cameraabir;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
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

import com.abir.cameraabir.databinding.ActivitySimplecameraBinding;
import com.bumptech.glide.Glide;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SimpleCamera extends AppCompatActivity {

    ActivitySimplecameraBinding binding;
    Context context;
    ImageCapture imageCapture;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    private Executor executor = Executors.newSingleThreadExecutor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySimplecameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            context = this;
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
            if (checkCameraHardware(context)) {
                Log.d("camera", "Camera enable");
                onClick();
            } else {
                Toast.makeText(context, "Camera Not Available", Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
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

        preview.setSurfaceProvider(binding.viewFinder.getSurfaceProvider());
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
                                        Toast.makeText(SimpleCamera.this, "Image Saved successfully", Toast.LENGTH_SHORT).show();
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

    public String getBatchDirectoryName() {

        String app_folder_path = "";
        app_folder_path = Environment.getExternalStorageDirectory().toString() + "/images";
        File dir = new File(app_folder_path);
        if (!dir.exists() && !dir.mkdirs()) {

        }
        return app_folder_path;
    }
}