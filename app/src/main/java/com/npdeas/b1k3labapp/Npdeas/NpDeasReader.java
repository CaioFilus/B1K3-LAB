package com.npdeas.b1k3labapp.Npdeas;

import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NPDEAS on 17/04/2018.
 */

public class NpDeasReader {


    private File file;
    private File routeFile;
    private FileInputStream input;
    private int offset = 0;


    public NpDeasReader() {
        file = new File(FileConstants.ROOT_DIR);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public NpDeasReader(File routeFile) {
        this();
        this.setRouteFile(routeFile);
    }
    public File getRootFile(){
        return  file;
    }

    public ArrayList<File> getRouteFiles() {
        ArrayList<File> result = new ArrayList<>();
        if (file.exists()) {
            File[] foldersAux = file.listFiles();
            for(int i = 0; i < foldersAux.length;i++){
                result.add(foldersAux[i].listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File file, String s) {
                        return s.endsWith(FileConstants.TXT_SUFIX);
                    }
                })[0]);
            }
            return result;
        } else {
            return null;
        }
    }

    public String[] getRouteNamesFiles(List<File> inFiles) {

        if (file.exists()) {
            File[] aux = inFiles.toArray(new File[inFiles.size()]);
            String[] result = new String[aux.length];
            for (int i = 0; i < aux.length; i++) {
                result[i] = aux[i].getName().substring(0, aux[i].getName().length()
                        - FileConstants.SUFIX_LEN);
            }
            return result;
        } else {
            return null;
        }
    }

    public ArrayList<File> getImage() {
        ArrayList<File> result = new ArrayList<>();
        if (file.exists()) {
            File[] foldersAux = file.listFiles();
            for(int i = 0; i < foldersAux.length;i++){
                result.add(foldersAux[i].listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File file, String s) {
                        return s.endsWith(FileConstants.IMG_SUFIX);
                    }
                })[0]);
            }
            return result;
        } else {
            return null;
        }
    }

    public void setRouteFile(File routeFile) {
        this.routeFile = routeFile;
        try {
            input = new FileInputStream(routeFile.getAbsolutePath());
        } catch (Exception e) {
            Log.i("Reader ", e.getMessage());
        }
    }
    public File getTxtFileFromFolder(File folder){
        File[] files = folder.listFiles();
        for(int i = 0; i < files.length;i++){
            if(files[i].getName().endsWith(FileConstants.TXT_SUFIX)){
                return files[i];
            }
        }
        return null;
    }
    public boolean setSeverDbSended(File file){
        File dbCheckFile = new File(file.getParentFile().getAbsolutePath(),"DbChecked.ckd");
        if(dbCheckFile.exists()){
            return true;
        }else{
            try {
                dbCheckFile.createNewFile();
            }catch (Exception e){
                return false;
            }finally {
                return true;
            }
        }
    }
    public File[] getUnsendedFile(){
        File[] result = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                File[] routeFiles = file.listFiles();
                for(int i = 0; i < routeFiles.length;i++){
                    if(routeFiles[i].getName().endsWith("ckd")){
                        return false;
                    }
                }
                return true;
            }
            });
            return result;
    }

    static public void removeFile(File file) {
        file.delete();
    }

    public FileStruct getFileStruct() {
        try {
            byte[] read = new byte[FileConstants.LINE_LENGTH];
            if (input.read(read, 0, FileConstants.LINE_LENGTH) != -1) {
                long longe = 0;
                long lati = 0;
                int speed = 0;
                int db = 0;
                short dis = 0;
                int i = 0;
                int j;
                for (j = 7; j >= 0; j--) {
                    longe |= ((long) (read[i++] & 0xFF) << (8 * j));
                }
                for (j = 7; j >= 0; j--) {
                    lati |= ((long) read[i++] & 0xFF) << (8 * j);
                }
                for (j = 3; j >= 0; j--) {
                    speed |= ((long) read[i++] & 0xFF) << (8 * j);
                }
                db = read[i++];
                for (j = 1; j >= 0; j--) {
                    dis |= ((long) read[i++] & 0xFF) << (8 * j);
                }
                FileStruct result = new FileStruct();
                result.setLongetude(Double.longBitsToDouble(longe));
                result.setLatitude(Double.longBitsToDouble(lati));
                result.setSpeed(Float.intBitsToFloat(speed));
                result.setDb(db);
                result.setDistance(dis);
                return result;
            }
        } catch (Exception e) {
            Log.i("Reader", e.getMessage());
        }
        return null;
    }
    /*private ArrayList<File> dateOrder(ArrayList<File> fileArrayList){

        Arrays.sort( fileArrayList, new Comparator<Object>()
        {
            public int compare(Object o1, Object o2) {

                if (((File)o1).lastModified() > ((File)o2).lastModified()) {
                    return -1;
                } else if (((File)o1).lastModified() < ((File)o2).lastModified()) {
                    return +1;
                } else {
                    return 0;
                }
            }

        });
    }*/
}
