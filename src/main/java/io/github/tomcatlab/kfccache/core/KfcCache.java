package io.github.tomcatlab.kfccache.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

public class KfcCache {
    Map<String, CacheEntry<?>> map = new HashMap<>();

    public String get(String key) {
        CacheEntry<String> cacheEntry = (CacheEntry<String>) map.get(key);
        return cacheEntry.value;
    }

    public void set(String key, String value) {
        map.put(key, new CacheEntry<>(value));
    }

    public String[] mget(String... keys) {
        return keys == null ? new String[0] : Arrays.stream(keys)
                .map(this::get).toArray(String[]::new);
    }

    public void mset(String[] keys, String[] values) {
        if (keys == null || keys.length == 0) {
            return;
        }

        for (int i = 0; i < keys.length; i++) {
            set(keys[i], values[i]);
        }
    }

    public long incr(String key) {
        String str = get(key);
        int value;
        try {
            value = str == null ? 0 : Integer.parseInt(str);
            value++;
            set(key, String.valueOf(value));
        } catch (NumberFormatException e) {
            throw e;
        }
        return value;
    }

    public long decr(String key) {
        String str = get(key);
        int value;
        try {
            value = str == null ? 0 : Integer.parseInt(str);
            value--;
            set(key, String.valueOf(value));
        } catch (NumberFormatException e) {
            throw e;
        }
        return value;
    }


    public long del(String... keys) {
        return keys == null ? 0 : Arrays.stream(keys)
                .map(map::remove).filter(Objects::nonNull).count();
    }

    public long exists(String... keys) {
        return keys == null ? 0 : Arrays.stream(keys)
                .map(map::containsKey).filter(x -> x).count();
    }

    public Integer lpush(String key, String[] vals) {
        CacheEntry<LinkedList<String>> cacheEntry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (cacheEntry == null){
           cacheEntry = new CacheEntry<>(new LinkedList<>());
        }else {
           Arrays.asList(vals).forEach(cacheEntry.value::addFirst);
        }
        map.put(key, cacheEntry);
        return vals.length;
    }

//    public Integer lpush(String key, String...vals) {
//        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
//        if(entry == null) {
//            entry = new CacheEntry<>(new LinkedList<>());
//            this.map.put(key, entry);
//        }
//        LinkedList<String> exist = entry.value;
//        Arrays.stream(vals).forEach(exist::addFirst);
//        return vals.length;
//    }


    public String[] lpop(String key, int count) {
        CacheEntry<LinkedList<String>> cacheEntry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (cacheEntry == null || cacheEntry.value == null || cacheEntry.value.size() ==0) return null;
        int len = Math.min(count, cacheEntry.value.size());
        String[] res = new String[len];
        int index = 0;
        while (index<len){
            String pop = cacheEntry.value.removeLast();
            res[index++] = pop;
        }
        return res;
    }

    public String[] rpop(String key, int count) {
        CacheEntry<LinkedList<String>> cacheEntry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (cacheEntry == null || cacheEntry.value == null || cacheEntry.value.size() ==0) return null;
        int len = Math.min(count, cacheEntry.value.size());
        String[] res = new String[len];
        int index = 0;
        while (index<len){
            String pop = cacheEntry.value.pop();
            res[index++] = pop;
        }
        return res;
    }

    public Integer rpush(String key, String[] vals) {
        CacheEntry<LinkedList<String>> cacheEntry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (cacheEntry != null){
           cacheEntry.value.addAll(Arrays.asList(vals));
        }else {
            map.put(key, new CacheEntry<>(new LinkedList<>(Arrays.asList(vals))));
        }
        return vals.length;
    }

    public Integer llen(String key) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) return 0;
        LinkedList<String> exist = entry.value;
        if (exist == null) return 0;
        return exist.size();
    }

    public String lindex(String key, int index) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) return null;
        LinkedList<String> exist = entry.value;
        if (exist == null) return null;
        if(index >= exist.size()) return null;
        return exist.get(index);
    }

    public String[] lrange(String key, int start, int end) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) return null;
        LinkedList<String> exist = entry.value;
        if (exist == null) return null;
        int size = exist.size();
        if(end >= size) end = size - 1;
        if (size<=start) return null;
        int len = Math.min(size, end-start+1);
        String[] res = new String[len];
        for (int i = 0; i < len; i++){
            res[i] = exist.get(start+i);
        }
        return res;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    static class CacheEntry<T> {
        T value;
    }
}
