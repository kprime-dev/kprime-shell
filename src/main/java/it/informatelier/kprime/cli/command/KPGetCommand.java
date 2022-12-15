package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.http.KPrimeRepository;
import it.informatelier.kprime.cli.http.ModelRequest;

public class KPGetCommand extends CommandRun {
    @Override
    public String getName() {
        return "get";
    }

    @Override
    public String getLineDescription() {
        return "GET KPrime server commands.";
    }

    @Override
    public void runBody() {
        setResult(new KPrimeRepository().ask(new ModelRequest("GET /project/forcetree/json")).getAnswer());

    }
}
