package com.npdeas.b1k3labapp.Route.Npdeas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NPDEAS on 17/04/2018.
 */

public class NpDeasReader {


    private File rootFile;
    private File routeFolder;
    private File routeFile;
    private FileInputStream input;
    private int offset = 0;


    public NpDeasReader() {
        rootFile = new File(FileConstants.ROOT_DIR);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
    }

    public NpDeasReader(File routeFolder) {
        this();
        this.routeFolder = routeFolder;
        this.setRouteFile(getTxtFileFromFolder(routeFolder));
    }

    public File getRootFile() {
        return rootFile;
    }

    public ArrayList<File> getRouteFiles() {
        ArrayList<File> result = new ArrayList<>();
        if (rootFile.exists()) {
            File[] foldersAux = rootFile.listFiles();
            for (int i = 0; i < foldersAux.length; i++) {
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
        if (rootFile.exists()) {
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

    public Bitmap getImage() {
        File[] imgFile = routeFolder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.getName().endsWith(FileConstants.IMG_SUFIX)) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        if (imgFile.length == 1) {
            return BitmapFactory.decodeFile(imgFile[0].getAbsolutePath());
        }
        return null;
    }

    public ArrayList<File> getImages() {
        ArrayList<File> result = new ArrayList<>();
        if (rootFile.exists()) {
            File[] foldersAux = rootFile.listFiles();
            for (int i = 0; i < foldersAux.length; i++) {
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

    public String getFileName() {
        return routeFile.getName();
    }

    public String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(FileConstants.DATA_FORMAT);
        return sdf.format(routeFolder.lastModified());
    }

    public void setRouteFile(File routeFile) {
        this.routeFile = routeFile;
        try {
            input = new FileInputStream(routeFile.getAbsolutePath());
        } catch (Exception e) {
            Log.i("Reader ", e.getMessage());
        }
    }

    public File getTxtFileFromFolder(File folder) {
        File[] files = folder.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().endsWith(FileConstants.TXT_SUFIX)) {
                return files[i];
            }
        }
        return null;
    }

    public boolean setSeverDbSended(File file) {
        File dbCheckFile = new File(file.getParentFile().getAbsolutePath(), "DbChecked.ckd");
        if (dbCheckFile.exists()) {
            return true;
        } else {
            try {
                dbCheckFile.createNewFile();
            } catch (Exception e) {
                return false;
            } finally {
                return true;
            }
        }
    }

    public File[] getUnsendedFile() {
        File[] result = rootFile.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                File[] routeFiles = file.listFiles();
                for (int i = 0; i < routeFiles.length; i++) {
                    if (routeFiles[i].getName().endsWith("ckd")) {
                        return false;
                    }
                }
                return true;
            }
        });
        return result;
    }
    public File getFolderPath(){
        return routeFolder;
    }
    public void remove() {

        File[] files = routeFolder.listFiles();
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
        routeFolder.delete();
    }

    public RouteNode getFileStruct() {
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
                RouteNode result = new RouteNode();
                result.setLongetude(Double.longBitsToDouble(longe));
                result.setLatitude(Double.longBitsToDouble(lati));
                result.setSpeed(Float.intBitsToFloat(speed));
                result.setDb(db);
                result.setOvertaking(dis);
                return result;
            }
        } catch (Exception e) {
            Log.i("Reader", e.getMessage());
        }
        return null;
    }


}
