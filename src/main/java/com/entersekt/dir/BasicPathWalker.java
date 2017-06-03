package com.entersekt.dir;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;

public class BasicPathWalker
        extends SimpleFileVisitor<Path> {

    private BufferedOutputStream writer;
    private Path path;

    public BasicPathWalker(Path path, BufferedOutputStream outputStream) throws IOException {
        writer = outputStream;
        this.path = path;
    }

    @Override
    public FileVisitResult visitFile(Path file,
                                     BasicFileAttributes attr) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (attr.isSymbolicLink()) {
            builder.append("Symbolic link: ");
        } else if (attr.isRegularFile()) {
            builder.append("Regular file: ");
        } else {
            builder.append("Other: ");
        }
        builder.append(String.format("%s (%d bytes) (%s modified)%n", file.toString(), attr.size(), attr.lastModifiedTime()));
        writer.write(builder.toString().getBytes());
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir,
                                              IOException exc) throws IOException {
        writer.write(String.format("Directory: %s%n", dir).getBytes());
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file,
                                           IOException exc) {
        System.err.println(exc);
        return CONTINUE;
    }

    public void walk() throws IOException {
        Files.walkFileTree(path, this);
        writer.flush();
        writer.close();
    }

}
