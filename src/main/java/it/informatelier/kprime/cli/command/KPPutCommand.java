package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.http.KPrimeRepository;
import it.informatelier.kprime.cli.http.ModelRequest;

public class KPPutCommand extends CommandRun {
    @Override
    public String getName() {
        return "put";
    }

    @Override
    public String getLineDescription() {
        return "PUT KPrime server commands.";
    }

    @Override
    public void runBody() {
        String command = getFirstToken()+ " "+ String.join(" ",getArgTokens());
        System.out.println("KPPutCommand ["+command+"]");
        setResult(new KPrimeRepository().ask(new ModelRequest("PUT >"+command)).getAnswer());

    }
}
