package com.example.jarvis_project_versionpc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jarvis_project_versionpc.JARVIS.JARVIS_libraries;
import com.example.jarvis_project_versionpc.JARVIS.JARVIS_prototype;
import com.example.jarvis_project_versionpc.MainActivity;
import com.example.jarvis_project_versionpc.R;
import com.example.jarvis_project_versionpc.Supplements.ObservableString;

import java.util.ArrayList;

public class QueryActivity extends AppCompatActivity {

    JARVIS_libraries jarvisLibraries;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        final Bundle extras = getIntent().getExtras();
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.google.com/search?q="+extras.getString("query"));

        jarvisLibraries = new JARVIS_libraries(this);
        recognizeSpeech();
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
                    if(sections[i+1].equals("atras") || sections[i+1].equals("volver") || sections[i+1].equals("vuelve")){
                        if(webView.canGoBack()){
                            webView.goBack();
                        }
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
