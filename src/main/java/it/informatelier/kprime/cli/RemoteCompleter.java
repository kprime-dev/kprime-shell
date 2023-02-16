package it.informatelier.kprime.cli;

import it.informatelier.kprime.cli.command.Commandable;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RemoteCompleter implements Completer {

    private final CommandLineParser parser;
    private final CommandExecutor executor;
    private final ServerRequiredParams serverRequiredParams;

    public RemoteCompleter(CommandLineParser parser, CommandExecutor executor, ServerRequiredParams serverRequiredParams) {
        this.parser = parser;
        this.executor = executor;
        this.serverRequiredParams = serverRequiredParams;
    }

    @Override
    public void complete(LineReader lineReader, ParsedLine parsedLine, List<Candidate> list) {
        String line = parsedLine.line();
        Commandable commandable = parser.parse(line);

        commandable.setMustArgs(Map.of(
                Commandable.must_arg_context, serverRequiredParams.getContext(),
                Commandable.must_arg_address, serverRequiredParams.getAddress(),
                Commandable.must_arg_user_name, serverRequiredParams.getUserName(),
                Commandable.must_arg_user_pass, serverRequiredParams.getUserPass()
        ));

        executor.execute(commandable);
        List<String> options = commandable.getOptsArgs();
        if (options != null) {
            list.addAll(options
                    .stream()
                    .map(Candidate::new)
                    .collect(Collectors.toList())
            );
        }
    }
}
