package com.xrwl.owner.Frame.auxiliary;

import java.io.File;

public class DataCleanManager {
    public static void DeleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }

                for (File f : childFile) {
                    DeleteFile(f);
                }
                file.delete();
            }
        }
    }

}
