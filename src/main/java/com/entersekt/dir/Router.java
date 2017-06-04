package com.entersekt.dir;

import com.entersekt.dir.walker.WalkDir;
import com.entersekt.dir.walker.WalkTree;
import com.entersekt.dir.walker.WhiteWalker;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;

import static spark.Spark.halt;

class Router {
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

    static Route list() {
        return walkerRoute(new WalkDir());
    }

    static Route tree() {
        return walkerRoute(new WalkTree());
    }

    static private Route walkerRoute(WhiteWalker walker) {
        return (Request request, Response response) -> {
            Path path = arrayToPath(request.splat());
            if (!Files.isReadable(path)) {
                throw halt(404, "path does not exist");
            }
            try {
                response.header("Content-Type", "text/plain");
                response.header("Content-Encoding", "gzip");
                walker.walk(path, (new BufferedOutputStream(new GZIPOutputStream(response.raw().getOutputStream()))));
            } catch (IOException e) {
                throw halt(501, printEx(e));
            }
            return "ok";
        };
    }

    static String printEx(IOException e) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        printWriter.flush();
        String trace = writer.toString();

        System.err.println(trace);
        return trace;
    }
}
