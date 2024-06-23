package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;

public class ScardCommand implements Command {
    @Override
    public String name() {
        return "SCARD";
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        String key = getKey(args);
        return Reply.integer(cache.scard(key));
    }
}
