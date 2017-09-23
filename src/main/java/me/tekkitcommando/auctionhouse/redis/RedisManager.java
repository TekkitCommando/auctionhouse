package me.tekkitcommando.auctionhouse.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

public class RedisManager {

    private static Jedis jedis;
    private static List<String> redisKeys = new ArrayList<>();

    public static void setupRedis() {
        JedisPool pool = new JedisPool("localhost");
        jedis = pool.getResource();
//        jedis.auth("the-password");
        getKeysFromDatabase();
    }

    public static Jedis getJedis() {
        return jedis;
    }

    public static List<String> getRedisKeys() {
        return redisKeys;
    }

    /**
     * Gets all keys from redis database and adds them to
     * the string list for ease of access.
     */
    private static void getKeysFromDatabase() {
        for (String key : getJedis().keys("*")) {
            if (!getRedisKeys().contains(key))
                getRedisKeys().add(key);
        }
    }
}
