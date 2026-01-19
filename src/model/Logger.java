package model;

import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private static final String FILE_NAME = "logs.txt";


    public static void log(String message){



        try(FileWriter writer = new FileWriter(FILE_NAME, true);){
            writer.write("I like pizza\n");
        } catch(IOException e){
            System.out.println("Error Message: " + e);
        }

    }

    public static void deleteFile(){

    }
}
