package it.informatelier.kprime.cli;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestProject {

    private static final String A_TOKEN = "alfa";

    @Test
    public void test_add_token() {
        Project prj = new Project();

        prj.addToken(A_TOKEN);

        assertTrue(prj.getTokens().contains(A_TOKEN));
    }

    @Test
    public void test_project_is_empty() {
        Project prj = new Project();

        assertTrue(prj.isEmpty());
    }

    @Test
    public void test_project_is_not_empty() {
        Project prj = new Project();

        prj.addToken(A_TOKEN);

        assertFalse(prj.isEmpty());
    }

    @Test
    public void test_add_no_value() {
        Project prj = new Project();

        prj.addToken(A_TOKEN);

        assertEquals(prj.getValuesLine(A_TOKEN), "-");
    }

    @Test
    public void test_add_single_value() {
        Project prj = new Project();

        prj.addToken(A_TOKEN);
        prj.addValue(A_TOKEN, "beta");

        assertEquals(prj.getValuesLine(A_TOKEN), "beta");
    }

    @Test
    public void test_add_multiple_values() {
        Project prj = new Project();

        prj.addToken(A_TOKEN);
        prj.addValue(A_TOKEN, "beta");
        prj.addValue(A_TOKEN, "delta");
        prj.addValue(A_TOKEN, "gamma");

        assertEquals(prj.getValuesLine(A_TOKEN), "beta delta gamma");
    }

}
