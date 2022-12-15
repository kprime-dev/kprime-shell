package controller;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

import javax.script.Bindings;
import javax.script.SimpleBindings;
import java.util.*;

public class SampleController {

    private static final String EXT = ".hsb";
    public static final String PLAYER_ATT = "player";

    public static ModelAndView listBooks(Request request, Response response) {

        //List<String> books = Application.books;
        List<String> books = Arrays.asList("Nome della Rosa","Guerra e pace");

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("message", "Welcome!");
        model.put("books", books);
        return new ModelAndView(model, "list-books"+ EXT);
    }
}
