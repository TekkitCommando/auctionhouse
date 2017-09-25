package me.tekkitcommando.auctionhouse.listener;

import me.tekkitcommando.auctionhouse.auction.AuctionItem;
import me.tekkitcommando.auctionhouse.auction.AuctionManager;
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
        if (event.getInventory().getName().equals(SellGui.getInventory().getName())) {
            ItemStack clicked = event.getCurrentItem();
            if (clicked.getType() != Material.AIR && clicked.getType() != null) {
                event.setCancelled(true);
                if(clicked.getAmount() > 1) {
                    AuctionItem auctionItem = new AuctionItem(clicked, RedisManager.getRedisKeys().size() + 1, 0, event.getWhoClicked().getUniqueId(), 0.0, 0);

                    if(AuctionManager.addPendingAuction((Player) event.getWhoClicked(), auctionItem)) {
                        event.getWhoClicked().sendMessage("Please type how many of the item in chat.");
                    }
                } else {
                    AuctionItem auctionItem = new AuctionItem(clicked, RedisManager.getRedisKeys().size() + 1, 1, event.getWhoClicked().getUniqueId(), 0.0, 1);

                    if(AuctionManager.addPendingAuction((Player) event.getWhoClicked(), auctionItem)) {
                        event.getWhoClicked().sendMessage("Please type how much you want to sell it for.");
                    }
                }
            }
        }
    }
}
