package com.buftas.patternanalyzer;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//This class provides methods to write and read to a csv file
public class CSVFileHandler {

    private File file;
    private String folder;
    private BufferedWriter bw;
    private FileWriter fw;
    private boolean result = false;

    public CSVFileHandler(String fileName, String folder) throws IOException {

        this.folder = folder;
        this.file = new File(folder, fileName + ".csv");
        if (!file.exists())
            result = file.createNewFile();
        if (result) {
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
        } else {
            Log.d("File Creation Debugging", "File Creation Failed...");
        }
    }

    public String readLine() {
        BufferedReader br = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(file));
            if ((sCurrentLine = br.readLine()) != null) {
                return sCurrentLine;
            }
            return "";
        } catch (IOException e) {
            Log.d("IO Debugging", e.toString());
            return "Error";
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                Log.d("IO Debugging", ex.toString());
            }
        }
    }

    public void writeLine(String data) {
        try {
            bw.append(data);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            Log.d("IO Debugging", e.toString());
        }
    }

    public void close() throws IOException {
        bw.close();
    }
}

