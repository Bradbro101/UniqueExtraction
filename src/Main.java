import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 This program reads in a specific file that contains the relationships of multiple destinations. The file's line structure
 is as follows:
    Place_X Place_Y Distance_Between

 This is read in by the program and the output is in prolog. The reason is that I need to automate the input and output,
 to save a lot of time. Therefore, I can sooner focus on my assignment task.
 */

public class Main {
    static String filepath = "C:\\Users\\Bradley Broughton\\OneDrive\\Documents\\Obsidian\\Obsidian Notes\\Undergrad\\Second Year" +
            "\\CS26520 - Artificial Intelligence\\distances.txt";
    static String output_filepath = "C:\\Users\\Bradley Broughton\\OneDrive\\Documents\\Obsidian\\Obsidian Notes\\Undergrad\\Second Year" +
            "\\CS26520 - Artificial Intelligence\\places_and_relationships.txt";
    static ArrayList<String> uniqueValues = new ArrayList<>();
    static ArrayList<String> relationshipsList = new ArrayList<>();
    public static ArrayList<String> values = new ArrayList<>();

    public static void uniqueReadIn() {
        try {
            File file = new File(filepath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                valueSeparation(scanner.nextLine().toLowerCase());
            }
            scanner.close();
            copyUniqueValues();
            updatePlacesToProlog();
        } catch (FileNotFoundException e) {
            System.err.println("File not found.");
        }
        System.out.println(uniqueValues);
    }

    public static void relationships() {
        try {
            File file = new File(filepath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                makeConnected(scanner.nextLine().toLowerCase());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found.");
        }
        System.out.println(relationshipsList);
    }

    public static void makeConnected(String nextLine) {
        String placeX = null;
        String placeY = null;
        int distance;
        int lineIndex = 0;

        StringBuilder place = new StringBuilder();

        for (int i = 0; i < nextLine.length(); i++) {
            char currentChar = nextLine.charAt(i);
            if (Character.isLetter(currentChar)) {
                place.append(currentChar);
            } else {
                lineIndex++;
                if (lineIndex == 1){
                    placeX = String.valueOf(place);
                    place = new StringBuilder();
                } else if (lineIndex == 2) {
                    placeY = String.valueOf(place);
                    place = new StringBuilder();
                }
            }
            if (Character.isDigit(currentChar)) {
                place.append(currentChar);
            }
        }
        distance = Integer.parseInt(place.toString());
        relationshipsList.add("distance(" + placeX + "," + placeY + "," + distance + ").");
    }
    public static void valueSeparation(String nextLine) {
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < nextLine.length(); i++) {
            char currentChar = nextLine.charAt(i);
            if (Character.isLetter(currentChar)){
                word.append(currentChar);
            } else {
                if (!word.isEmpty()){
                    values.add(String.valueOf(word));
                    word = new StringBuilder();
                }
            }
        }
    }

    public static void copyUniqueValues() {
        for(String value: values) {
            if (!uniqueValues.contains(value)) {
                uniqueValues.add(value);
            }
        }
        Collections.sort(uniqueValues);
    }

    public static void updatePlacesToProlog() {
        uniqueValues.replaceAll(s -> "place(" + s + ").");
    }

    public static void outputToFile(){
        try {
            File file = new File(output_filepath);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
            PrintStream printStream = new PrintStream(file);
            for(String place: uniqueValues) {
                printStream.println(place);
            }
            for (String relationship: relationshipsList) {
                printStream.println(relationship);
            }
            printStream.close();
            System.out.println("Wrote to file.");
        } catch (Exception e) {
            System.err.println("Error...");
        }
    }
    public static void main(String[] args) {
        uniqueReadIn();
        relationships();
        outputToFile();
    }
}