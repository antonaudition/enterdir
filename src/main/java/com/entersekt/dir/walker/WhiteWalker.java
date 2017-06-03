package com.entersekt.dir.walker;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public abstract class WhiteWalker {
    public abstract void walk(Path path, BufferedOutputStream outputStream) throws IOException;

    String printFile(Path file, BasicFileAttributes attr) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (attr.isSymbolicLink()) {
            builder.append("  Symbolic link: ");
        } else if (attr.isRegularFile()) {
            builder.append("  Regular file: ");
        } else {
            builder.append("  Other: ");
        }
        Files.getOwner(file);
        builder.append(String.format("./%-80s (%d bytes) (%s owner) (%s modified)%n", file.getFileName().toString(), attr.size(), Files.getOwner(file), attr.lastModifiedTime()));
        return builder.toString();
    }

    String printDirectory(Path dir) {
        return String.format("Directory: %s%n", dir);
    }

    String printDirectory(Path dir, BasicFileAttributes attr) throws IOException {
        return String.format("Directory: %-80s (%s modified)%n", dir, attr.lastModifiedTime());
    }

}
