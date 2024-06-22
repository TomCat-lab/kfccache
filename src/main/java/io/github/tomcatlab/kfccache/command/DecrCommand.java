package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;

public class DecrCommand implements Command {
    @Override
    public String name() {
        return "DECR";
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        try {
            long val = cache.decr(args[4]);
           return Reply.integer((int) val);
        }catch (NumberFormatException e){
            return Reply.error("NFE " + args[4] + " value[" + cache.get(args[4]) + "] is not an integer.");
        }

    }
}
