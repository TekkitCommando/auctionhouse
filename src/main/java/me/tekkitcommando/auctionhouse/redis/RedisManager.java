package me.tekkitcommando.auctionhouse.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

public class RedisManager {

    private static List<String> redisKeys = new ArrayList<>();
    private static JedisPool pool;

    public static void setupRedis() {
        pool = new JedisPool("localhost");
        getKeysFromDatabase();
    }

    public static JedisPool getPool() {
        return pool;
    }

    public static List<String> getRedisKeys() {
        return redisKeys;
    }

    /**
     * Gets all keys from redis database and adds them to
     * the string list for ease of access.
     */
    private static void getKeysFromDatabase() {
        Jedis jedis = pool.getResource();

        for (String key : jedis.keys("*")) {

            if (!getRedisKeys().contains(key))
                getRedisKeys().add(key);

        }

        jedis.close();
    }
}
