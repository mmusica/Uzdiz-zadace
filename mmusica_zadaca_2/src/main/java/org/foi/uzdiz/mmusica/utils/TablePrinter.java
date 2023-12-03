package org.foi.uzdiz.mmusica.utils;

import org.foi.uzdiz.mmusica.model.PackageType;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TablePrinter {

    public static <T> void printTable(List<T> elements) {
        if (elements == null || elements.isEmpty()) {
            System.out.println("Nema podataka.");
            return;
        }

        T firstElement = elements.get(0);
        Class<?> elementClass = firstElement.getClass();
        Field[] fields = elementClass.getDeclaredFields();

        // Calculate maximum column widths based on the length of the longest value in each column
        int[] columnWidths = calculateMaxColumnWidths(fields, elements);

        // Print column headers with adjusted widths
        printHeader(fields, columnWidths);

        // Print separator line
        printSeparatorLine(columnWidths);

        // Print rows with adjusted widths
        elements.forEach(element -> printRow(element, fields, columnWidths));
    }

    private static <T> int[] calculateMaxColumnWidths(Field[] fields, List<T> elements) {
        int[] columnWidths = new int[fields.length];

        // Initialize column widths with the length of the column names
        for (int i = 0; i < fields.length; i++) {
            columnWidths[i] = fields[i].getName().length();
        }

        // Update column widths based on the length of the longest value in each column
        elements.forEach(element -> {
            for (int i = 0; i < fields.length; i++) {
                try {
                    fields[i].setAccessible(true);
                    Object value = fields[i].get(element);
                    if (value != null) {
                        int valueLength = value.toString().length();
                        columnWidths[i] = Math.max(columnWidths[i], valueLength);
                    }
                    if(fields[i].getType() == PackageType.class){
                        columnWidths[i] = fields[i].getName().length();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        // Add extra padding
        for (int i = 0; i < columnWidths.length; i++) {
            columnWidths[i] += 2; // Add extra padding for aesthetics
        }

        return columnWidths;
    }

    private static void printHeader(Field[] fields, int[] columnWidths) {
        for (int i = 0; i < fields.length; i++) {
            System.out.printf("%-" + columnWidths[i] + "s", fields[i].getName());
        }
        System.out.println();
    }

    private static void printSeparatorLine(int[] columnWidths) {
        for (int width : columnWidths) {
            for (int i = 0; i < width; i++) {
                System.out.print("-");
            }
        }
        System.out.println();
    }

    private static <T> void printRow(T element, Field[] fields, int[] columnWidths) {
        for (int i = 0; i < fields.length; i++) {
            try {
                fields[i].setAccessible(true);
                Object value = fields[i].get(element);
                if (fields[i].getType() == java.time.LocalDateTime.class) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
                    LocalDateTime dateValue = (LocalDateTime) value;
                    System.out.printf("%-"+columnWidths[i]+"s", dateValue.format(formatter)+ " ");

                }else if(fields[i].getType() == PackageType.class){
                    PackageType packageTypeValue = (PackageType) value;
                    System.out.printf("%-"+columnWidths[i]+"s", packageTypeValue.getOznaka());
                }else {
                    System.out.printf("%-" + columnWidths[i] + "s", value);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

}