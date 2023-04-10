package com.creativityfactory.swiftserver.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

/**
 * Utility class for input/output operations.
 */
public class IOUtils {
    /**
     * Converts the contents of a BufferedReader object to a string.
     *
     * @param reader a BufferedReader object
     * @return a string containing the contents of the BufferedReader object
     */
    public static String fromBufferToString(BufferedReader reader) {
        Stream<String> stream = reader.lines();

        return stream.reduce("", (acc, ele) -> acc + ele);
    }

    /**
     * Reads the contents of a file at the specified path and returns it as a string.
     *
     * @param path the path to the file to read
     * @return a string containing the contents of the file
     * @throws IOException if there is an error reading the file
     */
    public static String readFile(String path) throws IOException {
        File file = new File(path);

        BufferedReader reader = new BufferedReader
                (new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        String lines = reader.lines().reduce("", (acc, ele) -> acc + ele);
        reader.close();

        return lines;
    }

    /**
     * Saves a string of data to a file at the specified path.
     * @param data the data to write to the file
     * @param path the path to the file to write to
     * @throws IOException if there is an error writing to the file
     */
    public static void saveDataToFile(String data, String path) throws IOException {
        File file = new File(path);
        FileWriter myWriter = new FileWriter(file, false);
        myWriter.write(data);
        myWriter.close();
    }
}


