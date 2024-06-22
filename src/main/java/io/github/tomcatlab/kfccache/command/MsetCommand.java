package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;

public class MsetCommand implements Command {
    @Override
    public String name() {
        return "MSET";
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        String[] keys = new String[(args.length-3)/4];
        String[] vals = new String[(args.length-3)/4];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = args[4+i*4];
            vals[i] = args[6+i*4];
        }
        cache.mset(keys,vals);
        return Reply.string(OK);
    }
}
