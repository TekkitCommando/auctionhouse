package me.tekkitcommando.auctionhouse;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;

import java.util.logging.Logger;

public class AuctionHouse extends JavaPlugin {

    private static JedisPool pool;
    private static Economy econ = null;
    private Logger logger;

    public static JedisPool getPool() {
        return pool;
    }

    public static Economy getEcon() {
        return econ;
    }

    @Override
    public void onEnable() {
        logger = getLogger();

        logger.info("[Auction House] Setting up economy...");
        if (!setEcon()) {
            logger.warning("[Auction House] Economy could not be setup! Shutting down plugin!");
            getServer().getPluginManager().disablePlugin(this);
        }

        logger.info("[Auction House] Starting jedis pool...");
        pool = new JedisPool("localhost");

        logger.info("[Auction House] Loading auction data...");

    }

    @Override
    public void onDisable() {
        logger.info("[Auction House] Saving auction data...");
    }

    private boolean setEcon() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
