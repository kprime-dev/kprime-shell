package it.informatelier.kprime.cli.command;

import java.util.HashMap;
import java.util.Map;

public class NewJarCommand extends Command {

    public static final String MVN_BIN_PATH = "mvnBinPath";
    public static final String GROUP_ID = "groupId";
    public static final String ARTIFACT_ID = "artifactId";
    public static final String PACKAGE = "package";
    private static NewJarCommand command = new NewJarCommand();

    private NewJarCommand() {
        setMustArgs(getMustArgsMap());
    }

    private Map<String, String> getMustArgsMap() {
        Map<String, String> required = new HashMap<>();
        required.put(ARTIFACT_ID,"");
        required.put(GROUP_ID,"");
        required.put(PACKAGE,"");
        return required;
    }

    public NewJarCommand(Map<String, String> args) {
        super(command.getCommandLine(args));
        setMustArgs(getMustArgs());
    }

    public static NewJarCommand getInstance() { return command; }

    @Override
    public void runBody() {}

    @Override
    public String getCommandLine(Map<String, String> args) {
        if (!valid(args,getMustArgs())) return null;
        String commandLineTemplate = args.get(MVN_BIN_PATH)  + "mvn archetype:generate " +
                "-DgroupId=%s " +
                "-DartifactId=%s " +
                "-Dpackage=%s " +
                "-DinteractiveMode=false";
        String format = String.format(commandLineTemplate, args.get(GROUP_ID), args.get(ARTIFACT_ID), args.get(PACKAGE));
        setCommandLine(format);
        return format;
    }

    @Override
    public String getLineDescription() {
        return "Executes maven create new jar project.";
    }

    @Override
    public String getName() {
        return "new-jar";
    }


}
