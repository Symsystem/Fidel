package com.fidel.fidel.classes;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeremyduchesne on 2/05/15.
 */
public class Utils {

    public static String BASE_URL = "http://fidel.symsystem.com/";

    public static final int FAIL = 0;
    public static final int SUCCESS = 1;
    public static final int NUM_RES_KO = 2;
    public static final int LOGIN_TAKEN = 3;
    public static final int EMAIL_TAKEN = 4;
    public static final int BAGAGE_KO = 2;

    public static List<String> readFile(Context context, int path){

        List<String> listRead = new ArrayList<String>();

        try{
            Resources res = context.getResources();
            InputStream ips=res.openRawResource(path);
            InputStreamReader ipsr=new InputStreamReader(ips);
            BufferedReader br=new BufferedReader(ipsr);
            String ligne;
            while ((ligne=br.readLine())!=null){
                listRead.add(ligne);
            }
            br.close();
        }
        catch (IOException e){
            System.out.println(e.toString());
        }
        return listRead;
    }
}
