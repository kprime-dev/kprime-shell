package it.informatelier.kprime.cli.http;

public class KPrimeDTO {

    private String request;
    private String response;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "KPrimeDTO{" +
                "request='" + request + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
