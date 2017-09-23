package me.tekkitcommando.auctionhouse.auction;

import com.google.gson.Gson;
import me.tekkitcommando.auctionhouse.redis.RedisManager;

import java.util.HashMap;
import java.util.Map;

public class AuctionManager {

    private static Map<Integer, AuctionItem> auctionItems = new HashMap<>();

    public static void getItemsFromDatabase() {
        Gson gson = new Gson();

        for (String key : RedisManager.getRedisKeys()) {
            String item = RedisManager.getJedis().get(key);
            AuctionItem auctionItem = gson.fromJson(item, AuctionItem.class);

            auctionItems.put(auctionItem.getId(), auctionItem);
        }
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
}
