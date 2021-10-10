package up.visulog.cli;

import static spark.Spark.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServeFrontend {
    public static void serve(int port, String data) throws IOException {
        port(port);
        System.out.printf("Server started on http://localhost:%d/", port);
        get("/", (req, res) -> {
            res.header("Content-Type", "text/html");
            return getIndex();
        });

        get("/style.css", (req, res) -> {
            res.header("Content-Type", "text/css");
            return getStyle();
        });

        get("/script.js", (req, res) -> {
            res.header("Content-Type", "application/javascript");
            return getScript();
        });

        get("/data.json", (req, res) -> {
            res.header("Content-Type", "application/json");
            return data;
        });
    }
    
    private static String getIndex() throws IOException {
        Path path = Paths.get("src/main/resources/module.html");

        return Files.readString(path);
    }
    
    private static String getScript() throws IOException {
        Path path = Paths.get("src/main/resources/script.js");

        return Files.readString(path);
    }

    private static String getStyle() throws IOException {
        Path path = Paths.get("src/main/resources/style.css");

        return Files.readString(path);
    }
}
