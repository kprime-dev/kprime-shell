package it.informatelier.kprime.cli.command;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class NewWarCommandTest {

    @Test
    public void checks_commandline_with_all_arguments(){
        Map<String, String> args = new HashMap<>();
        args.put(NewWarCommand.ARTIFACT_ID,"alfa");
        args.put(NewWarCommand.GROUP_ID,"beta");
        args.put(NewWarCommand.MVN_BIN_PATH,"/path/");
        NewWarCommand newWarCommand = new NewWarCommand(args);
        assertThat(newWarCommand.getCommandLine(),
                is("/path/mvn archetype:generate -DgroupId=beta -DartifactId=alfa " +
                        "-DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false"));
    }

    @Test
    public void checks_commandline_with_missing_arguments(){
        Map<String, String> args =null;
        NewWarCommand newWarCommand = new NewWarCommand(args);
        assertThat(newWarCommand.getCommandLine(),
                nullValue());
    }

    @Test

    public void checks_commandline_with_missing_some_argument(){
        Map<String, String> args = new HashMap<>();
        args.put(NewWarCommand.MVN_BIN_PATH,"/bin/");
        args.put(NewWarCommand.GROUP_ID,"beta");
        NewWarCommand newWarCommand = new NewWarCommand(args);
        assertThat(newWarCommand.getCommandLine(),
                nullValue());
    }

}
