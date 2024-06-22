package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;

public class SetCommand implements Command {
    @Override
    public String name() {
        return "SET";
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        String key = args[4];
        String length = args[5];
        if ("$0".equals(length)){
            cache.set(key,"");
        } else  {
            cache.set(key,args[6]);
        }
        return Reply.string(OK);
    }
}
