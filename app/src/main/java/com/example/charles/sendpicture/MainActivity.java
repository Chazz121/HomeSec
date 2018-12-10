package com.example.charles.sendpicture;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.camerakit.CameraKitView;

import java.io.File;
import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity {
    private CameraKitView cameraKitView;
    private Button capButton;
    public int REQUEST_CODE = 200;
    MediaRecorder mRecorder;
    Boolean recording = false;
    String fileOutPutlocation = Environment.DIRECTORY_DOWNLOADS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

            Toast.makeText(MainActivity.this,"permission denied",Toast.LENGTH_SHORT).show();
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE);

            Toast.makeText(MainActivity.this,"permission denied",Toast.LENGTH_SHORT).show();
        }
            setContentView(R.layout.activity_main);
        cameraKitView = findViewById(R.id.camera);
        capButton = (Button) findViewById(R.id.capButton);
        capButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recording){
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
                recording = false;
            }
            else{
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.VORBIS);
                mRecorder.setOutputFile(fileOutPutlocation+"audio.ogg");
                recording = true;
                mRecorder.start();

            }
            Toast.makeText(MainActivity.this,"photo created in"+Environment.getExternalStorageDirectory(),Toast.LENGTH_LONG);
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {


                    @Override
                    public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {
                        File savedPhoto = new File(Environment.getExternalStorageDirectory(), "photo.jpg");

                        try {
                            FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
                            outputStream.write(capturedImage);
                            outputStream.close();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        recordAudio();
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void  recordAudio(){

    }

}
