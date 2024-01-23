package com.example.jarvis_project_versionpc.JARVIS;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JARVIS_libraries {
    // JARVIS LIBRARIES
//---------------------------------------------------------------------------------//
    Context context;
    private final ArrayList<String> JARVIS_NAMES = Stream.of(
            "jarvis","yarvis","llarvis","jarbis","yarbis","llarbis",
            "charles","derbis","darwins","barbies","jordi","luis","ya véis"
    ).collect(Collectors.toCollection(ArrayList::new));

    private final ArrayList<String[][]> COMMAND_LIST = Stream.of(
            new String[][]{ // Necesita todas las palabras
                    {"escribe","escribir","muestra"},
                    {"busca", "buscar", "buscame","vusca"},
                    {"envia","whatsapp","manda"}
            },
            new String[][]{ // Va por palabras clave
                    {"llama", "llamar","llamame","yama"},
                    {"atras","Atras","vuelve","Vuelve"},
                    {"volumen","volume","Volumen"},
                    {"abre","lanza","abro","abra","abreme"},
                    {"donde"}
            }
    ).collect(Collectors.toCollection(ArrayList::new));

    private final ArrayList<String[]> APPS_LIST = Stream.of(
            new String[]{"com.spotify.music","spotify"},
            new String[]{"com.android.settings","ajustes"},
            new String[]{"com.android.chrome","google"},
            new String[]{"com.imaginbank.app","imagine"},
            new String[]{"com.instagram.android","instagram"},
            new String[]{"com.google.android.gm","email","mail"},
            new String[]{"com.google.android.apps.docs","drive"},
            new String[]{"com.google.android.apps.maps","maps"},
            new String[]{"com.shazam.android","shazam"},
            new String[]{"com.tranzmate","muvit"},
            new String[]{"com.android.deskclock","reloj"},
            new String[]{"com.miui.calculator","calculadora"},
            new String[]{"com.miui.notes","notas"},
            new String[]{"com.netflix.mediaclient","netflix"},
            new String[]{"pro.anioload.animecenter","anime"},
            new String[]{"com.supercell.clashroyale","royale","clash royale"},
            new String[]{"com.whatsapp","whatsapp"},
            new String[]{"com.google.android.youtube","youtube"},
            new String[]{"es.shufflex.dixmax.android","dixmax"},
            new String[]{"com.supercell.brawlstars","brawl stars","brawl"},
            new String[]{"com.supercell.clashofclans","clash of clans","clash"}

    ).collect(Collectors.toCollection(ArrayList::new));
    ArrayList<String[]> contactsDirectory = new ArrayList<>();

    // JARVIS STORED METHODS

    // GETTERS & SETTERS
    public ArrayList<String[][]> getCOMMAND_LIST(){
        return this.COMMAND_LIST;
    }
    public ArrayList<String[]> getAppsList(){
        return this.APPS_LIST;
    }
    public ArrayList<String> getJARVIS_NAMES(){return this.JARVIS_NAMES;}

    // FORMAT
    public Boolean lookForJARVIS(String speech){
        speech = regexString(speech);
        String[] speechSections = speech.split(" ");
        for(int i=0;i<speechSections.length;i++){
            for(int z=0;z<JARVIS_NAMES.size();z++){
                if(speech.contains(JARVIS_NAMES.get(z))){
                    return true;
                }
            }
        }
        return false;
    }
    public String formatSpeech(String speech){
        speech = regexString(speech);
        String[] speechSections = speech.split(" ");
        Boolean JARVISFound = false;
        speech = "";
        for(int i=0;i<speechSections.length;i++){
            for(int z=0;z<JARVIS_NAMES.size();z++){
                if(speechSections[i].contains(JARVIS_NAMES.get(z))){JARVISFound=true;}
            }
            if(JARVISFound){

                if(speech.equals("")){
                    speech = speechSections[i];
                }
                else{
                    speech = speech +" "+ speechSections[i];
                }
            }
        }
        return speech;
    }
    public String[] searchForDot(String[] commandSections){
        String[] commandParts = commandSections;
        for(int i=0;commandParts!=null && i<commandParts.length;i++){
            if(commandParts[i].equals("punto")){commandParts[i]=".";}
        }
        return commandParts;
    }
    public String regexString(String string){
        string = string.toLowerCase();
        String[][] regex = new String[][]{{"á","a"},{"é","e"},{"í","i"},{"ó","o"},{"ú","u"}};
        for(int i=0;i<string.length();i++){
            for(int z=0;z<regex.length-1;z++){
                if(string.substring(i,i+1).equals(regex[z][0])){
                    string=string.substring(0,i)+regex[z][1]+string.substring(i+1,string.length());
                }
            }
        }
        return string;
    }
    public String regexArticulos(String string){
        String[] array = string.toLowerCase().split(" ");
        string = "";
        String[] regex = new String[]{"se","pon","la","lo","el","las","les","los","del","a","ah","e","o","u","y"};
        for(int i=0;i<array.length;i++){
            Boolean valid = true;
            for(int z=0;z<regex.length-1;z++){
                if(array[i].equals(regex[z])){
                    valid=false;
                }
            }
            if(valid){
                if(string.equals("")){
                    string = string + array[i];
                }
                else{
                    string = string + " " + array[i];
                }
            }
        }
        return string;
    }
    public int numFromStringToInt(String numString){
        ArrayList<String[]> numStringInt = Stream.of(
                new String[]{"cero","0"}, new String[]{"uno","1"}, new String[]{"dos","2"},
                new String[]{"tres","3"}, new String[]{"cuatro","4"}, new String[]{"cinco","5"},
                new String[]{"seis","6"}, new String[]{"siete","7"}, new String[]{"ocho","8"},
                new String[]{"nueve","9"}, new String[]{"diez","10"},new String[]{"diez","11"},
                new String[]{"doce","12"}, new String[]{"trece","13"},new String[]{"catorce","14"},
                new String[]{"quince","15"}
        ).collect(Collectors.toCollection(ArrayList::new));
        for(int i=0;i<numStringInt.size();i++){
            if(numStringInt.get(i)[0].equals(numString)){return Integer.valueOf(numStringInt.get(i)[1]);}
            if(numStringInt.get(i)[1].equals(numString)){return Integer.valueOf(numStringInt.get(i)[1]);}
        }
        return 0;
    } //Up to 15

    // CALLING REQUIRED METHODS
    public String getContactNumber(String[] commandSections){
        String contacName = getContactName(commandSections);
        for(int i=0;contactsDirectory!=null && i<contactsDirectory.size();i++){
            if(regexString(contactsDirectory.get(i)[0]).equals(regexString(contacName))){
                return contactsDirectory.get(i)[1];
            }
        }
        return "";
    }
    public String getContactName(String[] commandSections){
        for(int i=0;i<contactsDirectory.size();i++){
            if(contactsDirectory.get(i)[0].toLowerCase().equals(commandSections[2])){return contactsDirectory.get(i)[0].toLowerCase();}
            else if(contactsDirectory.get(i)[0].toLowerCase().equals(commandSections[2]+" "+commandSections[3])){return contactsDirectory.get(i)[0].toLowerCase();}
            else if(contactsDirectory.get(i)[0].toLowerCase().equals(commandSections[2]+commandSections[3])){return contactsDirectory.get(i)[0].toLowerCase();}
        }
        return "";
    }

    // CONSTRUCTOR
    public JARVIS_libraries(Context context){this.context = context;}
}
