package it.informatelier.kprime.cli.fs;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

public class TreeWalkerTest {

    @Test
    @Ignore
    public void test_local_repo_tree_walk() throws IOException {
        Path directory = Paths.get("/home/nipe/.m2/");
        Collection<Path> all = new ArrayList<>();
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                if (file.getFileName().toString().endsWith("jar")) {
                    System.out.print(":["+file.getParent().toString()+"]");
                    System.out.println(":["+file.getFileName()+"]");
                    all.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        assertTrue(all.size()>10);
    }
}
