package com.example.jarvis_project_versionpc.JARVIS;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.LinearLayout;

import com.example.jarvis_project_versionpc.R;
import com.example.jarvis_project_versionpc.Supplements.ObservableString;
import com.example.jarvis_project_versionpc.Supplements.TypeWritter;

import java.util.ArrayList;

public class JARVIS_prototype {

    Context context;
    public JARVIS_methods jarvisMethods;
    String recognitionLanguage = "es-ES";
    // System basic elements
//-------------------------------------------------------//
    SpeechRecognizer speechRecognizer;
    Intent speechRecognizerIntent;
//-------------------------------------------------------//

    public JARVIS_prototype(Context context){
        this.context = context;
        this.jarvisMethods = new JARVIS_methods(this.context);
        recognizeSpeech();
    }

    public void recognizeSpeech(){
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this.context);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
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
                layoutLog(data.get(0));
                jarvisMethods.getCommand(data.get(0));
                speechRecognizer.startListening(speechRecognizerIntent);
            }
            @Override
            public void onPartialResults(Bundle partialResults) {}
            @Override
            public void onEvent(int eventType, Bundle params) {}
        });
        speechRecognizer.startListening(speechRecognizerIntent);
    }
    public void layoutLog(String str){
        TypeWritter typeWritter  = ((Activity)this.context).findViewById(R.id.typeWritter);
        typeWritter.setText("");
        typeWritter.setTextSize(34);
        typeWritter.setWidth(850);
        typeWritter.setTextColor(this.context.getColor(R.color.white));
        typeWritter.setCharacterDelay(60);
        LinearLayout.LayoutParams helloTextlayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        helloTextlayoutParams.setMargins(0,0,0,700);
        typeWritter.setLayoutParams(helloTextlayoutParams);
        typeWritter.animateText(str+".");

        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                typeWritter.setText("");
            }
        };
        handler.postDelayed(runnable, 5000);
    }
}

