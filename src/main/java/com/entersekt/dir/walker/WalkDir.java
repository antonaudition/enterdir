package com.entersekt.dir.walker;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;

public class WalkDir extends WhiteWalker {

    class WalkFiles extends SimpleFileVisitor<Path> {

        private BufferedOutputStream writer;
        private Path path;

        WalkFiles(Path path, BufferedOutputStream outputStream) {
            this.writer = outputStream;
            this.path = path;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
            writer.write(printFile(file, attr).getBytes());
            return CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attr) throws IOException {
            if (dir.equals(path)) {
                return CONTINUE;
            }
            if (!(Files.isReadable(dir) && Files.isExecutable(dir))) {
                writer.write(String.format("Directory: %s (%s modified) ACCESS DENIED%n", dir, attr.lastModifiedTime()).getBytes());
                return SKIP_SUBTREE;
            }
            writer.write(printDirectory(dir, attr).getBytes());
            return SKIP_SUBTREE;
        }
    }

    @Override
    public void walk(Path path, BufferedOutputStream outputStream) throws IOException {
        outputStream.write(String.format("Directory: %s%n", path).getBytes());
        try {
            Files.list(path);
        } catch (AccessDeniedException e) {
            outputStream.flush();
            outputStream.close();
            return;
        }
        Files.walkFileTree(path, new WalkFiles(path, outputStream));
        outputStream.flush();
        outputStream.close();
    }
}
