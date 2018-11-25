package com.sap.sl.sdk.authoring.samples.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class SamplesUtilities {

	public static File createTempFolder(String parent){
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = dateFormat.format(new Date());
        File uniqueFolder = new File(parent, "sdksamples_" + date + '_' + UUID.randomUUID());
        uniqueFolder.mkdirs();
        return uniqueFolder;
	}

	public static void deleteFolder(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                deleteFolder(child);
            }
        }

        if (!file.delete())
            throw new RuntimeException("Failed to delete: " + file);
    }
}
