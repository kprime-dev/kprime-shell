package it.nipe.garage.adapter.shell;

import it.nipe.garage.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

public class ShellApplication {

    public static void main(String[] args) {
        Application app = new Application();
        System.out.println(String.format("App %s version %s", app.getAppName(),app.getAppVersion()));
        String line = System.console().readLine();
        while(line!=null && !line.equals("quit")) {
            System.out.println(">"+line);
            System.out.println(parse(app,line));
            line = System.console().readLine();
        }

    }

    private static String parse(Application app, String line) {
        String result = "";
        List<String> tokens = tokenize(line);

        /*
        // posting: <user name> -> <message>
        if (tokens.size()==3 && tokens.get(1).equals("->")) {
            User author = app.getUser(tokens.get(0));
            Message message =  app.getMessage(tokens.get(2));
            app.post(author,message);
        }
        // following: <user name> follows <another user>
        if (tokens.size()==3 && tokens.get(1).equals("follows")) {
            User follower = app.getUser(tokens.get(0));
            User leader = app.getUser(tokens.get(2));
            follower.canViewTimelineOf(leader);
        }
    */
        return result;
    }

    private static List<String> tokenize(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line);
        List<String> tokens = new ArrayList<>();
        while(tokenizer.hasMoreElements()) {
            tokens.add(tokenizer.nextToken());
        }
        return tokens;
    }

}
