package com.entersekt.dir;

import spark.Request;
import spark.Response;
import spark.Route;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static spark.Spark.halt;

class PathInfo {
    private static Path arrayToPath(String[] parts) {
        return Paths.get(Arrays.toString(parts)
                .replace(", ", "/")
                .replace("[", "/")
                .replace("]", ""));
    }

    static Route stat = (Request request, Response response) -> {
        Path path = arrayToPath(request.splat());
        Boolean exists = Files.isReadable(path);
        return path.toString() + " exists? " + exists.toString() + "\n";
    };

    static Route list = (Request request, Response response) -> {
        Path path = arrayToPath(request.splat());
        if (!Files.isReadable(path)) {
            throw halt(404, "path does not exist");
        }
        try {
            response.raw().setContentType("text/plain");
            BasicPathWalker pf = new BasicPathWalker(path, new BufferedOutputStream(response.raw().getOutputStream()));
            pf.walk();
        } catch (IOException e) {
            throw halt(501, e.getMessage());
        }
        return "ok";
    };
}
