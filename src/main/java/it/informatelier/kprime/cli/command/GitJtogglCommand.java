package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.http.Gister;

import java.io.File;
import java.io.IOException;

public class GitJtogglCommand extends CommandRun {

    @Override
    public String getLineDescription() {
        return "Create a gist jtoggl.";
    }

    @Override
    public String getName() {
        return "git-jtoggl";
    }

    @Override
    public void runBody() {
        String remoteFileName = "https://api.github.com/repos/npedot/jtoggl/contents/jtoggl-api";
        String localFileName = "jtoggl.json";
        String localDirName = "";
        String workingDir = ".mpm/";
        new File(workingDir).mkdirs();
        try {
            Gister.fetchGit(remoteFileName, localDirName, localFileName, workingDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
