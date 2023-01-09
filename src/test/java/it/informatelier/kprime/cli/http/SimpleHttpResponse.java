package it.informatelier.kprime.cli.http;

public class SimpleHttpResponse {
    private ResponseHeader responseHeader;
    private String response;
    private String spellcheck;

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getSpellcheck() {
        return spellcheck;
    }

    public void setSpellcheck(String spellcheck) {
        this.spellcheck = spellcheck;
    }
}
