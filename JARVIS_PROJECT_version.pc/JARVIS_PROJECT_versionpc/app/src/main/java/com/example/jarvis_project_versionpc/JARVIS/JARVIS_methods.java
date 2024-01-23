package com.example.jarvis_project_versionpc.JARVIS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.net.URLEncoder;
import java.util.ArrayList;

import android.os.Handler;
import android.os.Looper;

import com.example.jarvis_project_versionpc.Activities.OpenApp;
import com.example.jarvis_project_versionpc.Activities.QueryActivity;
import com.example.jarvis_project_versionpc.MainActivity;
import com.example.jarvis_project_versionpc.R;
import com.example.jarvis_project_versionpc.Supplements.TypeWritter;

public class JARVIS_methods{

    // JARVIS VARS
//------------------------------------------------//
    private Boolean JARVIS_ACTIVE = true;
    private JARVIS_libraries jarvisLibraries;
    private ArrayList<String[][]> COMMAND_LIST;
    //------------------------------------------------//
    Context context;
    String command;
    LinearLayout linearLayout;
    Boolean GRABANDO = false;
//------------------------------------------------//

    public JARVIS_methods(Context context) {
        this.context = context;
        this.linearLayout = (LinearLayout) ((Activity)this.context).findViewById(R.id.layout);
        this.jarvisLibraries = new JARVIS_libraries(this.context);
        this.COMMAND_LIST = jarvisLibraries.getCOMMAND_LIST();
        this.getContacts();
    }

    public boolean getCommand(String speech){
        if(JARVIS_ACTIVE && jarvisLibraries.lookForJARVIS(speech)){
            String[][] not_keyCommands = COMMAND_LIST.get(0);
            String[][] keyCommands = COMMAND_LIST.get(1);
            speech = jarvisLibraries.formatSpeech(speech);
            speech = jarvisLibraries.regexString(speech);
            String[] speechSections = speech.split(" ");
            if(speechSections.length==1){sayHello();return true;}
            else{
                for(int index=0;index<not_keyCommands.length;index++){
                    for(int z=0;z<not_keyCommands[index].length;z++){
                        if(not_keyCommands[index][z].equals(speechSections[1])){
                            this.command = speech;
                            runNot_keyCommands(index);
                            return true;
                        }
                    }
                }
                this.command = speech;
                for(int index=0;index<keyCommands.length;index++){
                    for(int z=0;z<keyCommands[index].length;z++){
                        if(keyCommands[index][z].equals(speechSections[1])){
                            runKeyCommands(index);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    private void runNot_keyCommands(int index){
        switch(index) {
            case 0:
                String[] commandParts = this.command.split(" ");
                jarvisLibraries.searchForDot(commandParts);
                String text = "";
                for(int i=2;i<commandParts.length;i++){
                    text = text + " " +commandParts[i];
                }
                writeText(text);
                break;
            case 1:
                String query = "";
                String[] parts = this.command.split(" ");
                for(int i=2;i< parts.length;i++){
                    if(query.equals("")){query = query + parts[i];}
                    else{query = query + " " +parts[i];}
                }
                searchQuery(query);
                break;
            case 2:
                String[] copyCommandSections = jarvisLibraries.regexArticulos(jarvisLibraries.regexString(this.command)).split(" ");
                String numeroTelefono = jarvisLibraries.getContactNumber(copyCommandSections);
                String msg = "";
                String[] sections = this.command.split(" ");
                for(int i=4;i<sections.length;i++){
                    msg = msg + sections[i];
                }
                sendMessage(numeroTelefono,msg);
                break;
            default: layoutLog("No encuentro ese comando");
        }
    }
    private void runKeyCommands(int index){
        switch(index) {
            case 0:
                String[] copyCommandSections = jarvisLibraries.regexArticulos(jarvisLibraries.regexString(this.command)).split(" ");
                copyCommandSections = jarvisLibraries.searchForDot(copyCommandSections);
                String contactNumber = jarvisLibraries.getContactNumber(copyCommandSections);
                llamarAContacto(contactNumber);
                break;
            case 1:
                onBackPressed();
                break;
            case 2:
                this.command = jarvisLibraries.regexArticulos(jarvisLibraries.regexString(this.command));
                String[] sections = this.command.split(" ");
                setVolume(sections);
                break;
            case 3:
                String[] parts = this.command.split(" ");
                String app = "";
                for(int i=2;i<parts.length;i++){
                    if(app.equals("")){
                        app = app + parts[i];
                    }
                    else{
                        app = app + " " +parts[i];
                    }
                }
                openApp(app);
                break;
            case 4:
                locateJARVIS();
                break;
            default: layoutLog("No encuentro ese comando");
        }
    }
    public void layoutLog(String str){
        TypeWritter typeWritter  = ((Activity)this.context).findViewById(R.id.typeWritter);
        typeWritter.setText("");
        typeWritter.setTextSize(34);
        typeWritter.setWidth(850);
        typeWritter.setTextColor(this.context.getColor(R.color.white));
        typeWritter.setCharacterDelay(60);
        typeWritter.animateText(str);

        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                typeWritter.setText("");
            }
        };
        handler.postDelayed(runnable, 8000);
    }
    public void getContacts(){
        while(ContextCompat.checkSelfPermission(this.context, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity)this.context,new String[]{Manifest.permission.READ_CONTACTS},0);
        }

        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri,null,null,null,null);
        ArrayList<String[]> contactsDirectory = new ArrayList<>();

        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                Boolean validContact = true;
                @SuppressLint("Range") String
                        contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                if(contactName == "-1"){validContact=false;}
                @SuppressLint("Range") String
                        contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if(contactNumber == "-1"){validContact=false;}
                if(validContact && contactName != null && contactNumber != null){
                    contactsDirectory.add(new String[]{contactName,contactNumber});
                }
            }
        }
        if(contactsDirectory==null){
            Toast.makeText(this.context, "Agenda vacia", Toast.LENGTH_SHORT).show();
        }
        jarvisLibraries.contactsDirectory = contactsDirectory;
    }

