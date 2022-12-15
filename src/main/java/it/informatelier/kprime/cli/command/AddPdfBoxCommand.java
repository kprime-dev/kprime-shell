package it.informatelier.kprime.cli.command;

import it.informatelier.kprime.cli.xml.XmlManipulator;

public class AddPdfBoxCommand extends CommandRun {
    @Override
    public String getName() {
        return "add-pdfbox";
    }

    @Override
    public String getLineDescription() {
        return "Adds PdfBox dependency.";
    }

    @Override
    public void runBody() {
        new XmlManipulator().addPdfBoxDependency();
    }
}
