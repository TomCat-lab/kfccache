package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;

public class HgetallCommand implements Command {
    @Override
    public String name() {
        return this.getClass().getSimpleName().replace("Command", "").toUpperCase();
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        String key = getKey(args);
        String[] val = cache.hgetall(key);
        return Reply.array(val);
    }
}
