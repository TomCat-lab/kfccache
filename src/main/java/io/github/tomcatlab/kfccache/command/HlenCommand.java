package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;

public class HlenCommand implements Command {
    @Override
    public String name() {
        return this.getClass().getSimpleName().replace("Command", "").toUpperCase();
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        String key = getKey(args);
        Integer val = cache.hlen(key);
        return Reply.integer(val);
    }
}
