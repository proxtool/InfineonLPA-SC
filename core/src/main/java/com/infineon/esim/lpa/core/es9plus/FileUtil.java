package com.infineon.esim.lpa.core.es9plus;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtil {
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    public static void init(Activity activity) {
        if (activity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_WRITE_EXTERNAL_STORAGE);
                }
            }
        }
    }

    public static void writeToSuccessFile(String data) {
        writeToLogFile("succ", data);
    }

    public static void writeToFailFile(String data) {
        writeToLogFile("fail", data);
    }

    private static void writeToLogFile(String subDirectory, String data) {
        if (isExternalStorageWritable()) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String fileName = timeStamp + ".txt";
            File infDirectory = new File(Environment.getExternalStorageDirectory(), ".InfLPA");
            if (!infDirectory.exists()) infDirectory.mkdirs();
            File subDirectoryFile = new File(infDirectory, subDirectory);
            if (!subDirectoryFile.exists()) subDirectoryFile.mkdirs();

            File logFile = new File(subDirectoryFile, fileName);
            FileOutputStream fos = null;

            try {
                fos = new FileOutputStream(logFile);
                fos.write(data.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
