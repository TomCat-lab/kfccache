package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;

public class InfoCommand implements Command {
    private static final String INFO = "KFCCache server[v1.0] created by cola."+CRLF+
            "mock redis server, at 2024-06-12 in Beijing."+CRLF;
    @Override
    public String name() {
        return "INFO";
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        return Reply.bulkString(INFO);
    }
}
