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

    CommandLineParser(Config config, LineReader reader) {
        this.config = config;
        this.reader = reader;
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
     * May return null if unable to parse the command.
     *
     * @param line
     * @return a new command
     */
    Commandable parse(String line) {
        String firstToken = line.split(" ")[0];
        Commandable commandable = commands.get(firstToken) != null ? commands.get(firstToken) : new MvnCommand(line);
        commandable.setCommandLine(line);
        Map<String,String> mustArgs = askRequiredArgs(commandable.getMustArgs());
        completeEnvironment(mustArgs);
        commandable.setEnvironment(mustArgs);
        commandable.fillWithArgs(mustArgs);
        return commandable;
    }

    private void initCommands() {
        this.commands = new HashMap<>();
        register(NewJarCommand.getInstance());
        register(NewWarCommand.getInstance());
        register(NewDropwizardCommand.getInstance());
        register(new ShellCommand());
        register(new PrintCommand());
        register(new QueryCommand());
        register(new InitCommand());
        register(new InitSparkjavaCommand());
        register(new AddJava8Command());
        register(new AddJettyCommand());
        register(new AddJUnit4Command());
        register(new AddSkinCommand());
        register(new AddPdfBoxCommand());
        register(new AddH2Command());
        register(new AddFreemarkerCommand());
        register(new AddSparkjavaCommand());
        register(new AddFatJarCommand());
        register(new InitDropwizardCommand());
        register(new InitSpringbootCommand());
        register(new GistJUnitCommand());
        register(new GitJtogglCommand());
        register(new AddSiteCommand());
        register(new InstallCommand());
        register(new InitGarageCommand());
        register(new AddSlf4jCommand());
        register(new InitSparkjavaWebCommand());
        register(new InitJava9());
        register(new AddToolchains());
        register(new AddDependencyCommand());
        register(new InitKotlinCommand());
    }

    private void register(Command command) {
        this.commands.put(command.getName(),command);
    }
    public String getMvnBinPath() {
        return config.get("MVN_HOME")+"/bin /";
    }

    private void completeEnvironment(Map<String, String> mustArgs) {
        if (mustArgs==null) return;
        mustArgs.put( "mvnBinPath", getMvnBinPath());
    }

    private Map<String, String> askRequiredArgs(Map<String,String> mustArgKeys) {
        if (mustArgKeys==null) return null;
        Map<String,String> args = new HashMap<>();
        for(String argKey: mustArgKeys.keySet()) {
            args.put(argKey, ask(argKey,mustArgKeys.get(argKey)));
        }
        return args;
    }

    private String ask(String question,String defaultArg) {
        System.out.println(">"+question+" ["+defaultArg+"]?");
        String line = null;
        line = reader.readLine(">");
        if (line==null || line.trim().length()==0) return defaultArg;
        return line;
    }
}
