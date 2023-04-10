package com.creativityfactory.swiftserver.utils;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class IOUtilsTest {
    // fromBufferToString
    @Test
    public void testFromBufferToStringWithEmptyBuffer() {
        BufferedReader reader = new BufferedReader(new StringReader(""));
        String expectedOutput = "";
        assertEquals(expectedOutput, IOUtils.fromBufferToString(reader));
    }

    @Test
    public void testFromBufferToStringWithSingleLineBuffer() {
        BufferedReader reader = new BufferedReader(new StringReader("Hello, world!"));
        String expectedOutput = "Hello, world!";
        assertEquals(expectedOutput, IOUtils.fromBufferToString(reader));
    }

    // read file
    @Test
    public void testReadExistingFile() throws IOException {
        String expected = "This is the content of a test file.";
        String actual = IOUtils.readFile("src/test/resources/test-file.txt");
        assertEquals(expected, actual);
    }

    @Test
    public void testReadNonexistentFile() {
        assertThrows(IOException.class, () -> {
            IOUtils.readFile("src/test/resources/nonexistent-file.txt");
        });
    }

    @Test
    public void testReadLargeFile() throws IOException {
        String expected = "test ".repeat(1000000);
        FileWriter writer = new FileWriter("src/test/resources/test-large-file.txt");
        writer.write(expected);
        writer.close();
        String actual = IOUtils.readFile("src/test/resources/test-large-file.txt");
        assertEquals(expected, actual);
    }
    // save data to file
    @Test
    void testSaveDataToFile() throws IOException {
        // Create a temporary file for testing
        File tempFile = File.createTempFile("test", ".txt");

        // Write some test data to the file
        String testData = "Hello, world!";
        IOUtils.saveDataToFile(testData, tempFile.getPath());

        // Read the contents of the file and check if it matches the test data
        BufferedReader reader = new BufferedReader(new FileReader(tempFile));
        String fileContents = reader.readLine();
        reader.close();
        assertEquals(testData, fileContents);
    }

    @Test
    void testSaveDataToFileWithEmptyData() throws IOException {
        // Create a temporary file for testing
        File tempFile = File.createTempFile("test", ".txt");

        // Write an empty string to the file
        IOUtils.saveDataToFile("", tempFile.getPath());

        // Read the contents of the file and check if it is empty
        BufferedReader reader = new BufferedReader(new FileReader(tempFile));
        String fileContents = reader.lines().reduce("", (acc, ele) -> acc + ele);
        reader.close();
        assertEquals("", fileContents);
    }

    @Test
    void testSaveDataToFileWithNonexistentFile() {
        // Try to write to a file that does not exist
        assertThrows(IOException.class, () -> {
            IOUtils.saveDataToFile("Test", "/path/to/nonexistent/file.txt");
        });
    }
}