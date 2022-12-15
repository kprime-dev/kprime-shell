package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.http.Gister;
import it.informatelier.kprime.cli.xml.XmlManipulator;
import org.w3c.dom.Document;

public class GistJUnitCommand extends CommandRun {

    @Override
    public String getLineDescription() {
        return "Create a gist junit.";
    }

    @Override
    public String getName() {
        return "gist-junit";
    }

    @Override
    public void runBody() {
        String gist = Gister.getGist("https://api.github.com/gists/ae856e105f1f1adb55311bf4be203abf", "junit-dep.xml");
        String result = cleanGistChars(gist);
        Document dependencyDoc = XmlManipulator.parseDocument(result);
        if (dependencyDoc==null) return;
        new XmlManipulator().importDependency(dependencyDoc);
    }

    private String cleanGistChars(String gist) {
        gist = gist.replaceAll("\"","");
        String result = "";
        boolean jump = false;
        for (char c: gist.toCharArray()) {
            int v = (int)c;
            if (jump==true) {
                jump = false;
                continue;
            }
            if (v!=92) {
                result += c;
            } else {
                result += System.lineSeparator();
                jump = true;
            }

        }
        return result;
    }
}
