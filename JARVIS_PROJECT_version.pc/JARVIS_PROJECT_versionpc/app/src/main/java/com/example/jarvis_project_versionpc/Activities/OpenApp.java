package com.example.jarvis_project_versionpc.Activities;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jarvis_project_versionpc.JARVIS.JARVIS_libraries;
import com.example.jarvis_project_versionpc.R;

import java.util.ArrayList;

public class OpenApp extends AppCompatActivity {

    JARVIS_libraries jarvisLibraries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openapp);
        View view = findViewById(R.id.view);
        //view.
        jarvisLibraries = new JARVIS_libraries(this);
        recognizeSpeech();
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.settings");
        startActivity(intent);
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                intent.finish();
            }
        };
        handler.postDelayed(runnable, 5000);
    }
    public void analyzeResults(String results){
        String result = jarvisLibraries.formatSpeech(jarvisLibraries.regexArticulos(jarvisLibraries.regexString(results)));
        String[] sections = result.split(" ");
        for(int i=0;i<sections.length;i++) {
            for(int z=0;z<jarvisLibraries.getJARVIS_NAMES().size();z++){
                if(sections[i].equals(jarvisLibraries.getJARVIS_NAMES().get(z))){
                    if(sections[i+1].equals("salir") || sections[i+1].equals("salte") || sections[i+1].equals("sal")){
                        finish();
                    }
                }
            }
        }
    }
    public void recognizeSpeech(){
        String recognitionLanguage = "es-ES";
        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, recognitionLanguage);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,3);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {}
            @Override
            public void onBeginningOfSpeech() {}
            @Override
            public void onRmsChanged(float rmsdB) {}
            @Override
            public void onBufferReceived(byte[] buffer) {}
            @Override
            public void onEndOfSpeech() {}
            @Override
            public void onError(int error) {
                speechRecognizer.startListening(speechRecognizerIntent);
            }
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                analyzeResults(data.get(0));
                speechRecognizer.startListening(speechRecognizerIntent);
            }
            @Override
            public void onPartialResults(Bundle partialResults) {}
            @Override
            public void onEvent(int eventType, Bundle params) {}
        });
        speechRecognizer.startListening(speechRecognizerIntent);
    }
}
