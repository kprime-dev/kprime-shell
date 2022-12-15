package it.informatelier.kprime.cli.http;

public interface ModelRepository {

    public ModelResponse ask(ModelRequest request);
}
