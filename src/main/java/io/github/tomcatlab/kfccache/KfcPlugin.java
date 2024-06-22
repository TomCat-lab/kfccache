package io.github.tomcatlab.kfccache;

public interface KfcPlugin {
    void init();
    void startUp();
    void shutdown();
}
