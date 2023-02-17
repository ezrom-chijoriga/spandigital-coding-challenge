package com.spandigital.codingchallenge.ezrom.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static List<String> readFile(String file) throws IOException {
        List<String> fileItems = new ArrayList<>();

        String inputLine;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        while ((inputLine = bufferedReader.readLine()) != null) {
            fileItems.add(inputLine);
        }
        bufferedReader.close();
        return fileItems;
    }
}
