package me.tekkitcommando.auctionhouse.gui;

import me.tekkitcommando.auctionhouse.auction.AuctionItem;
import me.tekkitcommando.auctionhouse.auction.AuctionManager;
import me.tekkitcommando.auctionhouse.redis.RedisManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
            }
            setControlButtons(inv);
            player.openInventory(inv);

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
        ItemStack is;

        if (auctionItem != null) {
            is = auctionItem.getItem();
        } else {
            return;
        }

        ItemMeta im = is.getItemMeta();
        im.setLore(Arrays.asList("ID: " + auctionItem.getId(), "Price: " + auctionItem.getPrice(), "Seller: " + Bukkit.getPlayer(auctionItem.getSeller()).getName()));

        is.setItemMeta(im);

        inv.setItem(slot, is);
    }

    private static void setControlButtons(Inventory inv) {
        ItemStack prev = new ItemStack(Material.ARROW);
        ItemMeta prevMeta = prev.getItemMeta();
        prevMeta.setDisplayName("Previous Page");
        prev.setItemMeta(prevMeta);

        ItemStack add = new ItemStack(Material.ANVIL);
        ItemMeta addMeta = add.getItemMeta();
        addMeta.setDisplayName("Add Auction");
        add.setItemMeta(addMeta);

        ItemStack next = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = next.getItemMeta();
        nextMeta.setDisplayName("Next Page");
        next.setItemMeta(nextMeta);

        inv.setItem(45, prev);
        inv.setItem(49, add);
        inv.setItem(53, next);
    }
}
