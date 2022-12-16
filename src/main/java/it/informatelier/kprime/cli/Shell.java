package it.informatelier.kprime.cli;

import it.informatelier.kprime.cli.command.Commandable;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.widget.AutosuggestionWidgets;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.*;


public class Shell {
    private static Config config = new Config();
    private static Map<String,String> localDeps;
    private static Properties cliResourceProperties = new Properties();
    private static Properties cliHomeProperties = new Properties();

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
            if (cli_home!=null) cliHomeProperties.load(new FileReader(cli_home+"cli.properties"));
            System.out.println("KPRIME HOME:["+cli_home+"] with "+ cliHomeProperties.size()+" properties.");
            shell.start(args);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    Project parseProjectTokens(XMLStreamReader reader) {
        Project project = new Project();
        if (reader==null) return project;
        String lastToken = null;
        try {
            while (reader.hasNext()) {
                reader.next();
                if (reader.isStartElement()) {
                    lastToken = getLastToken(reader, project);
                }
                if (reader.hasText()) {
                    setTokenValue(reader, project, lastToken);
                }
            }
        } catch (XMLStreamException e) {
            return new Project();
        }
        System.out.println("POM with "+project.getTokens().size()+" tokens read.");
        return project;
    }

    XMLStreamReader getXmlStreamReader(InputStream stream) throws XMLStreamException {
        XMLInputFactory xmlInFact = XMLInputFactory.newInstance();
        return xmlInFact.createXMLStreamReader(stream);
    }

    private static void printUsage(CommandLineParser parser) {
        System.out.println(cliResourceProperties.getProperty("mpm.name")+" version: "+ cliResourceProperties.getProperty("mpm.version"));
        System.out.println("Mvn bin path: ["+parser.getMvnBinPath()+"]");
        System.out.println("Home path: ["+config.get("HOME")+"]");
        System.out.println("Locals deps: ["+localDeps.size()+"]");
        System.out.println("Usage: java -jar mpm*.jar");
        System.out.println("  it looks for a pom.xml file in current directory.");
        System.out.println("");
        System.out.println("  Type 'quit' to terminate the mpm program.");
        System.out.println(parser.getCommandsUsage());
    }

    private void start(String... args) throws IOException, XMLStreamException {
        localDeps = new HashMap<>();
        LineReader reader = readerWithOptions(List.of("help", "help topic", "help cmd", "quit", "info", "info-context", "log","properties"));
        listenForConsoleCommand(reader, args);
    }

    private void listenForConsoleCommand(LineReader reader, String... args) throws IOException {
        CommandLineParser parser = new CommandLineParser(config,reader);
        addCompletorsToConsole(reader, parser);
        CommandExecutor executor = new CommandExecutor(config);
        if (thereAreArguments(args)) executeSingleCommand(parser, executor, args);
        else askForCommandsUntilQuit(reader, parser, executor);
    }

    private boolean thereAreArguments(String[] args) {
        return args!=null && args.length>0;
    }

    private void askForCommandsUntilQuit(LineReader reader, CommandLineParser parser, CommandExecutor executor) throws IOException {
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
//                if (!commandExecuted.getOptsArgs().isEmpty())
//                currentReader = readerWithOptions(commandExecuted.getOptsArgs());
                    //currentReader = readerWithOptions(List.of("alfa","beta"));
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
        return;
    }

    private void printCommandLineOptions(List<String> optsArgs) {
        optsArgs.forEach(System.out::println);
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
        String line = "";
        for (int i=0;i<args.length;i++) line += args[i]+" ";
        System.out.println(">>>["+line+"]");
        return line;
    }

    private void addCompletorsToConsole(LineReader reader, CommandLineParser commandLineParser) {
        if (commandLineParser == null) return;
        if (reader == null) return;
        ArgumentCompleter argumentCompleter = new ArgumentCompleter(
                new StringsCompleter(commandLineParser.getCommandNameList()),
                new StringsCompleter(localDeps.keySet())
        );
        reader = LineReaderBuilder.builder().completer(argumentCompleter).build();
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

    private XMLStreamReader silentGetXmlStreamReader(FileInputStream fis) {
        if (fis==null) return null;
        XMLStreamReader reader = null;
        try {
            reader = getXmlStreamReader(fis);
        } catch (XMLStreamException e) {
            // silent
        }
        return reader;
    }

    private void setTokenValue(XMLStreamReader reader, Project project, String lastToken) {
        if (hasTokenValue(reader, lastToken)) {
            project.addValue(lastToken, reader.getText());
        }
    }

    private String getLastToken(XMLStreamReader reader, Project project) {
        String lastToken;

        project.addToken(reader.getLocalName());
        lastToken = reader.getLocalName();
        return lastToken;
    }

    private boolean hasTokenValue(XMLStreamReader reader, String lastToken) {
        return lastToken != null && reader.isCharacters() && !reader.isWhiteSpace();
    }

}