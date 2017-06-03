package com.entersekt.dir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        port(8080);
        get("/alive", (req, res) -> "I'm alive!\n");
        get("/stat/*", (req, res) -> {
            Path path = arrayToPath(req.splat());
            Boolean exists = Files.isReadable(path);
            return path.toString() + " exists? " + exists.toString() +"\n";
        });
    }

    private static Path arrayToPath(String[] parts) {
        return Paths.get(Arrays.toString(parts)
                .replace(", ", "/")
                .replace("[", "/")
                .replace("]", ""));
    }
}
