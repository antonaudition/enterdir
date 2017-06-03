package com.entersekt.dir;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        port(8080);
        get("/alive", (req, res) -> "I'm alive!\n");
        get("/stat/*", PathInfo.stat);
        get("/ls/*", PathInfo.list);
    }

}
