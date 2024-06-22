package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;

public class ExistsCommand implements Command {
    @Override
    public String name() {
        return "EXISTS";
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        String[] keys = new String[(args.length-3)/2];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = args[4+i*2];
        }
        long exists = cache.exists(keys);
        return Reply.integer((int) exists);
    }
}
