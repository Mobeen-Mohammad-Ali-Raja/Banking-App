package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
//import java.time.format.DateTimeFormatterBuilder;
//import java.time.temporal.ChronoField;

public class Logger {
    private static final String FILE_NAME = "logs.txt";


    public static void log(String message){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:ms");
        String formattedLog = "[" + LocalDateTime.now().format(formatter) + "] " + message + "\n";


        // If needing an extensive time formatting feature use commented features below

        /*
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();

        DateTimeFormatter formatterrrr = builder.appendLiteral("Second is: ")
                .appendFraction(ChronoField.NANO_OF_SECOND, 1, 5, true)
                .appendLiteral(", Day is :")
                .appendValue(ChronoField.DAY_OF_MONTH)
                .appendLiteral(", Month is: ")
                .appendValue(ChronoField.MONTH_OF_YEAR)
                .toFormatter();

                String timeFormatted = LocalDateTime.now().format(formatterrrr);
                System.out.println(timeFormatted +);
         */

        // Writing to file and catching error if it occurs
        try(FileWriter writer = new FileWriter(FILE_NAME, true);){
            writer.write(formattedLog);
        } catch(IOException e){
            System.out.println("Error Message: " + e);
        }

    }

    public static void deleteFile(){

        // Creating a new file object based on "logs.txt" as the file
        File file = new File(FILE_NAME);

        // Try and catch to attempt to delete file, verification of deletion should be handled before calling this method
        if(file.delete()){
            Logger.log("File deleted successfully");
            System.out.println("File deleted successfully");
        } else {
            Logger.log("File could not be deleted");
            System.out.println("File could not be deleted");
        }
    }

    public static void readFile(){

        // Creating a new file object based on "logs.txt" as the file
        File file = new File(FILE_NAME);

        // Try and catch to attempt to read file
        try(Scanner myReader = new Scanner(file)){
            while(myReader.hasNextLine()){
                System.out.println(myReader.nextLine());
            }
        } catch(FileNotFoundException e){
            System.out.println("Error Message: " + e);
        }
    }
}


