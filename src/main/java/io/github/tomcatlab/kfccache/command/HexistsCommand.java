package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;

public class HexistsCommand implements Command {
    @Override
    public String name() {
        return this.getClass().getSimpleName().replace("Command", "").toUpperCase();
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        String key = getKey(args);
        String hkey = getVal(args);
        Integer val = cache.hexists(key,hkey);
        return Reply.integer(val);
    }
}
