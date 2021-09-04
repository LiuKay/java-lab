package com.kay.io.file;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;

public class PathDemo {

    public static void main(String[] args) throws IOException {
        Path toAbsolutePath = Paths.get("").toAbsolutePath();
        System.out.println("Current path is :" + toAbsolutePath);

        Path path = Paths.get("src/main/java/com/kay////io").normalize(); //  normalize for ///
        Path absolutePath = path.toAbsolutePath();
        System.out.println("absolutePath:" + absolutePath);


        System.out.println("Root:" + absolutePath.getRoot());
        System.out.println("Parent:" + absolutePath.getParent());

        //visit dictionary
        Files.walkFileTree(absolutePath, new MyFileVisitor());

        createFile();
    }

    /**
     * if the test.txt parent path doesn't exist, throw NoSuchFileException
     *
     * @throws IOException
     */
    public static void createFile() throws IOException {
//        Path dictionary = Paths.get("test");
//        if (!Files.exists(dictionary)) {
//            Files.createDirectories(dictionary);
//        }
        Path testPath = Paths.get("test/test.txt");
        Files.write(testPath, "hello".getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
    }


    static class MyFileVisitor extends SimpleFileVisitor<Path> {

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            System.out.println(file.getFileName());
            return super.visitFile(file, attrs);
        }
    }
}
