package com.npdeas.b1k3labapp.Sensors;

import android.media.MediaRecorder;
import android.util.Log;

import com.npdeas.b1k3labapp.Constants;

import java.io.File;

/**
 * Created by NPDEAS on 04/04/2018.
 */

public class Microphone {
    public static final int MIC_OK = 0;

    private MediaRecorder mediaRecorder;//cria variavel mediaRecorder
    private File audiofile;
    private int avaregeMic[] = new int[Constants.AVAREGE_SIZE];
    private int index = 0;
    private int lastRecord = 0;
    private boolean isRecording = false;


    public Microphone() {

    }

    public boolean startRecord() {//função de gravação
        if (mediaRecorder == null) {
            try {
                audiofile = File.createTempFile("sound", ".3gp");//cria arquivo temporário
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setOutputFile(audiofile.getAbsolutePath());
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                mediaRecorder.prepare();
                mediaRecorder.start();
                isRecording = true;
                return true;
            } catch (Exception e) {
                //Log.i("MICROPHONE",e.getMessage());//Mensagem de erro
                return false;
            }
        }
        return true;
    }

    public int getAmplitude() {
        if (mediaRecorder != null) {
            return mediaRecorder.getMaxAmplitude();
        }
        return 0;
    }//função getAmplitude retorna a máxima amplitude da variável mediaRecorder

    public double getDB() {
        return 20 * Math.log10(getAmplitude());
    }// função getDB retorna 20*log10 da função getAmplitude

    public double getDB(int amp) {
        return 20 * Math.log10(amp);
    }

    // função getDB retorna valor inteiro 20*log10 da variavel amp
    public double getDB(double amp) {
        return 20 * Math.log10(amp);
    }

    // função getDB retorna valor 20*log10 da variavel amp
    public double getDbMobilAvarage() {//cria função getDbMobilAvarage()
        if (mediaRecorder != null) {
            int sum = 0;
            if (index >= Constants.AVAREGE_SIZE) {//caso index seja >=Constants.AVAREGE_SIZE receberá 0
                index = 0;
            }
            avaregeMic[index] = mediaRecorder.getMaxAmplitude();
            index++;
            int size = 0;
            for (int j = 0; j < Constants.AVAREGE_SIZE; j++) {
                if (avaregeMic[j] != 0) {
                    sum += avaregeMic[j];
                    size++;
                }
            }
            if (size == 0) {
                return 0;
            }
            int avarage = sum / size;
            lastRecord = avarage;
            return getDB(avarage);
        } else {
            return getDB(lastRecord);
        }
        //return db + getDB(avarage/32767.)/94;
    }

    public void stopRecording() {
        try {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
            audiofile.delete();

        } catch (Exception e) {

        }
    }

//    public void start() {
//        mediaRecorder.start();// início da gravação
//    }

    public void stop() {
        mediaRecorder.stop();
    }
}
