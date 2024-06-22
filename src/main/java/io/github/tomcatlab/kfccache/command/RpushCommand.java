package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;

public class RpushCommand implements Command {
    @Override
    public String name() {
        return "RPUSH";
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        String key = args[4];
        String[] vals = getParamsNoKey(args);
        return Reply.integer(cache.rpush(key, vals));
    }
}
