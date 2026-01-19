package model;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeFormatterBuilder;
//import java.time.temporal.ChronoField;

public class Logger {
    private static final String FILE_NAME = "logs.txt";


    public static void log(String message){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:ms");
        String formattedLog = "[" + LocalDateTime.now().format(formatter) + "] " + message + "\n";


        //if needing an extensive time formatting feature use commented features below

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

        try(FileWriter writer = new FileWriter(FILE_NAME, true);){
            writer.write(formattedLog);
        } catch(IOException e){
            System.out.println("Error Message: " + e);
        }

    }

    public static void deleteFile(){

    }
}


