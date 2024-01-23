package com.example.jarvis_project_versionpc;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.jarvis_project_versionpc.Activities.QueryActivity;
import com.example.jarvis_project_versionpc.JARVIS.JARVIS_prototype;
import com.example.jarvis_project_versionpc.Supplements.ObservableString;

public class MainActivity extends AppCompatActivity {

    // System JARVIS basic methods
//-------------------------------------------------------//
    JARVIS_prototype JARVIS;
    private void getPermissions(){
        if(ActivityCompat.checkSelfPermission(
                MainActivity.this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    ,1
            );
        }
    }
//-------------------------------------------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermissions();
        JARVIS = new JARVIS_prototype(this);
        JARVIS.jarvisMethods.getCommand("Jarvis abre Spotify");
    }
    @Override
    public void onResume() {
        super.onResume();
        JARVIS.recognizeSpeech();
    }
}