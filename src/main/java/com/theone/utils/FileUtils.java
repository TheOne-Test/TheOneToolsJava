package com.theone.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author mhsu
 * @version 1.0
 * Description:
 * Created 2023-06-21 18:53
 */
public class FileUtils {

    public static void writeStr2File(String fileName, String content) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true))) {
            bufferedWriter.write(content);
        }
    }
}
