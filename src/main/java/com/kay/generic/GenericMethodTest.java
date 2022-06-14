package com.kay.generic;

import lombok.extern.log4j.Log4j2;

/**
 * Generic Method
 */
@Log4j2
public class GenericMethodTest {

    // generic method printArray
    public static <E> void printArray(E[] inputArray) {
        // Display array elements
        for (E element : inputArray) {
            System.out.printf("%s ", element);
        }
        log.info("===");
    }

    public static void main(String[] args) {
        // Create arrays of Integer, Double and Character
        Integer[] intArray = {1, 2, 3, 4, 5};
        Double[] doubleArray = {1.1, 2.2, 3.3, 4.4};
        Character[] charArray = {'H', 'E', 'L', 'L', 'O'};

        log.info("Array integerArray contains:");
        printArray(intArray);   // pass an Integer array

        log.info("\nArray doubleArray contains:");
        printArray(doubleArray);   // pass a Double array

        log.info("\nArray characterArray contains:");
        printArray(charArray);   // pass a Character array
    }
}
