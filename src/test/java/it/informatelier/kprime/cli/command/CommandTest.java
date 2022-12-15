package it.informatelier.kprime.cli.command;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CommandTest {

    @Test
    public void command_query_project_has_first_and_one_arg_token() {
        Command command = new Command() {
            @Override
            public String getCommandLine(Map<String, String> args) {
                return "";
            }

            @Override
            public String getLineDescription() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public void runBody() {}

        };
        command.setCommandLine("query project");
        assertEquals("query",command.getFirstToken());
        assertEquals("project",command.getArgTokens().get(0));
    }
}
