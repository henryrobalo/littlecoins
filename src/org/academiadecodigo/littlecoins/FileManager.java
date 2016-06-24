package org.academiadecodigo.littlecoins;

import java.io.*;

/**
 * Created by henry on 24/06/2016.
 */
public class FileManager {

    public static final String filePath = "resources/myfile.txt";

    /**
     * this method read file from resources.
     *
     * @return file content in a string
     * @throws FileNotFoundException
     */
    public static String readFile() throws FileNotFoundException {

        FileReader fr = new FileReader(filePath);
        BufferedReader bf = new BufferedReader(fr);

        String stringFromFile = "";

        try {
            String line = bf.readLine();
            while (line != null) {
                stringFromFile = stringFromFile+ line +"\n";
                line = bf.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringFromFile;

    }





}
