package it.informatelier.kprime.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Project {

    private List<String> tokens;
    private Map<String, List<String>> values;
    private String tree = "";

    public Project() {
        tokens = new ArrayList<>();
        values = new HashMap<>();
    }

    /**
     * Get the list of token in Project
     *
     * @return
     */
    public List<String> getTokens() {
        return tokens;
    }

    /**
     * Add token in Project
     *
     * @param token
     */
    public void addToken(String token) {
        tokens.add(token);
        tree += ".";
    }

    /**
     * Add value in a token
     *
     * @param token
     * @param value
     * @throws IllegalArgumentException
     */
    public void addValue(String token, String value) throws IllegalArgumentException {

        List<String> valsPerToken = values.get(token);

        if (valsPerToken == null) {
            valsPerToken = new ArrayList<>();
        }

        valsPerToken.add(value);
        values.put(token, valsPerToken);

        tree += String.format("\n-->%s.\n%s:%s", value, token, getValuesLine(token));
    }

    /**
     * Get token values
     *
     * @param token
     * @return
     */
    public String getValuesLine(String token) {

        List<String> valsPerToken = values.get(token);
        String line = "";

        if (valsPerToken == null) {
            return "-";
        }

        for (String val : valsPerToken) {
            line += " " + val;
        }

        return line.trim();
    }

    /**
     * Check if the project is empty
     *
     * @return
     */
    public Boolean isEmpty() {
        return tokens.isEmpty();
    }

    @Override
    public String toString() {
        return tree;
    }

    public void changeValue(String tagName, String newValue) {
        int count = 0;
        for (String tag : tokens) {
            if (tag.equals(tagName)) {
                count++;
            }
        }
        if (count == 1) {
        }
    }
}
