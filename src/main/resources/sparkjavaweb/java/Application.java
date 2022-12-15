
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static spark.Spark.*;

public class Application {

    public static final int DEFAULT_PORT = 8080;
    // Public declared dependencies used in controllers.
//    public static UserRepositoryRam quizUserRepo = new UserRepositoryRam();
//    public static EventEmitter emitter = new EventEmitter();
    public static List<String> books;

    public static void main(String[] args) {
        setupFixtures();
        setupDependencies();
        setupSparkWebServer();
        //setupSecurity();
        setupBeforeFilters();
        Router.setUpRoutes(new HandlebarsTemplateEngine());
        setupAfterFilters();
        setupErrors();
    }

    private static void setupSecurity() {
        //Properties props = new Properties();
        //props.setProperty(ApplicationSecurity.CONF_HTTP_HOST,"http://localhost:");
        //props.setProperty(ApplicationSecurity.CONF_HOST_PORT,""+DEFAULT_PORT);
        //props.setProperty(ApplicationSecurity.CONF_FACEBOOK_KEY,ApplicationSecurity.FACEBOOK_KEY);
        //props.setProperty(ApplicationSecurity.CONF_FACEBOOK_SECRET,ApplicationSecurity.FACEBOOK_SECRET);
        //props.setProperty(ApplicationSecurity.CONF_TWITTER_KEY,ApplicationSecurity.TWITTER_KEY);
        //props.setProperty(ApplicationSecurity.CONF_TWITTER_SECRET,ApplicationSecurity.TWITTER_SECRET);
        //SecurityFilter securityFilter = ApplicationSecurity.setupSecurityFilter(props);
        //before("/*", securityFilter);
    }

    private static void setupFixtures() {
        books = Arrays.asList("Nome della Rosa","Guerra e pace");
    }

    private static void setupDependencies() {
        // Nothing to inject.
    }

    private static void setupSparkWebServer() {
        // Configure Spark
        port(getHerokuAssignedPortOr(DEFAULT_PORT));
        staticFiles.location("/public");
        staticFiles.expireTime(600L);
    }

    static int getHerokuAssignedPortOr(int defaultPort) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String port = processBuilder.environment().get("PORT");
        if (port != null) {
            System.out.println("SETUP SERVER PORT FROM ENV:["+port+"]");
            return Integer.parseInt(port);
        }
        System.out.println("SETUP SERVER DEFAULT PORT:["+defaultPort+"]");
        return defaultPort;
    }

    private static void setupAfterFilters() {
        //Set up after-filters (called after each get/post)
        after("*",                   Filters.addGzipHeader);
    }

    private static void setupBeforeFilters() {
        // Set up before-filters (called before each get/post)
//        before("*",                  Filters.addTrailingSlashes);
        before("*",                  Filters.handleLocaleChange);
//        before((request, response) -> {
//            request.session().attribute(Constants.CURRENT_USER,currentUser);
//        });
    }

    private static void setupErrors() {
        exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
            response.body("<html><body><h1>Custom 501 handling</h1></body></html>");
        });

        Spark.internalServerError("<html><body><h1>Custom 500 handling</h1></body></html>");

        // Using string/html
        Spark.notFound((req, res) -> {
            if (req.contentType().equals("application/json")) {
                res.type("application/json");
                return "{\"message\":\"Custom 404\"}";
            } else
                return "<html><body><h1>Custom 404 handling</h1></body></html>";
        });
    }

}
