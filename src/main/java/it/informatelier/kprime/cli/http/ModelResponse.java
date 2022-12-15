package it.informatelier.kprime.cli.http;

public class ModelResponse {
    String answer;

    public ModelResponse(String response) {
        this.answer = response;
    }
    public String getAnswer() {
        return answer;
    }
}
