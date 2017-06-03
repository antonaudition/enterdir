package com.entersekt.dir.walker;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;

public class WalkTree extends WhiteWalker {

    class WalkDirectories extends SimpleFileVisitor<Path> {

        private List<Path> dirList;

        WalkDirectories() {
            dirList = Collections.synchronizedList(new ArrayList<Path>());
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            dirList.add(dir);
            return CONTINUE;
        }

        List<Path> sorted() {
            Collections.sort(dirList);
            return dirList;
        }

        void walk(Path path) throws IOException {
            Files.walkFileTree(path, this);
        }
    }

    class WalkFiles extends SimpleFileVisitor<Path> {

        private BufferedOutputStream writer;
        private Path path;

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
            return SKIP_SUBTREE;
        }

        void walk(Path path, BufferedOutputStream outputStream) throws IOException {
            this.path = path;
            this.writer = outputStream;

            Files.walkFileTree(path, this);
        }
    }

    public void walk(Path path, BufferedOutputStream writer) throws IOException {
        WalkDirectories walker = new WalkDirectories();
        Files.walkFileTree(path, walker);
        for (Path dir : walker.sorted()) {
            writer.write(String.format("Directory: %s%n", dir).getBytes());
            new WalkFiles().walk(dir, writer);
        }
        writer.flush();
        writer.close();
    }
}
