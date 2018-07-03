package com.npdeas.b1k3labapp.Route.Npdeas;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by NPDEAS on 12/04/2018.
 */

public class NpDeasWriter {

    private final static int LINE_LENGTH = 24 ;
    private final static String ROOT_DIR = "/sdcard";

    private File file;
    private File bikeLabFolder;
    private File routeFile;
    private String folderName;
    private String fileName;
    private FileOutputStream outputStream;
    private byte[] lineBytes =  new byte[LINE_LENGTH];


    public NpDeasWriter(String fileName){

        this.fileName = fileName;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(FileConstants.DATA_FORMAT);
        
        String dir = ROOT_DIR + File.separator + "B1K3Lab";

        folderName = sdf.format(cal.getTime());
        //create bikeLabFolder
        bikeLabFolder = new File(dir); //bikeLabFolder name
        routeFile = new File(dir + File.separator + folderName);
        if(!bikeLabFolder.exists()){
            bikeLabFolder.mkdirs();
        }
        routeFile.mkdirs();
        file = new File(routeFile.getAbsolutePath(),fileName + ".txt");
        if(!file.exists()){
            try {
                file.createNewFile();
                outputStream = new FileOutputStream(file.getAbsolutePath());
            }catch (Exception e){
                Log.i("FILE",e.getMessage());
            }
        }
        //this.writePreHeader();

    }

    public synchronized void  addNewLine(FileStruct struct){
        try {
            int i = 0;
            int j;
            long tempLong = Double.doubleToLongBits(struct.getLongetude());
            long tempLati = Double.doubleToLongBits(struct.getLatitude());
            int tempSpeed = Float.floatToIntBits(struct.getSpeed());

            for(j = 7;j >= 0;j--){
                lineBytes[i++] = (byte) ((tempLong>>(8*j)) & 0xFF);
            }
            for(j = 7;j >= 0;j--){
                lineBytes[i++] = (byte) ((tempLati>>(8*j)) & 0xFF);
            }
            for(j = 3;j >= 0;j--){
                lineBytes[i++] = (byte) ((tempSpeed >> (8*j)) & 0xFF);
            }
            lineBytes[i++] = (byte) struct.getDb();
            for(j = 1;j >= 0;j--){
                lineBytes[i++] = (byte) ((struct.getOvertaking() >> (8*j)) & 0xFF);
            }
            lineBytes[i] = '\n';
            outputStream.write(lineBytes,0,i);
            outputStream.flush();

        }catch (Exception e){

        }

    }
    public synchronized void close(){
        try {
            outputStream.close();
        }catch (Exception e){

        }
    }
    public synchronized void close(Bitmap bitmap){
        if(file.length()!=0) {
            try {
                FileOutputStream imgOutput = new FileOutputStream(routeFile.getAbsolutePath()
                        + File.separator + fileName + ".png");
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, imgOutput);
            } catch (Exception e) {

            }
        }else{
            file.delete();
            file.getParentFile().delete();
        }
        this.close();
    }
    private void writePreHeader(){
        byte[] header = new byte[17];
        long startTime =  System.currentTimeMillis();
        int i = 0;
        int j;
        try {
            for (j = 7; j >= 0; j--) {
                header[i++] = (byte) ((startTime >> (8 * j)) & 0xFF);
            }
            for (j = 0; j < 8; j++) {
                header[i++] = 0;
            }
            header[i] = '\n';
            outputStream.write(lineBytes, 0, i);
            outputStream.flush();
        }catch (Exception e){
            Log.i("Writer ",e.getMessage());
        }
    }
    private void writePosHeader(){
        long endTime =  System.currentTimeMillis();
        try {
            outputStream = new FileOutputStream(file.getAbsolutePath());
            byte[] postHeader = new byte[8];


        }catch (Exception e){

        }
    }
}
