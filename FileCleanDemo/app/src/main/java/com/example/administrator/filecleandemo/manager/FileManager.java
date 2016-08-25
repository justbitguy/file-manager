package com.example.administrator.filecleandemo.manager;


/**
 * Created by Zac on 2016/8/24.
 */


public class FileManager {
    public static final int FILE_ALL = 1 << 0;
    public static final int FILE_IMAGE = 1 << 1;
    public static final int FILE_AUDIO = 1 << 2;
    public static final int FILE_VIDEO = 1 << 3;
    public static final int FILE_APK = 1 << 4;
    public static final int FILE_COMPRESS = 1 << 5; // .zip, .rar
    public static final int FILE_DOC = 1 << 6; // .txt, .pdf, .doc etc...

    private static FileManager sInstance;

    private FileManager(){
    }

    public static FileManager getInstance(){
        synchronized (FileManager.class){
            if (sInstance == null){
                sInstance = new FileManager();
            }
        }
        return sInstance;
    }

    public void startScan(int scanType){
        switch (scanType){
            case FILE_ALL:
                // TODO: 2016/8/24
                BigFileScanner scanner4=new BigFileScanner();
                scanner4.startScan();
                break;
            case FILE_IMAGE:
                ImageFileScanner scanner = new ImageFileScanner();
                scanner.startScan();
                break;
            case FILE_AUDIO:
                // TODO: 2016/8/24
                MusicScanner scanner3=new MusicScanner();
                scanner3.startScan();
                break;
            case FILE_COMPRESS:
                ZipScanner scanner2=new ZipScanner();
                scanner2.startScan();
                break;
            case FILE_APK:
                ApkFileScanner apkScanner = new ApkFileScanner();
                apkScanner.startScan();
            // TODO: 2016/8/24  
        }
    }
}
