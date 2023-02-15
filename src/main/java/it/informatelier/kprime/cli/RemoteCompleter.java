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
    private final String context;
    private final String address;

    public RemoteCompleter(CommandLineParser parser, CommandExecutor executor, String context, String address) {
        this.parser = parser;
        this.executor = executor;
        this.context = context;
        this.address = address;
    }

    @Override
    public void complete(LineReader lineReader, ParsedLine parsedLine, List<Candidate> list) {
        String line = parsedLine.line();
        Commandable commandable = parser.parse(line);

        commandable.setMustArgs(Map.of(
                Commandable.must_arg_context, context,
                Commandable.must_arg_address, address
        ));

        executor.execute(commandable);
        List<String> options = commandable.getOptsArgs();
        System.out.println(options);
        if (options != null) {
            list.addAll(options
                    .stream()
                    .map(Candidate::new)
                    .collect(Collectors.toList())
            );
        }
    }
}
