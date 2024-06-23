package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;

import java.util.Arrays;

public class ZaddCommand implements Command {
    @Override
    public String name() {
        return "ZADD";
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        String key = getKey(args);
        String[] scores = getHkeys(args);
        String[] svals = getHvals(args);
        return Reply.integer(cache.zadd(key, toDouble(scores),svals));
    }

    private double[] toDouble(String[] scores) {
        return Arrays.stream(scores).mapToDouble(Double::parseDouble).toArray();
    }
}
