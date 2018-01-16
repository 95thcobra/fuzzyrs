package com.rs.core.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John (FuzzyAvacado) on 1/1/2016.
 */
public class FuzzyFileManager {

    public static List<String[]> getSplitFileLines(File file, String splitString) throws IOException {
        final List<String[]> lines = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            String[] splitLines;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("//")) {
                    continue;
                }
                splitLines = line.split(splitString);
                lines.add(splitLines);
            }
            return lines;
        }
    }

    public static List<String[]> getSplitFileLines(String filePath, String splitString) throws IOException {
        return getSplitFileLines(new File(filePath), splitString);
    }

    public static List<String> getFileLines(File file) throws IOException {
        final List<String> lines = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("//")) {
                    continue;
                }
                lines.add(line);
            }
            return lines;
        }
    }

    public static List<String> getFileLines(String filePath) throws IOException {
        return getFileLines(new File(filePath));
    }
}
