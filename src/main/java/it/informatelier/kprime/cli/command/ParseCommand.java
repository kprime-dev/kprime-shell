package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.http.KPrimeRepository;
import it.informatelier.kprime.cli.http.ModelRequest;
import it.informatelier.kprime.cli.xml.XmlQuery;

public class ParseCommand extends CommandRun {
    @Override
    public String getName() {
        return "parse";
    }

    @Override
    public String getLineDescription() {
        return "Parse KPrime server commands.";
    }

    @Override
    public void runBody() {
        setResult(new KPrimeRepository().ask(new ModelRequest("help")).getAnswer());

    }
}
