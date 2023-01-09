package it.informatelier.kprime.cli;

import it.informatelier.kprime.cli.command.Commandable;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.widget.AutosuggestionWidgets;

import java.io.*;
import java.util.*;


public class Shell {
    private static final Config config = new Config();
    private static final Properties cliResourceProperties = new Properties();
    private static final Properties cliHomeProperties = new Properties();
    private final String propertyAddCommand = "properties-add";

    public static void main(String[] args) {
        Shell shell = new Shell();
        try {
            cliResourceProperties.load(Shell.class.getResourceAsStream("/cli_resource.properties"));

            String cliVersion = cliResourceProperties.getProperty("cli.version");
            String cliGroupId = cliResourceProperties.getProperty("cli.group");
            String cliArtifactId = cliResourceProperties.getProperty("cli.artifact");
            System.out.println("KPRIME CLI [" + cliGroupId + "." + cliArtifactId + "] version [" + cliVersion + "]");
            if (shell.getCliHome() == null) {
                System.err.println("fatal error env KPRIME_HOME not set.");
                return;
            }
            if (! new File(shell.getCliHome() + "cli.properties").canRead()) {
                firstRunSequence(cliVersion, shell.getCliHome() + "cli.properties");
            }
            cliHomeProperties.load(new FileReader(shell.getCliHome()+"cli.properties"));
            System.out.println("KPRIME HOME:[" + shell.getCliHome() + "] with " + cliHomeProperties.size() + " properties.");
            shell.start(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void firstRunSequence(String cliVersion, String cliPropertiesFilePath) throws IOException {
        Properties pros = new Properties();
        Console reader = System.console();
        String serverName = reader.readLine(Commandable.must_arg_server_name+">");
        pros.setProperty(Commandable.must_arg_server_name,serverName);
        String serverAddress = reader.readLine(Commandable.must_arg_address+">");
        pros.setProperty(serverName,serverAddress);
        String userName = reader.readLine(Commandable.must_arg_user_name+">");
        pros.setProperty(Commandable.must_arg_user_name,userName);
        String userPass = reader.readLine(Commandable.must_arg_user_pass+">");
        pros.setProperty(Commandable.must_arg_user_pass,userPass);
        FileOutputStream fos = new FileOutputStream(cliPropertiesFilePath);
        pros.store(fos,"Kprime CLI "+cliVersion);
    }

    private String getCliHome() {
        return System.getenv("KPRIME_HOME");
    }

    private static void printUsage(CommandLineParser parser) {
        System.out.println(cliResourceProperties.getProperty("cli.name")+" version: "+ cliResourceProperties.getProperty("cli.version"));
        System.out.println("  Type 'quit' to terminate the program.");
        System.out.println(parser.getCommandsUsage());
    }

    private void start(String... args) {
        LineReader reader = readerWithOptions(
                List.of("help", "help topic", "help cmd", "quit", "info", "info-context", "log",
                        "properties","properties-add","properties-rem"));
        CommandLineParser parser = new CommandLineParser(config,reader);
        CommandExecutor executor = new CommandExecutor(config);
        if (thereAreArguments(args)) executeSingleCommand(parser, executor, args);
        else askForCommandsUntilQuit(reader, parser, executor);
    }

    private boolean thereAreArguments(String[] args) {
        return args!=null && args.length>0;
    }

    private void askForCommandsUntilQuit(LineReader reader, CommandLineParser parser, CommandExecutor executor) {
        String line;
        while ((line = reader.readLine(getServerNameProperty()+":"+getContextProperty()+">")) != null) {
                if (isExitCommand(line)) {
                    break;
                }
                if (isPropertiesCommand(line)) {
                    printHomeProperties();
                    continue;
                }
                if (isAddPropertiesCommand(line)) {
                    String lineWithoutCommandPrefix = line.substring(propertyAddCommand.length()+1);
                    String[] tokens = lineWithoutCommandPrefix.split("=");
                    cliHomeProperties.setProperty(tokens[0].trim(),tokens[1].trim());
                    saveHomeProperty();
                    continue;
                }
                if (isRemovePropertiesCommand(line)) {
                    String[] tokens = line.split(" ");
                    cliHomeProperties.remove(tokens[1].trim());
                    saveHomeProperty();
                    continue;
                }
                if (isHelpCommand(line)) {
                    printUsage(parser);
                    continue;
                }
                Commandable command = parser.parse(line);
                if (command != null) {
                    command.setMustArgs(Map.of(
                            Commandable.must_arg_context, getContextProperty(), //e.g. "kprime"
                            Commandable.must_arg_address, getAddressProperty()  //e.g. "http://localhost:7000"
                    ));
                    Commandable commandExecuted = executor.execute(command);
                    String executeResult = commandExecuted.getResult();
                    printCommandLineResult(commandExecuted.getCommandLine(), executeResult);
                    printCommandLineOptions(commandExecuted.getOptsArgs());
                }
        }
    }

    private String getAddressProperty() {
        String serverName = cliHomeProperties.getProperty(Commandable.must_arg_server_name, "");
        return cliHomeProperties.getProperty(serverName, "");
    }

    private String getServerNameProperty() {
        return cliHomeProperties.getProperty(Commandable.must_arg_server_name, "");
    }

    private String getContextProperty() {
        return cliHomeProperties.getProperty(Commandable.must_arg_context, "");
    }

    private void printHomeProperties() {
        cliHomeProperties.forEach(Shell::printPair);
    }

    private static void printPair(Object a, Object b) {
        System.out.println("["+a+"]=["+b+"]");
    }

    private LineReader readerWithOptions(List<String> optsArgs) {
        ArgumentCompleter completer = new ArgumentCompleter(
                new StringsCompleter(optsArgs));
        LineReader reader = LineReaderBuilder.builder().completer(completer).build();
        AutosuggestionWidgets autosuggestionWidgets = new AutosuggestionWidgets(reader);
        autosuggestionWidgets.enable();
        return reader;
    }

    private void executeSingleCommand(CommandLineParser parser, CommandExecutor executor, String[] args) {
        System.out.println("Single command execution.");
        String line = buildCommandLineFromArgs(args);
        Commandable command = parser.parse(line);
        Commandable commandExecuted = executor.execute(command);
        if (commandExecuted!=null) {
          printCommandLineResult(commandExecuted.getCommandLine(),commandExecuted.getResult());
        }
    }

    private void printCommandLineOptions(List<String> optsArgs) {
        if (optsArgs!=null) optsArgs.forEach(System.out::println);
    }

    private void printCommandLineResult(String commandLine,String output) {
      System.out.println("\u001B[33m:\\"+commandLine+">\u001B[0m\"");
      System.out.println(output);
    }

    private String buildCommandLineFromArgs(String[] args) {
        StringBuilder line = new StringBuilder();
        for (String arg : args) line.append(arg).append(" ");
        System.out.println(">>>["+line+"]");
        return line.toString();
    }

    private boolean isExitCommand(String command) {
        return command.trim().equalsIgnoreCase("quit");
    }

    private boolean isHelpCommand(String command) {
        return "help".equalsIgnoreCase(command.trim()) || "".equals(command.trim()) ;
    }

    private boolean isPropertiesCommand(String command) {
        return "properties".equalsIgnoreCase(command.trim());
    }

    private boolean isAddPropertiesCommand(String command) {
        return command.trim().startsWith(propertyAddCommand);
    }

    private boolean isRemovePropertiesCommand(String command) {
        return command.trim().startsWith("properties-rem");
    }

    private void saveHomeProperty() {
        try {
            cliHomeProperties.store(new FileWriter(getCliHome()+"cli.properties"), "#");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}