package com.example.jarvis_project_versionpc.Supplements;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import androidx.annotation.Nullable;

public class TypeWritter extends androidx.appcompat.widget.AppCompatTextView {

    private CharSequence text;
    private int index;
    private long delay = 75;

    public TypeWritter(Context context) {
        super(context);
    }

    public TypeWritter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private Handler handler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(text.subSequence(0,index++));
            if(index<=text.length()){
                handler.postDelayed(characterAdder,delay);
            }
        }
    };

    public void animateText(CharSequence text){
        this.text = text;
        this.index=0;
        setText("");
        handler.removeCallbacks(characterAdder);
        handler.postDelayed(characterAdder,delay);
    }
    public void setCharacterDelay(long m){
        delay=m;
    }
}