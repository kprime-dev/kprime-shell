package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.http.KPrimeProxy;
import it.informatelier.kprime.cli.http.ModelRequest;

public class KPPostCommand extends CommandRun {
    @Override
    public String getName() {
        return "post";
    }

    @Override
    public String getLineDescription() {
        return "POST KPrime server commands.";
    }

    @Override
    public void runBody() {
        String address = getMustArgs().get(must_arg_address);
        String context = getMustArgs().get(must_arg_context);
        String kpUser = getMustArgs().get(must_arg_user_name);
        String kpPass = getMustArgs().get(must_arg_user_pass);
        if (address==null || address.isEmpty()) {
            setResult("No required "+must_arg_address+" in POST properties.");
            return;
        }
        if (context==null || context.isEmpty()) {
            setResult("No required "+must_arg_context+" in POST properties.");
            return;
        }
        setResult(new KPrimeProxy().ask(address,context,kpUser,kpPass,
                new ModelRequest("POST help")).getAnswer());

    }
}
