package me.tekkitcommando.auctionhouse.listener;

import me.tekkitcommando.auctionhouse.auction.AuctionItem;
import me.tekkitcommando.auctionhouse.auction.AuctionManager;
import me.tekkitcommando.auctionhouse.gui.SellGui;
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
            Player seller = (Player) event.getWhoClicked();
            event.setCancelled(true);

            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
                return;
            }

            ItemStack clicked = event.getCurrentItem();

            if (clicked.getAmount() > 1) {
                AuctionItem auctionItem = new AuctionItem();
                auctionItem.setItemType(clicked.getType().toString());
                auctionItem.setId(AuctionManager.getAuctionItems().size());
                auctionItem.setAmount(0);
                auctionItem.setSeller(seller.getUniqueId());
                auctionItem.setPrice(0.0);
                auctionItem.setHours(1);

                if (AuctionManager.addPendingAuction((Player) seller, auctionItem)) {
                    seller.closeInventory();
                    seller.sendMessage("Please type how many of the item in chat.");
                }

            } else {
                AuctionItem auctionItem = new AuctionItem();
                auctionItem.setItemType(clicked.getType().toString());
                auctionItem.setId(AuctionManager.getAuctionItems().size());
                auctionItem.setAmount(1);
                auctionItem.setSeller(seller.getUniqueId());
                auctionItem.setPrice(0.0);
                auctionItem.setHours(1);

                if (AuctionManager.addPendingAuction((Player) seller, auctionItem)) {
                    seller.closeInventory();
                    seller.sendMessage("Please type how much you want to sell it for.");
                }
            }
        }
    }
}
