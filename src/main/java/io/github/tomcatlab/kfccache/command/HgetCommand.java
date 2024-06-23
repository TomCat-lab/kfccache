package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;

public class HgetCommand implements Command {
    @Override
    public String name() {
        return "HGET";
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        String key = getKey(args);
        String hkey =getVal(args);
        String val = cache.hget(key,hkey);
        return Reply.string(val);
    }
}
