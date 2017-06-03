package com.entersekt.dir.walker;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;

public class WalkBasic
        extends SimpleFileVisitor<Path> {

    private BufferedOutputStream writer;
    private Path path;

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (attr.isSymbolicLink()) {
            builder.append("Symbolic link: ");
        } else if (attr.isRegularFile()) {
            builder.append("Regular file: ");
        } else {
            builder.append("Other: ");
        }
        Files.getOwner(path);
        builder.append(String.format("%s (%d bytes) (%s owner) (%s modified)%n", file.toString(), attr.size(), Files.getOwner(file), attr.lastModifiedTime()));
        writer.write(builder.toString().getBytes());
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        writer.write(String.format("Directory: %s%n", dir).getBytes());
        return CONTINUE;
    }

    public void walk(Path path, BufferedOutputStream outputStream) throws IOException {
        writer = outputStream;
        this.path = path;
        Files.walkFileTree(path, this);
        writer.flush();
        writer.close();
    }

}
