package me.tekkitcommando.auctionhouse.listener;

import me.tekkitcommando.auctionhouse.AuctionHouse;
import me.tekkitcommando.auctionhouse.auction.AuctionItem;
import me.tekkitcommando.auctionhouse.auction.AuctionManager;
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
                int auctionId = Integer.valueOf(event.getCurrentItem().getItemMeta().getLore().get(0));
                AuctionItem auctionItem = AuctionManager.getAuctionItem(auctionId);

                if (auctionItem != null) {

                    if (purchaseItem(buyer, auctionItem)) {
                        sellItem(auctionItem);
                        AuctionManager.removeAuctionItem(auctionId);
                    }
                }
            }
        }
    }

    /**
     * Checks if player has enough money and if he or she does then
     * take the money and give him or her the auction
     *
     * @param buyer       The buyer of the auction
     * @param auctionItem The auction being auctioned
     */
    private boolean purchaseItem(Player buyer, AuctionItem auctionItem) {
        if (AuctionHouse.getEconomy().has(buyer, auctionItem.getPrice())) {
            AuctionHouse.getEconomy().withdrawPlayer(buyer, auctionItem.getPrice());
            buyer.getInventory().addItem(auctionItem.getItem());
            buyer.sendMessage(ChatColor.GREEN + "[Auction House] You have successfully bought the auction(s) " + auctionItem.getItem().getItemMeta().getDisplayName() + "!");
            return true;
        } else {
            buyer.sendMessage(ChatColor.RED + "[Auction House] You don't have enough money for this auction!");
            return false;
        }
    }

    /**
     * Gives the seller of the specified auction auction money from the purchase
     *
     * @param auctionItem The auction being auctioned
     */
    private void sellItem(AuctionItem auctionItem) {
        AuctionHouse.getEconomy().depositPlayer(auctionItem.getSeller(), auctionItem.getPrice());
        auctionItem.getSeller().sendMessage(ChatColor.GREEN + "[Auction House] Your auction(s) " + auctionItem.getItem().getItemMeta().getDisplayName() + "was purchased!");
    }
}
