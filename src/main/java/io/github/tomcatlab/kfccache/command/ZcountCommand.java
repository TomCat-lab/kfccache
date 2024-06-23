package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;

public class ZcountCommand implements Command {
    @Override
    public String name() {
        return "ZCOUNT";
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        String key = getKey(args);
        double min = Double.parseDouble(getVal(args));
        double max = Double.parseDouble(args[8]);
        return Reply.integer(cache.zcount(key,min,max));
    }
}
