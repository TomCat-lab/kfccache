package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;

public class ZscoreCommand implements Command {
    @Override
    public String name() {
        return "ZSCORE";
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        String key = getKey(args);
        String val = getVal(args);
        Double res = cache.zscore(key,val);
        return Reply.string(res == null?null : res.toString());
    }
}
