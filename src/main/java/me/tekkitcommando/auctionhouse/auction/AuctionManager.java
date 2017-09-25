package me.tekkitcommando.auctionhouse.auction;

import com.google.gson.Gson;
import me.tekkitcommando.auctionhouse.redis.RedisManager;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import javax.swing.text.PlainDocument;
import java.util.HashMap;
import java.util.Map;

public class AuctionManager {

    private static Gson gson = new Gson();
    private static Map<Integer, AuctionItem> auctionItems = new HashMap<>();
    private static Map<Player, AuctionItem> pendingAuctions = new HashMap<>();

    public static Map<Integer, AuctionItem> getAuctionItems() {
        return auctionItems;
    }

    public static void getItemsFromDatabase() {
        Jedis jedis = RedisManager.getPool().getResource();
        for (String key : RedisManager.getRedisKeys()) {
            String item = jedis.get(key);
            AuctionItem auctionItem = gson.fromJson(item, AuctionItem.class);

            auctionItems.put(auctionItem.getId(), auctionItem);
        }
        jedis.close();
    }

    public static void saveItemsToDatabase() {
        Jedis jedis = RedisManager.getPool().getResource();
        for (AuctionItem auctionItem : auctionItems.values()) {
            String auction = gson.toJson(auctionItem);
            jedis.set(String.valueOf(auctionItem.getId()), auction);
            System.out.println(auction);
        }
        jedis.close();
    }

    public static AuctionItem getAuctionItem(int id) {
        if (auctionItems.containsKey(id)) {
            return auctionItems.get(id);
        }
        return null;
    }

    public static void addAuctionItem(AuctionItem auctionItem) {
        auctionItems.put(auctionItem.getId(), auctionItem);
    }

    public static void removeAuctionItem(int id) {
        if (auctionItems.containsKey(id)) {
            auctionItems.remove(id);
        }
    }

    public static Map<Player, AuctionItem> getPendingAuctions() {
        return pendingAuctions;
    }

    public static AuctionItem getPendingAuction(Player player) {
        if (pendingAuctions.containsKey(player)) {
            return pendingAuctions.get(player);
        } else {
            return null;
        }
    }

    public static boolean addPendingAuction(Player player, AuctionItem auctionItem) {
        if (pendingAuctions.containsKey(player)) {
            return false;
        } else {
            pendingAuctions.put(player, auctionItem);
            return true;
        }
    }

    public static void removePendingAuction(Player player) {
        if (pendingAuctions.containsKey(player)) {
            pendingAuctions.remove(player);
        }
    }
}
