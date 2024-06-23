package io.github.tomcatlab.kfccache.core;

public interface Command {
     String CRLF = "\r\n";
     String OK = "OK";

    String name();
    Reply<?> execute(KfcCache cache, String[] args);

    default String[] getParamsNoKey(String[] args) {
        int len = (args.length-5)/2;
        String[] keys = new String[len];
        for(int i=0; i<len; i++) {
            keys[i] = args[6+i*2];
        }
        return keys;
    }

    default String getKey(String[] args) {
        return args[4];
    }

    default String getVal(String[] args) {
        return args[6];
    }

    default String[] getHvals(String[] args) {
        int len = (args.length - 5) / 4;
        String[] vals = new String[len];
        for (int i = 0; i < len; i++) {
            vals[i] = args[8 + i * 4];
        }
        return vals;
    }

    default String[] getHkeys(String[] args) {
        int len = (args.length - 5) / 4;
        String[] keys = new String[len];
        for (int i = 0; i < len; i++) {
            keys[i] = args[6 + i * 4];
        }
        return keys;
    }
}
