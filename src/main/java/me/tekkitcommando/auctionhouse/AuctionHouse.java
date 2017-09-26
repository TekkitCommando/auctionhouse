package me.tekkitcommando.auctionhouse;

import me.tekkitcommando.auctionhouse.auction.AuctionManager;
import me.tekkitcommando.auctionhouse.command.AuctionCommand;
import me.tekkitcommando.auctionhouse.listener.AuctionListener;
import me.tekkitcommando.auctionhouse.listener.ChatListener;
import me.tekkitcommando.auctionhouse.listener.SellListener;
import me.tekkitcommando.auctionhouse.redis.RedisManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class AuctionHouse extends JavaPlugin {

    private static Economy economy = null;
    private Logger logger;

    /**
     * Allows access to the economy instance
     *
     * @return the economy instance
     */
    public static Economy getEconomy() {
        return economy;
    }

    private void setEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);

        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        } else {
            logger.warning("Could not hook into Vault! Make sure it is installed along with an Economy plugin.");
        }
    }

    private void setupCommands() {
        getCommand("auction").setExecutor(new AuctionCommand());
    }

    private void setupEvents() {
        getServer().getPluginManager().registerEvents(new AuctionListener(), this);
        getServer().getPluginManager().registerEvents(new SellListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }

    /**
     * Handles the enabling of the plugin when the server is started
     */
    @Override
    public void onEnable() {
        logger = getLogger();

        logger.info("[Auction House] Setting up economy...");
        setEconomy();

        logger.info("[Auction House] Setting up redis...");
        RedisManager.setupRedis();

        logger.info("[Auction House] Loading auction data...");
        AuctionManager.getItemsFromDatabase();

        logger.info("[Auction House] Enabling auction command...");
        setupCommands();

        logger.info("[Auction House] Registering events...");
        setupEvents();
    }

    /**
     * Handles the disabling of the plugin when the server is stopped
     */
    @Override
    public void onDisable() {
        logger.info("[Auction House] Saving auction data...");
        AuctionManager.saveItemsToDatabase();
    }
}
