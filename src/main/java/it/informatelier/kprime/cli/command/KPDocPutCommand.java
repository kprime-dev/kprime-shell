package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.http.KPrimeProxy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class KPDocPutCommand extends CommandRun {

    @Override
    public String getLineDescription() {
        return "PUTs a local text file to server.";
    }

    @Override
    public String getName() {
        return "doc-put";
    }

    @Override
    public void runBody() {
        String address = getMustArgs().get(must_arg_address);
        String context = getMustArgs().get(must_arg_context);
        String trace = getMustArgs().get(must_arg_trace);
        String filePathString = getMustArgs().get(must_arg_file_path);
        if (address==null || address.isEmpty()) {
            setResult("No required "+must_arg_address+" in PUT properties.");
            return;
        }
        if (context==null || context.isEmpty()) {
            setResult("No required "+must_arg_context+" in PUT properties.");
            return;
        }
        if (trace==null || trace.isEmpty()) {
            setResult("No required "+must_arg_trace+" in PUT properties.");
            return;
        }
        if (filePathString==null || filePathString.isEmpty()) {
            setResult("No required "+must_arg_file_path+" in PUT properties.");
            return;
        }
        String docText = null;
        try {
            docText = Files.readString(Path.of(filePathString));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileName = filePathString.substring(filePathString.lastIndexOf('/'));
        new KPrimeProxy().putDoc(address,context,trace,fileName,docText);
    }
}
