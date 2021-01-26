package embedded.container;

public interface EmbeddedContainer {
    void start();
    void stop();
    void setupContext() throws IllegalAccessException, InstantiationException;
}
