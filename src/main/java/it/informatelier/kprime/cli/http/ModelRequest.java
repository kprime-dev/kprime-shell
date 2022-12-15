package it.informatelier.kprime.cli.http;

public class ModelRequest {

    private String question;

    public ModelRequest(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }
}
