package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;

public class PingCommand implements Command {
    @Override
    public String name() {
        return "PING";
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        String res ="PONG";
        if (args.length>=5) {
            res = args[4];
        }
        return Reply.string(res);
    }
}
