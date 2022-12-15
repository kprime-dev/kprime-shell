package it.informatelier.kprime.cli.fs;

import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TreeCopierTest {

    @Test
    @Ignore
    public void test_a_simple_tree_copy() {
        Path sourcePath = Paths.get("/home/nipe/Temp/spark");
        Path targetPath = Paths.get("/home/nipe/Temp/spark2");
        TreeCopier.copy(sourcePath,targetPath,false ,false);
    }
}
