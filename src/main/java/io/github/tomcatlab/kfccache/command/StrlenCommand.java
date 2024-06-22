package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;

public class StrlenCommand implements Command {
    @Override
    public String name() {
        return "STRLEN";
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        String val = cache.get(args[4]);
        if (val==null) {
            return Reply.integer(0);
        }
        return Reply.integer(val.length());
    }
}
