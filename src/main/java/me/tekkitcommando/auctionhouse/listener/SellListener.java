package me.tekkitcommando.auctionhouse.listener;

import me.tekkitcommando.auctionhouse.auction.AuctionItem;
import me.tekkitcommando.auctionhouse.auction.AuctionManager;
import me.tekkitcommando.auctionhouse.gui.SellGui;
import me.tekkitcommando.auctionhouse.redis.RedisManager;
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
            Player seller = (Player) event.getWhoClicked();

            if (clicked.getType() != Material.AIR && clicked.getType() != null) {
                event.setCancelled(true);

                if (clicked.getAmount() > 1) {
                    AuctionItem auctionItem = new AuctionItem();
                    auctionItem.setItemType(clicked.getType().toString());
                    auctionItem.setId(RedisManager.getRedisKeys().size());
                    auctionItem.setAmount(0);
                    auctionItem.setSeller(seller.getUniqueId());
                    auctionItem.setPrice(0.0);
                    auctionItem.setHours(1);

                    if (AuctionManager.addPendingAuction((Player) seller, auctionItem)) {
                        seller.getInventory().remove(auctionItem.getItem());
                        seller.closeInventory();
                        seller.sendMessage("Please type how many of the item in chat.");
                    }

                } else {
                    AuctionItem auctionItem = new AuctionItem();
                    auctionItem.setItemType(clicked.getType().toString());
                    auctionItem.setId(RedisManager.getRedisKeys().size());
                    auctionItem.setAmount(1);
                    auctionItem.setSeller(seller.getUniqueId());
                    auctionItem.setPrice(0.0);
                    auctionItem.setHours(1);

                    if (AuctionManager.addPendingAuction((Player) seller, auctionItem)) {
                        seller.getInventory().remove(auctionItem.getItem());
                        seller.closeInventory();
                        seller.sendMessage("Please type how much you want to sell it for.");
                    }
                }
            }
        }
    }
}
