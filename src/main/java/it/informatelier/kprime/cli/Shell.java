package it.informatelier.kprime.cli;

import it.informatelier.kprime.cli.command.Commandable;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.widget.AutosuggestionWidgets;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class Shell {
    private static final Config config = new Config();
    private static final Properties cliResourceProperties = new Properties();
    private static final Properties cliHomeProperties = new Properties();
    private enum commandLabels {
        PROPERTIES("properties"),
        PROPERTY_SET("property-set"),
        PROPERTY_REM("property-rem"),
        QUIT("quit"),
        LOG("log"),
        CONTEXTS("contexts"),
        ADD_CONTEXTS("add-context"),
        REM_CONTEXTS("rem-context"),
        CONTEXT("context"),
        HELP("help"),
        HELP_CMD("help cmd"),
        HELP_TOPIC("help topic"),
        INFO_CONTEXT("info-context"),
        INFO_CLI("info-cli"),
        INFO("info");

        private final String label;

        commandLabels (String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        static public List<String> getLabels() {
            return Arrays.stream(commandLabels.values())
                    .map(commandLabels::getLabel).collect(Collectors.toList());

        }

        @Override
        public String toString() {
            return getLabel();
        }
    };

    public static void main(String[] args) {
        Shell shell = new Shell();
        try {
            cliResourceProperties.load(Shell.class.getResourceAsStream("/cli_resource.properties"));

            String cliVersion = cliResourceProperties.getProperty("cli.version");
            String cliGroupId = cliResourceProperties.getProperty("cli.group");
            String cliArtifactId = cliResourceProperties.getProperty("cli.artifact");
            if (shell.getCliHome() == null) {
                System.err.println("fatal error env KPRIME_HOME not set.");
                return;
            }
            String cliPropertiesFilePath = shell.getCliHome() + "cli.properties";
            if (! new File(cliPropertiesFilePath).canRead()) {
                firstRunSequence(cliVersion, shell.getCliHome() + "cli.properties");
            }
            cliHomeProperties.load(new FileReader(shell.getCliHome()+"cli.properties"));
            String kpUser = cliHomeProperties.getProperty(Commandable.must_arg_user_name);
            if (kpUser==null || kpUser.isEmpty()) askUserPass(cliVersion,cliPropertiesFilePath,cliHomeProperties);
            shell.start(cliResourceProperties,args);
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

    private static void askUserPass(String cliVersion, String cliPropertiesFilePath, Properties cliHomeProperties) throws IOException {
        Console reader = System.console();
        String userName = reader.readLine(Commandable.must_arg_user_name+">");
        cliHomeProperties.setProperty(Commandable.must_arg_user_name,userName);
        String userPass = reader.readLine(Commandable.must_arg_user_pass+">");
        cliHomeProperties.setProperty(Commandable.must_arg_user_pass,userPass);
        FileOutputStream fos = new FileOutputStream(cliPropertiesFilePath);
        cliHomeProperties.store(fos,"Kprime CLI "+cliVersion);
    }

    private String getCliHome() {
        return System.getenv("KPRIME_HOME");
    }

    private static void printUsage(CommandLineParser parser) {
        System.out.println(cliResourceProperties.getProperty("cli.name")+" version: "+ cliResourceProperties.getProperty("cli.version"));
        System.out.println("  Type 'quit' to terminate the program.");
        System.out.println("  Type one of these:");
        System.out.println(parser.getCommandsUsage());
        System.out.println("  Any other command will be parsed by KPrime server.");
    }

    private void start(Properties cliResourceProperties, String... args) {
        CommandLineParser parser = new CommandLineParser(config);
        CommandExecutor executor = new CommandExecutor(config);

        LineReader reader = LineReaderBuilder
                .builder()
                .completer(new AggregateCompleter(
                        new ArgumentCompleter(new StringsCompleter(
                                    commandLabels.getLabels()
                        )),
                        new RemoteCompleter(parser, executor, ServerRequiredParams.fromProperties(cliHomeProperties))
                )).build();
        AutosuggestionWidgets autosuggestionWidgets = new AutosuggestionWidgets(reader);
        autosuggestionWidgets.enable();

        ServerRequiredParams serverRequiredParams = ServerRequiredParams.fromProperties(cliHomeProperties);
        if (thereAreArguments(args)) executeSingleCommand(parser, executor, args, serverRequiredParams);
        else askForCommandsUntilQuit(reader, parser, executor, serverRequiredParams,cliResourceProperties);
    }

    private boolean thereAreArguments(String[] args) {
        return args!=null && args.length>0;
    }

    private void askForCommandsUntilQuit(
            LineReader reader,
            CommandLineParser parser,
            CommandExecutor executor,
            ServerRequiredParams serverRequiredParams,
            Properties cliResourceProperties) {
        String line;
        while ((line = reader.readLine(getPrompt(serverRequiredParams))) != null) {
                if (isExitCommand(line)) break;
                else if (isPropertiesCommand(line)) printHomeProperties();
                else if (isSetPropertyCommand(line)) setPropertiesAction(line,serverRequiredParams);
                else if (isRemovePropertiesCommand(line)) remPropertiesAction(line,serverRequiredParams);
                else if (isHelpCommand(line)) printUsage(parser);
                else if (isInfoCliCommand(line)) printInfoCli(cliResourceProperties);
                else executeCommandAction(parser, executor, line, serverRequiredParams);
        }
    }

    private static String getPrompt(ServerRequiredParams serverRequiredParams) {
        return serverRequiredParams.getServerName() + ":" + serverRequiredParams.getContext() + ">";
    }

    private void executeSingleCommand(
            CommandLineParser parser,
            CommandExecutor executor,
            String[] args,
            ServerRequiredParams serverRequiredParams) {
        System.out.println("Single command execution.");
        String line = buildCommandLineFromArgs(args);
        Commandable command = parser.parse(line,serverRequiredParams);
        if (command != null) {
            Commandable commandExecuted = executor.execute(command);
            if (commandExecuted != null) {
                printCommandLineResult(commandExecuted.getCommandLine(), commandExecuted.getResult());
            }
        }
    }

    private void executeCommandAction(
            CommandLineParser parser,
            CommandExecutor executor,
            String line,
            ServerRequiredParams serverRequiredParams) {
        Commandable command = parser.parse(line,serverRequiredParams);
        if (command != null) {
            Commandable commandExecuted = executor.execute(command);
            String executeResult = commandExecuted.getResult();
            printCommandLineResult(commandExecuted.getCommandLine(), executeResult);
            printCommandLineOptions(commandExecuted.getOptsArgs());
        }
    }

    private void remPropertiesAction(String line, ServerRequiredParams serverRequiredParams) {
        String[] tokens = line.split(" ");
        String key = tokens[1].trim();
        cliHomeProperties.remove(key);
        saveHomeProperty();
        if (key.equals(Commandable.must_arg_context)) serverRequiredParams.setContext("");
    }

    private void setPropertiesAction(String line, ServerRequiredParams serverRequiredParams) {
        String lineWithoutCommandPrefix = line.substring(commandLabels.PROPERTY_SET.label.length()+1);
        String[] tokens = lineWithoutCommandPrefix.split("=");
        String key = tokens[0].trim();
        String value = tokens[1].trim();
        cliHomeProperties.setProperty(key, value);
        saveHomeProperty();
        if (key.equals(Commandable.must_arg_context)) serverRequiredParams.setContext(value);
    }

    private void printInfoCli(Properties cliResourceProperties) {
        for(Object key : cliResourceProperties.keySet()) {
            System.out.println(key+"="+cliResourceProperties.get(key));
        }
        System.out.println("ENVIRONMENT:");
        Map<String, String> clienv = System.getenv();
        for(String key : clienv.keySet()) {
            System.out.println(key+"="+clienv.get(key));
        }
    }

    private void printHomeProperties() {
        cliHomeProperties.forEach(Shell::printPair);
    }

    private static void printPair(Object a, Object b) {
        System.out.println("["+a+"]=["+b+"]");
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
        return command.trim().equalsIgnoreCase(commandLabels.QUIT.label);
    }

    private boolean isHelpCommand(String command) {
        return commandLabels.HELP_CMD.label.equalsIgnoreCase(command.trim()) || "".equals(command.trim()) ;
    }

    private boolean isInfoCliCommand(String command) {
        return commandLabels.INFO_CLI.label.equalsIgnoreCase(command.trim()) || "".equals(command.trim()) ;
    }

    private boolean isPropertiesCommand(String command) {
        return commandLabels.PROPERTIES.label.equalsIgnoreCase(command.trim());
    }

    private boolean isSetPropertyCommand(String command) {
        return command.trim().startsWith(commandLabels.PROPERTY_SET.getLabel());
    }

    private boolean isRemovePropertiesCommand(String command) {
        return command.trim().startsWith(commandLabels.PROPERTY_REM.getLabel());
    }

    private void saveHomeProperty() {
        try {
            cliHomeProperties.store(new FileWriter(getCliHome()+"cli.properties"), "#");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}