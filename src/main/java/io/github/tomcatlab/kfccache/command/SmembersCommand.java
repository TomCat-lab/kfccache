package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;

public class SmembersCommand implements Command {
    @Override
    public String name() {
        return this.getClass().getSimpleName().replace("Command", "").toUpperCase();
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        String key = getKey(args);
        String[] results = cache.smembers(key);
        return Reply.array(results);
    }
}
