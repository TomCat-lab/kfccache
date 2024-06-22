package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;

public class MgetCommand implements Command {
    @Override
    public String name() {
        return "MGET";
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        String[] keys = new String[(args.length-3)/2];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = args[4+i*2];
        }
        String[] results = cache.mget(keys);
        return Reply.array(results);
    }
}
