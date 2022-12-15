package it.informatelier.kprime.cli.fs;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class FsManipulatorTest {

    @Test
    @Ignore
    public  void test_list_all_jars_in_local_repo() {
        List<String> localJarNames = FsManipulator.listLocalJars("/Users/nipe/.m2/repository");
        assertEquals(980,localJarNames.size());
        localJarNames.stream().forEach(System.out::println);
    }


}
