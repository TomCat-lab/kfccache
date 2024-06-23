package io.github.tomcatlab.kfccache.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Stream;

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
        if (cacheEntry == null) {
            cacheEntry = new CacheEntry<>(new LinkedList<>());
        } else {
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
        if (cacheEntry == null || cacheEntry.value == null || cacheEntry.value.size() == 0) return null;
        int len = Math.min(count, cacheEntry.value.size());
        String[] res = new String[len];
        int index = 0;
        while (index < len) {
            String pop = cacheEntry.value.removeLast();
            res[index++] = pop;
        }
        return res;
    }

    public String[] rpop(String key, int count) {
        CacheEntry<LinkedList<String>> cacheEntry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (cacheEntry == null || cacheEntry.value == null || cacheEntry.value.size() == 0) return null;
        int len = Math.min(count, cacheEntry.value.size());
        String[] res = new String[len];
        int index = 0;
        while (index < len) {
            String pop = cacheEntry.value.pop();
            res[index++] = pop;
        }
        return res;
    }

    public Integer rpush(String key, String[] vals) {
        CacheEntry<LinkedList<String>> cacheEntry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (cacheEntry != null) {
            cacheEntry.value.addAll(Arrays.asList(vals));
        } else {
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
        if (index >= exist.size()) return null;
        return exist.get(index);
    }

    public String[] lrange(String key, int start, int end) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) return null;
        LinkedList<String> exist = entry.value;
        if (exist == null) return null;
        int size = exist.size();
        if (end >= size) end = size - 1;
        if (size <= start) return null;
        int len = Math.min(size, end - start + 1);
        String[] res = new String[len];
        for (int i = 0; i < len; i++) {
            res[i] = exist.get(start + i);
        }
        return res;
    }

    //set
    public Integer sadd(String key, String[] vals) {
        CacheEntry<LinkedHashSet<String>> cacheEntry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (cacheEntry == null) {
            cacheEntry = new CacheEntry<>(new LinkedHashSet<>());
        } else {
            cacheEntry.value.addAll(Arrays.asList(vals));
        }
        map.put(key, cacheEntry);
        return vals.length;
    }

    public String[] smembers(String key) {
        CacheEntry<LinkedHashSet<String>> cacheEntry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (cacheEntry == null) {
            return null;
        } else {
            return cacheEntry.value.toArray(new String[0]);
        }
    }

    public Integer scard(String key) {
        if (key == null) return 0;
        CacheEntry<LinkedHashSet<String>> cacheEntry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (cacheEntry == null) {
            return 0;
        } else {
            return cacheEntry.value.size();
        }
    }

    public Integer sismember(String key, String val) {
        CacheEntry<LinkedHashSet<String>> cacheEntry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (cacheEntry == null) {
            return 0;
        } else {
            return cacheEntry.value.contains(val) ? 1 : 0;
        }
    }

    public Integer srem(String key, String[] vals) {
        if (key == null) return 0;
        CacheEntry<LinkedHashSet<String>> cacheEntry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (cacheEntry == null) {
            return 0;
        }

        return (int) Arrays.stream(vals)
                .map(cacheEntry.value::remove).filter(x -> x).count();
    }

    Random random = new Random();

    public String[] spop(String key, int count) {
        CacheEntry<LinkedHashSet<String>> cacheEntry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (cacheEntry == null || cacheEntry.value == null || cacheEntry.value.size() == 0) return null;
        int len = Math.min(count, cacheEntry.value.size());
        String[] res = new String[len];
        int index = 0;
        while (index < len) {
            String obj = cacheEntry.value.toArray(String[]::new)[random.nextInt(cacheEntry.value.size())];
            cacheEntry.value.remove(obj);
            res[index++] = obj;
        }
        return res;
    }

    public Integer hset(String key, String[] hkeys, String[] hvals) {
        if (hkeys == null || hkeys.length == 0) return 0;
        if (hvals == null || hvals.length == 0) return 0;
        CacheEntry<LinkedHashMap<String, String>> cacheEntry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (cacheEntry == null) {
            cacheEntry = new CacheEntry<>(new LinkedHashMap<>());
        }
        int count = 0;
        for (int i = 0; i < hkeys.length; i++) {
            if (cacheEntry.value.put(hkeys[i], hvals[i]) == null) count++;
        }
        map.put(key, cacheEntry);
        return count;
    }


    public String hget(String key, String hkey) {
        if (hkey == null) return null;
        CacheEntry<LinkedHashMap<String, String>> cacheEntry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (cacheEntry == null) {
            return null;
        }
        String val = cacheEntry.value.get(hkey);
        return val;
    }

    //
    public String[] hgetall(String key) {
        CacheEntry<LinkedHashMap<String, String>> cacheEntry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (cacheEntry == null) {
            return null;
        }

        return cacheEntry.value.entrySet().stream().flatMap(e -> Stream.of(e.getKey(), e.getValue())).toArray(String[]::new);
    }

    public Integer hlen(String key) {
        CacheEntry<LinkedHashMap<String, String>> cacheEntry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (cacheEntry == null) {
            return 0;
        }
        return cacheEntry.value.size();

    }

    public Integer hdel(String key, String hkey) {
        if (key == null || hkey == null) return 0;
        CacheEntry<LinkedHashMap<String, String>> cacheEntry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (cacheEntry == null) return 0;
        return cacheEntry.value.remove(hkey) == null ? 0 : 1;
    }

    public Integer hexists(String key, String hkey) {
        if (key == null || hkey == null) return 0;
        CacheEntry<LinkedHashMap<String, String>> cacheEntry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (cacheEntry == null) return 0;
        return cacheEntry.value.containsKey(hkey) ? 1 : 0;
    }

    public String[] hmget(String key, String[] hkeys) {
        if (hkeys == null || hkeys.length == 0) return null;
        CacheEntry<LinkedHashMap<String, String>> cacheEntry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (cacheEntry == null) return null;
        return Arrays.stream(hkeys).map(cacheEntry.value::get).toArray(String[]::new);
    }

    public Integer zadd(String key, double[] aDouble, String[] vals) {

        CacheEntry<LinkedHashSet<ZsetEntry>> cacheEntry = (CacheEntry<LinkedHashSet<ZsetEntry>>) map.get(key);
        if (cacheEntry == null) {
            cacheEntry = new CacheEntry<>(new LinkedHashSet<>());
        }

        for (int i = 0; i < vals.length; i++) {
            cacheEntry.value.add(new ZsetEntry(vals[i], aDouble[i]));
        }

        map.put(key, cacheEntry);
        return vals.length;
    }

    public Integer zcard(String key) {
        if (key == null) return 0;
        CacheEntry<?> cacheEntry = map.get(key);
        if (cacheEntry == null) {
            return 0;
        } else {
            LinkedHashSet<?> value = (LinkedHashSet<?>) cacheEntry.value;
            return value.size();
        }
    }

    public Integer zcount(String key, double min, double max) {
        if (key == null) return 0;
        CacheEntry<?> cacheEntry = map.get(key);
        if (cacheEntry == null) {
            return 0;
        } else {
            return (int) ((LinkedHashSet<ZsetEntry>) cacheEntry.value).stream().filter(x -> x.score >= min && x.score <= max).count();
        }
    }

    public Double zscore(String key, String val) {
        CacheEntry<?> cacheEntry = map.get(key);
        if (cacheEntry == null) {
            return null;
        } else {
            return ((LinkedHashSet<ZsetEntry>) cacheEntry.value).stream()
                    .filter(x -> x.val.equals(val))
                    .map(x -> x.score).findFirst().orElse(null);
        }
    }

    public Integer zrank(String key, String val) {
        CacheEntry<LinkedHashSet<ZsetEntry>> cacheEntry = (CacheEntry<LinkedHashSet<ZsetEntry>>) map.get(key);
        if (cacheEntry == null) return null;
        Double zscore = zscore(key, val);
        if (zscore == null) return null;
        return (int) cacheEntry.value.stream()
                .filter(x -> x.score < zscore)
                .count();
    }

    public Integer zrem(String key, String[] vals) {
        CacheEntry<LinkedHashSet<ZsetEntry>> cacheEntry = (CacheEntry<LinkedHashSet<ZsetEntry>>) map.get(key);
        if (cacheEntry == null) {
            return 0;
        }

        return (int) Arrays.stream(vals)
                .map(e -> cacheEntry.value.removeIf(x -> x.val.equals(e)))
                .filter(x -> x).count();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    static class CacheEntry<T> {
        T value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class ZsetEntry {
        private String val;
        private double score;
    }
}
