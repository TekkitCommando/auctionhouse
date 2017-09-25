package me.tekkitcommando.auctionhouse.listener;

import me.tekkitcommando.auctionhouse.AuctionHouse;
import me.tekkitcommando.auctionhouse.auction.AuctionItem;
import me.tekkitcommando.auctionhouse.auction.AuctionManager;
import me.tekkitcommando.auctionhouse.gui.AuctionGui;
import me.tekkitcommando.auctionhouse.gui.SellGui;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AuctionListener implements Listener {

    /**
     * Checks if player is clicking in the auction inventory and manages
     * what they click.
     *
     * @param event Event called when an inventory is clicked in
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equals(AuctionGui.getInventory().getName())) {
            ItemStack clicked = event.getCurrentItem();
            if (clicked.getType() != Material.AIR || clicked.getType() != null) {
                event.setCancelled(true);
                if (event.getSlot() != 45 && event.getSlot() != 49 && event.getSlot() != 53) {
                    Player buyer = (Player) event.getWhoClicked();
                    int auctionId = Integer.valueOf(event.getCurrentItem().getItemMeta().getLore().get(0));
                    AuctionItem auctionItem = AuctionManager.getAuctionItem(auctionId);

                    if (auctionItem != null) {

                        if (purchaseItem(buyer, auctionItem)) {
                            sellItem(auctionItem);
                            buyer.closeInventory();
                            AuctionManager.removeAuctionItem(auctionId);
                        }
                    }
                } else {
                    switch (event.getSlot()) {
                        case 45:
                        case 53:
                            int page = Integer.valueOf(event.getCurrentItem().getItemMeta().getDisplayName());

                            event.getWhoClicked().closeInventory();

                            AuctionGui.openAuctionGui((Player) event.getWhoClicked(), page);
                            break;

                        case 49:
                            event.getWhoClicked().closeInventory();
                            SellGui.openSellGui((Player) event.getWhoClicked());
                            break;
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
        AuctionHouse.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(auctionItem.getSeller()), auctionItem.getPrice());

        if (Bukkit.getOfflinePlayer(auctionItem.getSeller()).isOnline()) {
            Player player = Bukkit.getServer().getPlayer(auctionItem.getSeller());
            player.sendMessage(ChatColor.GREEN + "[Auction House] Your auction(s) " + auctionItem.getItem().getItemMeta().getDisplayName() + "was purchased!");
        }
    }
}
