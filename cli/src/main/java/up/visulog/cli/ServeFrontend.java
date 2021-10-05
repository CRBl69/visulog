package up.visulog.cli;

import static spark.Spark.*;

public class ServeFrontend {
    public static void serve(int port) {
        port(port);
        System.out.printf("Server started on http://localhost:%d/", port);
        get("/", (req, res) -> {
            return "test";
        });
    }
}
