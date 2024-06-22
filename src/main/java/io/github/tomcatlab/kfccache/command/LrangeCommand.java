package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;

public class LrangeCommand implements Command {
    @Override
    public String name() {
        return "LRANGE";
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        String key = getKey(args);
        String[] paramsNoKey = getParamsNoKey(args);
        int start = Integer.parseInt(paramsNoKey[0]);
        int end = Integer.parseInt(paramsNoKey[1]);
        return Reply.array(cache.lrange(key, start, end));
    }
}
