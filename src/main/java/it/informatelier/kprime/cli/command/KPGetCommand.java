package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.http.KPrimeProxy;
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
        String address = getMustArgs().get(must_arg_address);
        String context = getMustArgs().get(must_arg_context);
        if (address==null || address.isEmpty()) {
            setResult("No required "+must_arg_address+" in GET properties.");
            return;
        }
        if (context==null || context.isEmpty()) {
            setResult("No required "+must_arg_context+" in GET properties.");
            return;
        }
        setResult(new KPrimeProxy().ask(address,context,
                new ModelRequest("GET /project/forcetree/json")).getAnswer());

    }
}
