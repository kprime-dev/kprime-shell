package it.informatelier.kprime.cli;

public class Config {

    public String get(String envKey) {
        return System.getenv(envKey);
    }
}
