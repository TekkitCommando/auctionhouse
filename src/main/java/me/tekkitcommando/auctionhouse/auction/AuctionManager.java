package me.tekkitcommando.auctionhouse.auction;

import com.google.gson.Gson;
import me.tekkitcommando.auctionhouse.redis.RedisManager;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

public class AuctionManager {

    private static Gson gson = new Gson();
    private static Map<Integer, AuctionItem> auctionItems = new HashMap<>();
    private static Map<Player, AuctionItem> pendingAuctions = new HashMap<>();

    /**
     * Allows access to the map of all the auction items
     *
     * @return The map of auction items
     */
    public static Map<Integer, AuctionItem> getAuctionItems() {
        return auctionItems;
    }

    /**
     * Uses the redis manager and gson to access the json data and create
     * the auction items and add them to the list of purchasable auctions
     */
    public static void getItemsFromDatabase() {
        Jedis jedis = RedisManager.getPool().getResource();

        for (String key : RedisManager.getRedisKeys()) {
            String item = jedis.get(key);

            AuctionItem auctionItem = gson.fromJson(item, AuctionItem.class);
            auctionItem.setupAuction();

            auctionItems.put(auctionItem.getId(), auctionItem);
        }

        jedis.close();
    }

    /**
     * Saves the items as json data to the redis database
     */
    public static void saveItemsToDatabase() {
        if (AuctionManager.getAuctionItems().size() > 0) {
            Jedis jedis = RedisManager.getPool().getResource();

            for (AuctionItem auctionItem : auctionItems.values()) {
                String auction = gson.toJson(auctionItem);
                System.out.println(auction);

                jedis.set(String.valueOf(auctionItem.getId()), auction);
            }

            jedis.close();
        } else {
            System.out.println("No auction items to export!");
        }
    }

    /**
     * A quick function for retrieval of one auction item if it exists only
     * if you know the id of the auction
     *
     * @param id ID of the auction
     * @return The auction if it exists, else null
     */
    public static AuctionItem getAuctionItem(int id) {
        if (auctionItems.containsKey(id)) {
            return auctionItems.get(id);
        }
        return null;
    }

    /**
     * Adds an auction item to the list of purchasable auctions
     *
     * @param auctionItem The auction item instance that will be added
     */
    public static void addAuctionItem(AuctionItem auctionItem) {
        auctionItems.put(auctionItem.getId(), auctionItem);
    }

    public static void removeAuctionItem(int id) {
        if (auctionItems.containsKey(id)) {
            auctionItems.remove(id);
        }
    }

    /**
     * Allows access to the map of pending action items that are being further configured
     *
     * @return The map of pending auction items
     */
    public static Map<Player, AuctionItem> getPendingAuctions() {
        return pendingAuctions;
    }

    /**
     * A quick function for retrieval of one pending auction if it exists, only
     * if you know the player creating the auction
     *
     * @param player The player creating the auction
     * @return The pending auction if it exists, else null
     */
    public static AuctionItem getPendingAuction(Player player) {
        if (pendingAuctions.containsKey(player)) {
            return pendingAuctions.get(player);
        } else {
            return null;
        }
    }

    /**
     * Adds an auction item needing further configuration to the map of pending auctions
     * if there is not already another auction item being configured by the player
     *
     * @param player The player creating the auction
     * @param auctionItem The auction item being created
     * @return true if stored, false if another item is already being created
     */
    public static boolean addPendingAuction(Player player, AuctionItem auctionItem) {
        if (pendingAuctions.containsKey(player)) {
            return false;
        } else {
            pendingAuctions.put(player, auctionItem);
            return true;
        }
    }

    /**
     * Removes an auction item from the pending auctions map. Used when the auction item
     * is properly configured and can be added to the list of purchasable auctions
     *
     * @param player Player that is creating the auction
     */
    public static void removePendingAuction(Player player) {
        if (pendingAuctions.containsKey(player)) {
            pendingAuctions.remove(player);
        }
    }
}
