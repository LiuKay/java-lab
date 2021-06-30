package com.kay.io.file;

import java.io.IOException;
import java.nio.file.*;
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
    }

    static class MyFileVisitor extends SimpleFileVisitor<Path> {

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            System.out.println(file.getFileName());
            return super.visitFile(file, attrs);
        }
    }
}
