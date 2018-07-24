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
    private int avaregeMic[] =  new int[Constants.AVAREGE_SIZE];
    private int index = 0;


    public Microphone(){
        mediaRecorder = new MediaRecorder();//alocação de memória
        try {
            File audiofile = File.createTempFile("sound", ".3gp");//cria arquivo temporário
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// Entrada Microfone
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);//Ajusta formato de saída .mp3
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//Codificação para mp3
            mediaRecorder.setOutputFile(audiofile.getAbsolutePath());
        }catch (Exception e){
            Log.i("teste",e.getMessage());//Mensagem de erro
        }
    }
    public boolean startRecord(){//função de gravação
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();// início da gravação
            return true;
        }catch (Exception e){
            Log.i("MICROPHONE",e.getMessage());//Mensagem de erro
            return false;
        }
    }
    public int getAmplitude(){
        return mediaRecorder.getMaxAmplitude();
    }//função getAmplitude retorna a máxima amplitude da variável mediaRecorder
    public double getDB(){
        return 20* Math.log10(getAmplitude());
    }// função getDB retorna 20*log10 da função getAmplitude
    public double getDB(int amp){
        return 20* Math.log10(amp);
    }
    // função getDB retorna valor inteiro 20*log10 da variavel amp
    public double getDB(double amp){
        return 20* Math.log10(amp);
    }
    // função getDB retorna valor 20*log10 da variavel amp
    public double getDbMobilAvarage(){//cria função getDbMobilAvarage()
        int sum = 0;
        if(index >= Constants.AVAREGE_SIZE){//caso index seja >=Constants.AVAREGE_SIZE receberá 0
            index = 0;
        }
        avaregeMic[index] =  mediaRecorder.getMaxAmplitude();
        index++;
        int size = 0;
        for(int j = 0; j < Constants.AVAREGE_SIZE;j++){
            if (avaregeMic[j] != 0){
                sum += avaregeMic[j];
                size++;
            }
        }
        if(size == 0){
            return 0;
        }
        int avarage = sum/size;
        return getDB(avarage);
        //return db + getDB(avarage/32767.)/94;
    }
    public void stopRecording(){
        mediaRecorder.stop();
        mediaRecorder.release();
    }
    public void start(){
        mediaRecorder.start();// início da gravação
    }
    public void stop(){
        mediaRecorder.stop();
    }
}
