package me.tekkitcommando.auctionhouse.inventory;

import me.tekkitcommando.auctionhouse.AuctionHouse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuctionInventory {

    private static List<String> redisKeys = new ArrayList<>();

    public static void openAuctionGui(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GREEN + "Auction House");

        try (Jedis jedis = AuctionHouse.getPool().getResource()){
            jedis.auth("the-password");

            for (String key : jedis.keys("*")) {
                if (!redisKeys.contains(key)) {
                    redisKeys.add(key);
                }
            }

            if (redisKeys.size() < 44) {
                for (int slot = 0; slot < redisKeys.size(); slot++) {

                }
            } else {
                for (int slot = 0; slot <= 44; slot++) {
                    String key = redisKeys.get(slot);
                    Material mat = Material.getMaterial(jedis.hget(key, "item"));
                    int amount = Integer.valueOf(jedis.hget(key, "amount"));

                    ItemStack is = new ItemStack(mat, amount);

                    ItemMeta im = is.getItemMeta();
                    im.setLore(Arrays.asList("Price: " + jedis.hget(key, "price"), "Seller: " + jedis.hget(key, "seller")));

                    inv.setItem(slot, is);
                }
            }

            jedis.close();
        }
    }
}
