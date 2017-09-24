package me.tekkitcommando.auctionhouse.auction;

import com.google.gson.Gson;
import me.tekkitcommando.auctionhouse.redis.RedisManager;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

public class AuctionManager {

    private static Gson gson = new Gson();
    private static Map<Integer, AuctionItem> auctionItems = new HashMap<>();

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

    public static void removeAuctionItem(int id) {
        if (auctionItems.containsKey(id)) {
            auctionItems.remove(id);
        }
    }

    public static void addAuctionItem(AuctionItem auctionItem) {
        auctionItems.put(0, auctionItem);
    }
}
