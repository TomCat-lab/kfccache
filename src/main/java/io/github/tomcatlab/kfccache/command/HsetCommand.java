package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;

public class HsetCommand implements Command {
    @Override
    public String name() {
        return "HSET";
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        String key = getKey(args);
        String[] hkeys = getHkeys(args);
        String[] hvals = getHvals(args);
        return Reply.integer( cache.hset(key,hkeys,hvals));
    }
}
