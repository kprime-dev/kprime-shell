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

    public static void main(String[] args) {
        Shell shell = new Shell();
        try {
            cliResourceProperties.load(Shell.class.getResourceAsStream("/cli_resource.properties"));
            String cli_home = System.getenv("KPRIME_HOME");
            String cliVersion = cliResourceProperties.getProperty("cli.version");
            String cliGroupId = cliResourceProperties.getProperty("cli.group");
            String cliArtifactId = cliResourceProperties.getProperty("cli.artifact");
            System.out.println("KPRIME CLI ["+cliGroupId+"."+cliArtifactId+"] version ["+ cliVersion +"]");
            if (cli_home==null) {
                System.err.println("fatal error env KPRIME_HOME not set.");
                return;
            }
            cliHomeProperties.load(new FileReader(cli_home+"cli.properties"));
            System.out.println("KPRIME HOME:["+cli_home+"] with "+ cliHomeProperties.size()+" properties.");
            shell.start(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printUsage(CommandLineParser parser) {
        System.out.println(cliResourceProperties.getProperty("mpm.name")+" version: "+ cliResourceProperties.getProperty("mpm.version"));
        System.out.println("Mvn bin path: ["+parser.getMvnBinPath()+"]");
        System.out.println("Home path: ["+config.get("HOME")+"]");
        System.out.println("Usage: java -jar mpm*.jar");
        System.out.println("  it looks for a pom.xml file in current directory.");
        System.out.println();
        System.out.println("  Type 'quit' to terminate the mpm program.");
        System.out.println(parser.getCommandsUsage());
    }

    private void start(String... args) {
        LineReader reader = readerWithOptions(null, List.of("help", "help topic", "help cmd", "quit", "info", "info-context", "log","properties"));
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
        LineReader currentReader = reader;
        while ((line = currentReader.readLine(">")) != null) {
                if (isExitCommand(line)) {
                    break;
                }
                if (isPropertiesCommand(line)) {
                    printHomeProperties();
                    continue;
                }
                if (isHelpCommand(line)) {
                    printUsage(parser);
                    continue;
                }
                Commandable command = parser.parse(line);
                if (command != null) {
                    Commandable commandExecuted = executor.execute(command);
                    String executeResult = commandExecuted.getResult();
                    //currentReader = readerWithOptions(currentReader,List.of("alfa","beta"));
                    printCommandLineResult(commandExecuted.getCommandLine(), executeResult);
                    printCommandLineOptions(commandExecuted.getOptsArgs());
                }
        }
    }

    private void printHomeProperties() {
        cliHomeProperties.forEach(Shell::printPair);
    }

    private static void printPair(Object a, Object b) {
        System.out.println("["+a+"]=["+b+"]");
    }

    private LineReader readerWithOptions(LineReader currentReader,List<String> optsArgs) {
        if (optsArgs==null) return currentReader;
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
      if (commandLine!=null && commandLine.startsWith("mvn"))
        print(output,"[WARNING]","[ERROR]","SUCCESS");
      else
        System.out.println(output);
    }

    private void print(String output,String... filters){
      String[] split = output.split("\n");
      for (String line :split) {
        for (String filter: filters) {
          if (line.contains(filter))
            System.out.println(":"+line);
          }
      }
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

}