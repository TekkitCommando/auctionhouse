package me.tekkitcommando.auctionhouse.listener;

import me.tekkitcommando.auctionhouse.AuctionHouse;
import me.tekkitcommando.auctionhouse.item.AuctionItem;
import me.tekkitcommando.auctionhouse.item.AuctionItemManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class AuctionListener implements Listener {

    /**
     * Checks if player is clicking in the auction inventory and manages
     * what they click.
     *
     * @param event Event called when an inventory is clicked in
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equalsIgnoreCase(ChatColor.stripColor("Auction House"))) {
            if (event.getCurrentItem().getType() != Material.AIR || event.getCurrentItem() != null) {
                Player buyer = (Player) event.getWhoClicked();
                AuctionItem auctionItem = AuctionItemManager.getAuctionItem(event.getCurrentItem());

                if (auctionItem != null) {

                    if (purchaseItem(buyer, auctionItem)) {
                        sellItem(auctionItem);
                        AuctionItemManager.removeAuctionItem(auctionItem);
                    }
                }
            }
        }
    }

    /**
     * Checks if player has enough money and if he or she does then
     * take the money and give him or her the item
     *
     * @param buyer       The buyer of the item
     * @param auctionItem The item being auctioned
     */
    private boolean purchaseItem(Player buyer, AuctionItem auctionItem) {
        if (AuctionHouse.getEcon().has(buyer, auctionItem.getPrice())) {
            AuctionHouse.getEcon().withdrawPlayer(buyer, auctionItem.getPrice());
            buyer.getInventory().addItem(auctionItem.getItem());
            buyer.sendMessage(ChatColor.GREEN + "[Auction House] You have successfully bought the item(s) " + auctionItem.getItem().getItemMeta().getDisplayName() + "!");
            return true;
        } else {
            buyer.sendMessage(ChatColor.RED + "[Auction House] You don't have enough money for this item!");
            return false;
        }
    }

    /**
     * Gives the seller of the specified auction item money from the purchase
     *
     * @param auctionItem The item being auctioned
     */
    private void sellItem(AuctionItem auctionItem) {
        AuctionHouse.getEcon().depositPlayer(auctionItem.getSeller(), auctionItem.getPrice());
        auctionItem.getSeller().sendMessage(ChatColor.GREEN + "[Auction House] Your item(s) " + auctionItem.getItem().getItemMeta().getDisplayName() + "was purchased!");
    }
}
