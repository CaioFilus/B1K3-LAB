package com.npdeas.b1k3labapp.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class FileUtils {

    private static final String imgFolder = "route_imgs";

    public static boolean saveImg(Context context, Bitmap img, String name) {
        try {
            File root = context.getFilesDir();
            File folder = new File(root, imgFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File imgFile = new File(folder, name + ".png");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.PNG, 100, stream);
            OutputStream outputStream = new FileOutputStream(imgFile);
            outputStream.write(stream.toByteArray());
            outputStream.close();
//            img.recycle();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Bitmap readImg(Context context, String name) {
        try {
            File root = context.getFilesDir();
            File folder = new File(root, imgFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File imgFile = new File(folder, name + ".png");
            if (imgFile.exists()) {
                FileInputStream inputStream = new FileInputStream(imgFile);
                byte fileContent[] = new byte[(int) imgFile.length()];
                inputStream.read(fileContent);
                return BitmapFactory.decodeByteArray(fileContent, 0, fileContent.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
