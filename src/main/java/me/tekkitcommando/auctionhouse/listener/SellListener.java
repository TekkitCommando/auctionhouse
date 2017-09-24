package me.tekkitcommando.auctionhouse.listener;

import me.tekkitcommando.auctionhouse.auction.AuctionItem;
import me.tekkitcommando.auctionhouse.gui.SellGui;
import me.tekkitcommando.auctionhouse.redis.RedisManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SellListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equals(ChatColor.GREEN + "Click The Item To Sell")) {
            ItemStack clicked = event.getCurrentItem();
            if (clicked.getType() != Material.AIR && clicked.getType() != null) {
                event.setCancelled(true);
                AuctionItem auctionItem = new AuctionItem(clicked, RedisManager.getRedisKeys().size() + 1, 0, event.getWhoClicked().getUniqueId(), 0.0);
                SellGui.openSellGui((Player) event.getWhoClicked(), auctionItem, 0, 0);
            }
        } else if (event.getInventory().getName().equals(ChatColor.GREEN + "How many of that item?")) {
            ItemStack clicked = event.getCurrentItem();
            if (clicked.getType() != Material.AIR && clicked.getType() != null) {
                event.setCancelled(true);
                AuctionItem auctionItem = new AuctionItem(clicked, RedisManager.getRedisKeys().size() + 1, clicked.getAmount(), event.getWhoClicked().getUniqueId(), 0.0);
                SellGui.openSellGui((Player) event.getWhoClicked(), auctionItem, auctionItem.getAmount(), 0);
            }
        }
    }
}
