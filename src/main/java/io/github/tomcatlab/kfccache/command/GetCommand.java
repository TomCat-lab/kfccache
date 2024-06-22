package io.github.tomcatlab.kfccache.command;

import io.github.tomcatlab.kfccache.core.Command;
import io.github.tomcatlab.kfccache.core.KfcCache;
import io.github.tomcatlab.kfccache.core.Reply;
import org.springframework.util.StringUtils;

public class GetCommand implements Command {
    @Override
    public String name() {
        return "GET";
    }

    @Override
    public Reply<?> execute(KfcCache cache, String[] args) {
        String val = cache.get(args[4]);
        if (StringUtils.isEmpty(val)){
           return Reply.string(val);
        }else{
           return Reply.bulkString(val);
        }
    }
}
