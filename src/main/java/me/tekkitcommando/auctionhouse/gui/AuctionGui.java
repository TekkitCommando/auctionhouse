package me.tekkitcommando.auctionhouse.gui;

import me.tekkitcommando.auctionhouse.auction.AuctionItem;
import me.tekkitcommando.auctionhouse.auction.AuctionManager;
import me.tekkitcommando.auctionhouse.redis.RedisManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Arrays;

public class AuctionGui {

    /**
     * Opens the gui for the specified player at the specified page
     *
     * @param player Player that will see the gui
     * @param page   Page of the gui that will be seen
     */
    public static void openAuctionGui(Player player, int page) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GREEN + "Auction House");

        try {

            if (RedisManager.getRedisKeys().size() <= 44) {

                for (int slot = 0; slot < RedisManager.getRedisKeys().size(); slot++)
                    setInventoryItem(inv, slot);

            } else {
                if (page > 1) {

                    for (int slot = 44 + page; slot <= 44 * page; slot++)
                        setInventoryItem(inv, slot);

                } else {

                    for (int slot = 0; slot <= 44; slot++)
                        setInventoryItem(inv, slot);

                }

                // Add arrows too.
            }

            RedisManager.getJedis().close();
        } catch (JedisException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the gui slot to the retrieved Auction Item based on
     * the slot.
     *
     * @param inv  The gui to set the auction item in
     * @param slot The slot where the auction item will be
     */
    private static void setInventoryItem(Inventory inv, int slot) {

        AuctionItem auctionItem = AuctionManager.getAuctionItem(slot);
        ItemStack is = null;

        if (auctionItem != null) {
            is = auctionItem.getItem();
        } else {
            return;
        }

        ItemMeta im = is.getItemMeta();
        im.setLore(Arrays.asList("ID: " + auctionItem.getId(), "Price: " + auctionItem.getPrice(), "Seller: " + auctionItem.getSeller()));

        is.setItemMeta(im);

        inv.setItem(slot, is);
    }
}
