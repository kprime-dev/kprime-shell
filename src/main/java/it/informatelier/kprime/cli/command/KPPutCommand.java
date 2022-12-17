package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.http.KPrimeProxy;
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
        String address = getMustArgs().get(must_arg_address);
        String context = getMustArgs().get(must_arg_context);
        if (address==null || address.isEmpty()) {
            setResult("No required "+must_arg_address+" in properties.");
            return;
        }
        if (context==null || context.isEmpty()) {
            setResult("No required "+must_arg_context+" in properties.");
            return;
        }
        String command = getFirstToken()+ " "+ String.join(" ",getArgTokens());
        System.out.println("KPPutCommand ["+command+"]");
        setResult(new KPrimeProxy().ask(address,context,
                new ModelRequest("PUT >"+command)).getAnswer());

    }
}
