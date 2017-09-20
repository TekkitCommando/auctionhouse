package me.tekkitcommando.auctionhouse;

import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;

import java.util.logging.Logger;

public class AuctionHouse extends JavaPlugin {

    private Logger logger;
    private static JedisPool pool;

    @Override
    public void onEnable() {
        logger = getLogger();

        logger.info("[Auction House] Starting jedis pool...");
        pool = new JedisPool("localhost");

        logger.info("[Auction House] Loading auction data...");

    }

    @Override
    public void onDisable() {
        logger.info("[Auction House] Saving auction data...");
    }

    public static JedisPool getPool() {
        return pool;
    }
}
