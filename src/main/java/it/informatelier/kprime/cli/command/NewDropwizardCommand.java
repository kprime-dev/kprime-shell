package it.informatelier.kprime.cli.command;

import java.util.HashMap;
import java.util.Map;

/**
 * Command for create a new war.
 */
final public class NewDropwizardCommand extends Command {

    public static final String MVN_BIN_PATH = "mvnBinPath";
    public static final String NAME = "name";

    private static final NewDropwizardCommand command = new NewDropwizardCommand();

    private NewDropwizardCommand() {
        Map<String, String> required = new HashMap<>();
        required.put(NAME,"");
        setMustArgs(required);
    }

    public NewDropwizardCommand(Map<String, String> args) {
        super(command.getCommandLine(args));
    }

    public static synchronized NewDropwizardCommand getInstance() { return command; }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @Override
    public void runBody() {}

    @Override
    public String getCommandLine(Map<String, String> args) {
        if (!valid(args,getMustArgs())) return null;
        String commandLineTemplate = args.get(MVN_BIN_PATH) + "mvn archetype:generate " +
                "-DarchetypeGroupId=io.dropwizard.archetypes " +
                "-DarchetypeArtifactId=java-simple -DarchetypeVersion=0.9.2 " +
                "-DgroupId=com.redhat.examples.dropwizard " +
                "-Dname=%s " +
                "-DartifactId=hola-dropwizard -Dversion=1.0 ";
        String format = String.format(commandLineTemplate, args.get(NAME));
        setCommandLine(format);
        return format;
    }

    @Override
    public String getLineDescription() {
        return "Executes mvn create new dropwizard project.";
    }

    @Override
    public String getName() {
        return "new-dropwizard";
    }

}
