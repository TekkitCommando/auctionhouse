package me.tekkitcommando.auctionhouse.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

public class RedisManager {

    private static List<String> redisKeys = new ArrayList<>();
    private static JedisPool pool;

    /**
     * Instantiates the JedisPool for redis connections and than retrieves
     * the keys from the redis database
     */
    public static void setupRedis() {
        pool = new JedisPool("localhost");
        getKeysFromDatabase();
    }

    /**
     * Allows access to the JedisPool instance for redis connections
     *
     * @return The JedisPool instance for redis connections
     */
    public static JedisPool getPool() {
        return pool;
    }

    /**
     * Allows access to the redis keys for data retrieval
     *
     * @return The redis keys
     */
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
