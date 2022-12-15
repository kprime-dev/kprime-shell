package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.http.KPrimeRepository;
import it.informatelier.kprime.cli.http.ModelRequest;

public class KPParseCommand extends CommandRun {
    @Override
    public String getName() {
        return "parse";
    }

    @Override
    public String getLineDescription() {
        return "POST KPrime server commands.";
    }

    @Override
    public void runBody() {
        setResult(new KPrimeRepository().ask(new ModelRequest("POST help")).getAnswer());

    }
}
