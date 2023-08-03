package it.informatelier.kprime.cli;

import it.informatelier.kprime.cli.command.*;
import org.jline.reader.LineReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Parser of commands starting from a string.
 */
class CommandLineParser {
    private final Config config;
    private LineReader reader;
    private static Map<String, Commandable> commands;

    CommandLineParser(Config config) {
        this.config = config;
        initCommands();
    }

    List<String> getCommandNameList() {
        return commands.values().stream().map(command -> command.getName()).collect(Collectors.toList());
    }

    String getCommandsUsage() {
        return commands.values().stream()
                .map(x -> String.format("%20s:%50s\n",x.getName() ,x.getLineDescription()))
                .reduce(String::concat)
                .get();
    }

    /**
     * Parse the string to create a new Command.
     * It returns null when unable to parse the command.
     *
     * @param line
     * @param serverRequiredParams
     * @return a new command
     */
    Commandable parse(String line, ServerRequiredParams serverRequiredParams) {
        String[] tokens = line.split(" ");
        String firstToken = tokens[0];
        String commandToken = "";
        String context = "";
        if (firstToken.startsWith("-context=")) {
            commandToken = tokens[1];
            context = firstToken.substring(9);
            line = line.substring(line.indexOf(firstToken)+firstToken.length());
        } else {
            commandToken = firstToken;
            context = serverRequiredParams.getContext();
        }
        //System.out.println("Context ["+context+"] line ["+line+"]");
        Commandable commandable = commands.get(commandToken) != null ? commands.get(commandToken) : new KPPutCommand();
        commandable.setCommandLine(line);
        commandable.setMustArgs(Map.of(
                Commandable.must_arg_context, context, //e.g. "kprime"
                Commandable.must_arg_address, serverRequiredParams.getAddress(),  //e.g. "http://localhost:7000"
                Commandable.must_arg_user_name, serverRequiredParams.getUserName(),
                Commandable.must_arg_user_pass, serverRequiredParams.getUserPass()
        ));
        return commandable;
    }

    private void initCommands() {
        commands = new HashMap<>();
        register(new KPGetCommand());
        register(new KPPostCommand());
        register(new KPPutCommand());
    }

    private void register(Command command) {
        commands.put(command.getName(),command);
    }

//    private Map<String, String> askRequiredArgs(Map<String,String> mustArgKeys) {
//        if (mustArgKeys==null) return null;
//        Map<String,String> args = new HashMap<>();
//        for(String argKey: mustArgKeys.keySet()) {
//            args.put(argKey, ask(argKey,mustArgKeys.get(argKey)));
//        }
//        return args;
//    }

//    private String ask(String question,String defaultArg) {
//        System.out.println(">"+question+" ["+defaultArg+"]?");
//        String line = null;
//        line = reader.readLine(">");
//        if (line==null || line.trim().length()==0) return defaultArg;
//        return line;
//    }
}
