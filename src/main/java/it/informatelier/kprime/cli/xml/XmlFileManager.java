package it.informatelier.kprime.cli.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class XmlFileManager {
    public XmlFileManager() {
    }

    FileInputStream silentOpenPomFile() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("pom.xml");
        } catch (FileNotFoundException e) {
        }
        return fis;
    }
}