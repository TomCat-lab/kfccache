package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;

public class HmgetCommand implements Command {
    @Override
    public String name() {
        return "HMGET";
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        String key = getKey(args);
        String[] hkeys = getParamsNoKey(args);
        String[] val = cache.hmget(key,hkeys);
        return Reply.array(val);
    }
}
