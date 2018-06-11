package com.npdeas.b1k3labapp.Npdeas;

import android.graphics.Bitmap;

/**
 * Created by NPDEAS on 02/05/2018.
 */

public class NpdeasUtils {

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String sbttDotForBar(String in){
        char []  aux =  in.toCharArray();
        for(int i = 0; i < in.length();i++){
            if(aux[i] == '.'){
                aux[i] = '\\';
            }
        }
        return aux.toString();
    }

}
