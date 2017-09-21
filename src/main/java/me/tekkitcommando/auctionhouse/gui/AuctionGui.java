package me.tekkitcommando.auctionhouse.gui;

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

public class AuctionGui {

    private static List<String> redisKeys = new ArrayList<>();
    private static Jedis jedis;

    public static void openAuctionGui(Player player, int page) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GREEN + "Auction House");

        try (Jedis jedis = AuctionHouse.getPool().getResource()) {
            jedis.auth("the-password");

            for (String key : jedis.keys("*")) {

                if (!redisKeys.contains(key))
                    redisKeys.add(key);

            }

            if (redisKeys.size() <= 44) {

                for (int slot = 0; slot < redisKeys.size(); slot++)
                    setInventoryItem(inv, slot, jedis);

            } else {
                if (page > 1) {

                    for (int slot = 44 + page; slot <= 44 * page; slot++)
                        setInventoryItem(inv, slot, jedis);

                } else {

                    for (int slot = 0; slot <= 44; slot++)
                        setInventoryItem(inv, slot, jedis);
                }

                // Add arrows too.
            }

            jedis.close();
        }
    }

    /**
     * Sets the gui slot the correct item based on the values retrieved
     * from the key in the redis database
     *
     * @param inv   The gui to set the item in
     * @param slot  The slot where the item will be
     * @param jedis The instance of jedis for retrieving values from the database
     */
    private static void setInventoryItem(Inventory inv, int slot, Jedis jedis) {
        String key = redisKeys.get(slot);
        Material material = Material.getMaterial(jedis.hget(key, "item"));
        int amount = Integer.valueOf(jedis.hget(key, "amount"));

        ItemStack is = new ItemStack(material, amount);

        ItemMeta im = is.getItemMeta();
        im.setLore(Arrays.asList("Price: " + jedis.hget(key, "price"), "Seller: " + jedis.hget(key, "seller")));

        inv.setItem(slot, is);
    }
}
