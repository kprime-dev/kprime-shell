

import controller.SampleController;
//        import it.nipe.quizsocial.adapter.web.controller.QuizPostController;
//        import it.nipe.quizsocial.adapter.web.controller.QuizTimelineController;
//        import it.nipe.quizsocial.adapter.web.controller.QuizTryController;

import spark.template.handlebars.HandlebarsTemplateEngine;

import static spark.Spark.*;

public class Router {


    static void setUpRoutes(HandlebarsTemplateEngine templateEngine) {
        get("/hello", (req, res) -> "Hello World");
        path("/books", () -> {
            get("/hello", (req, res) -> "Hello World");
            get("/inventory", (req, res) -> SampleController.listBooks(req, res), templateEngine);
            //get("/fight/:book-id/:page-id", (req, res) -> GameBookController.fight(req, res), templateEngine);
        });
    }
}