    // Command Methods
//------------------------------------------------//
    private void sayHello(){
        TypeWritter typeWritter  = ((Activity) this.context).findViewById(R.id.typeWritter);
        typeWritter.setText("");
        typeWritter.setTextColor(this.context.getColor(R.color.white));
        typeWritter.setTextSize(37);
        typeWritter.setTypeface(typeWritter.getTypeface(), Typeface.BOLD_ITALIC);
        typeWritter.animateText("Â¿EN QUE PUEDO\n AYUDARTE?");
    }
    private void writeText(String text){
        TypeWritter typeWritter  = new TypeWritter(this.context);
        typeWritter.setText("");
        typeWritter.setTextSize(23);
        typeWritter.setTextColor(this.context.getColor(R.color.white));
        typeWritter.setCharacterDelay(60);
        linearLayout.addView(typeWritter);
        typeWritter.animateText(text);
    }
    private void llamarAContacto(String contactNumber) {
        if(contactNumber.equals("")){
            layoutLog("No encontramos el contacto");
        }
        else{
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+contactNumber+""));
            this.context.startActivity(intent);
        }
    }
    private void searchQuery(String query){
        try{
            Intent intent = new Intent(this.context, QueryActivity.class);
            intent.putExtra("query",query);
            this.context.startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context,"No se pudo completar la busqueda",Toast.LENGTH_SHORT).show();
        }
    }
    public void onBackPressed(){
        Intent intent = new Intent(this.context, MainActivity.class);
        this.context.startActivity(intent);
        ((Activity)this.context).finish();
    }
    private void setVolume(String[] sections){
        AudioManager audioManager = (AudioManager) this.context.getSystemService(this.context.AUDIO_SERVICE);
        if(sections.length>1){
            if(sections[1].equals("volumen")){
                if(sections.length>2){
                    if(
                            sections[2].equals("arriba") || sections[2].equals("abajo") ||
                                    sections[2].equals("mas") || sections[2].equals("menos")
                    ){
                        if(sections[2].equals("arriba") || sections[2].equals("mas")){
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
                        }
                        if(this.command.split(" ")[2].equals("abajo") || sections[2].equals("menos")){
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);
                        }
                    }
                    else{
                        int times = 3;
                        times = jarvisLibraries.numFromStringToInt(this.command.split(" ")[2]);
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,times,AudioManager.FLAG_SHOW_UI);
                    }
                }
            }
        }
    }
    private void openApp(String app){
        String packageName = "hola";
        for(int i=0;i<jarvisLibraries.getAppsList().size();i++){
            for(int z=0;z<jarvisLibraries.getAppsList().get(i).length;z++){
                if(app.toLowerCase().equals(jarvisLibraries.getAppsList().get(i)[z])){
                    packageName = jarvisLibraries.getAppsList().get(i)[0];
                }
            }
        }
        //Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        Intent intent = new Intent(this.context, OpenApp.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.context.startActivity(intent);
    }
    private void locateJARVIS(){
        String[] commandParts = jarvisLibraries.regexArticulos(jarvisLibraries.regexString(this.command)).split(" ");
        for(int i=0;i<commandParts.length;i++){
            if(commandParts[0].equals("donde") && commandParts[1].equals("estas")){
                // Emitir sonido
            }
        }
    } //No terminado
    private void grabadora(){
        MediaRecorder mediaRecorder = new MediaRecorder();
    }
    private void sendMessage(String numeroTelefono,String msg){
        if(!numeroTelefono.equals("")){

            PackageManager packageManager = context.getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);

            try {
                String url = "https://api.whatsapp.com/send?phone="+ numeroTelefono +"&text=" + URLEncoder.encode(msg, "UTF-8");
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                if (i.resolveActivity(packageManager) != null) {
                    context.startActivity(i);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            TypeWritter typeWritter = ((Activity)this.context).findViewById(R.id.typeWritter);
            Handler handler = new Handler(Looper.getMainLooper());
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    long downTime = SystemClock.uptimeMillis();
                    long eventTime = SystemClock.uptimeMillis() + 100;
                    float x = 550f;
                    float y = 400f;
                    int metaState = 0;
                    MotionEvent motionEvent = MotionEvent.obtain(
                            downTime,
                            eventTime,
                            MotionEvent.ACTION_UP,
                            x,
                            y,
                            metaState
                    );
                    typeWritter.dispatchTouchEvent(motionEvent);
                }
            };
            handler.postDelayed(runnable, 2250);

        }
        else{
            layoutLog("No encuentro el contacto.");
        }
    }
    private void anota(String nota){
        nota = "nota1";
        //Shared Preferences -> hacer una agenda ahi
    }
//------------------------------------------------//
}
