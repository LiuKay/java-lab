package com.kay.exception;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * https://github.com/LiuKay/java-lab/blob/master/docs/Java%20%E5%BC%82%E5%B8%B8%E5%A4%84%E7%90%86%E6%9C%80%E4%BD%B3%E5%AE%9E%E8%B7%B5.md
 * This example shows the 9 best practises exception handling in Java. If you run this demo in some
 * IDEs, you can see the compile warnings IDEs show.
 * <p>
 * 1. Clean Up Resources in a Finally Block or Use a Try-With-Resource Statement 2. Prefer Specific
 * Exceptions 3. Document the Exceptions You Specify 4. Throw Exceptions With Descriptive Messages
 * 5. Catch the Most Specific Exception First 6. Don’t Catch Throwable 7. Don’t Ignore Exceptions 8.
 * Don’t Log and Throw 9. Wrap the Exception Without Consuming it
 */
@Log4j2
public class ExceptionHandlingExample {

    //1. Clean Up Resources in a Finally Block or Use a Try-With-Resource Statement
    public void doNotCloseResourceInTry() {
        FileInputStream inputStream;
        try {
            File file = new File("./tmp.txt");
            inputStream = new FileInputStream(file);

            // use the inputStream to read a file

            // do NOT do this
            inputStream.close();
        } catch (IOException e) {
            log.error(e);
        }
    }

    public void closeResourceInFinally() {
        FileInputStream inputStream = null;
        try {
            File file = new File("./tmp.txt");
            inputStream = new FileInputStream(file);

            // use the inputStream to read a file

        } catch (FileNotFoundException e) {
            log.error(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e);
                }
            }
        }
    }

    public void automaticallyCloseResource() {
        File file = new File("./tmp.txt");
        try (FileInputStream inputStream = new FileInputStream(file)) {
            // use the inputStream to read a file

        } catch (IOException e) {
            log.error(e);
        }
    }

    //2. Prefer Specific Exceptions
    public void doNotDoThis() throws Exception {
//    ...
    }

    public void doThis() throws NumberFormatException {
//    ...
    }

    //3. Document the Exceptions You Specify

    /**
     * This method does something extremely useful ...
     *
     * @param input
     * @throws MyBusinessException if ... happens
     */
    public void doSomething(String input) throws MyBusinessException {
//    ...
    }

    //4. Throw Exceptions With Descriptive Messages

    //5. Catch the Most Specific Exception First
    public void catchMostSpecificExceptionFirst() {
        try {
            doSomething("A message");
        } catch (NumberFormatException e) {
            log.error(e);
        } catch (IllegalArgumentException e) {
            log.error(e);
        }
    }

    //6. Don’t Catch Throwable
    public void doNotCatchThrowable() {
        try {
            // do something
        } catch (Throwable t) {
            // FIXME: don't do this!
        }
    }

    //7. Don’t Ignore Exceptions
    public void doNotIgnoreExceptions() {
        try {
            // do something
        } catch (NumberFormatException e) {
            // this will never happen
        }
    }

    public void logAnException() {
        try {
            // do something
        } catch (NumberFormatException e) {
            log.error("This should never happen.", e);
        }
    }

    //8. Don’t Log and Throw

    //9. Wrap the Exception Without Consuming it
    public void wrapException() throws MyBusinessException {
        try {
            // do something
        } catch (NumberFormatException e) {
            throw new MyBusinessException("A message that describes the error.", e);
        }
    }

    static class MyBusinessException extends RuntimeException {

        public MyBusinessException() {
        }

        public MyBusinessException(String message) {
            super(message);
        }

        public MyBusinessException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
