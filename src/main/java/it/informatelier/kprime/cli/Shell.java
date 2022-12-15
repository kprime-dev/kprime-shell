package it.informatelier.kprime.cli;

import it.informatelier.kprime.cli.command.Commandable;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.StringsCompleter;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;


public class Shell {
    private static Config config = new Config();
    private static Map<String,String> localDeps;
    private static Properties mpmProperties = new Properties();
    private static Properties envProperties = new Properties();

    public static void main(String[] args) {
        Shell shell = new Shell();
        try {
            mpmProperties.load(Shell.class.getResourceAsStream("/mpm.properties"));
            String mpm_home = System.getenv("KPRIME_HOME");
            if (mpm_home==null) {
                System.err.println("fatal error env KPRIME_HOME not set.");
                return;
            }
            if (mpm_home!=null) envProperties.load(new FileReader(mpm_home+"env.properties"));
            System.out.println("KP HOME:["+mpm_home+"] with "+envProperties.size()+" properties.");
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
        System.out.println(mpmProperties.getProperty("mpm.name")+" version: "+mpmProperties.getProperty("mpm.version"));
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
        Project project = openPomFile();
        localDeps = new HashMap<>();
        LineReader reader = LineReaderBuilder.builder().build();
        listenForConsoleCommand(reader, project, args);
    }

    private LineReader getNewConsoleReader() throws IOException {
        LineReader reader = LineReaderBuilder.builder().build();
        return reader;
    }

    private void listenForConsoleCommand(LineReader reader, Project project, String... args) throws IOException {
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
        while ((line = reader.readLine(">")) != null) {
            if (isExitCommand(line)) { break;}
            if (isHelpCommand(line)) { printUsage(parser); continue; }
            Commandable command = parser.parse(line);
            if (command!=null) {
              printCommandLineResult(command.getCommandLine(),executor.execute(command));
            }
        }
    }

    private void executeSingleCommand(CommandLineParser parser, CommandExecutor executor, String[] args) {
        System.out.println("Single command execution.");
        String line = buildCommandLineFromArgs(args);
        Commandable command = parser.parse(line);
        if (command!=null) {
          printCommandLineResult(command.getCommandLine(),executor.execute(command));
        }
        return;
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
        return "quit".equalsIgnoreCase(command);
    }

    private boolean isHelpCommand(String command) {
        return "help".equalsIgnoreCase(command) || "usage".equalsIgnoreCase(command) || "".equals(command) ;
    }

    private boolean isLayoutCommand(String command) {
        return "layout".equals(command);
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

    private Project openPomFile() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("pom.xml");
        } catch (FileNotFoundException e) {
            return null;
        }
        XMLStreamReader reader = silentGetXmlStreamReader(fis);
        return parseProjectTokens(reader);
    }

    private Map<String,String> openLocalDepdendencyMap(String localRepoPath) {
        Path directory = Paths.get(localRepoPath);
        Map<String,String> all = new HashMap<>();
        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    if (file.getFileName().toString().endsWith("jar")) {
                        System.out.print("#");
                        all.put("add-dependency "+file.getFileName().toString(),file.getParent().toString());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
            System.out.print(".");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return all;
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