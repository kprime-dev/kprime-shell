package it.informatelier.kprime.cli;

/**
 * Created by nipe on 23/11/16.
 */
public class Config {

    public String get(String envKey) {
        return System.getenv(envKey);
    }
}
