package me.tekkitcommando.auctionhouse.listener;

import me.tekkitcommando.auctionhouse.AuctionHouse;
import me.tekkitcommando.auctionhouse.auction.AuctionItem;
import me.tekkitcommando.auctionhouse.auction.AuctionManager;
import me.tekkitcommando.auctionhouse.gui.AuctionGui;
import me.tekkitcommando.auctionhouse.gui.SellGui;
import me.tekkitcommando.auctionhouse.redis.RedisManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import redis.clients.jedis.Jedis;

public class AuctionListener implements Listener {

    /**
     * Checks if the event was caused by clicking in the auction gui if so,
     * get the item and slot clicked on and decide whether to sell clicked item
     * or if it is a control item such as the 'Add Auction' item.
     *
     * @param event The event that is called when an inventory is clicked in
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equals(AuctionGui.getInventory().getName())) {
            Player buyer = (Player) event.getWhoClicked();
            event.setCancelled(true);

            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
                return;
            }

            ItemStack clicked = event.getCurrentItem();

            if (event.getSlot() <= 44 && clicked.hasItemMeta()) {
                int auctionId = Integer.valueOf(clicked.getItemMeta().getLore().get(0));

                if (AuctionManager.getAuctionItem(auctionId) != null) {
                    AuctionItem auctionItem = AuctionManager.getAuctionItem(auctionId);

                    if (buyer == Bukkit.getPlayer(auctionItem.getSeller())) {
                        buyer.sendMessage(ChatColor.RED + "[Auction House] You cannot purchase your own auction item!");
                        return;
                    }

                    if (purchaseItem(buyer, auctionItem)) {
                        Jedis jedis = RedisManager.getPool().getResource();

                        sellItem(auctionItem);
                        buyer.closeInventory();
                        AuctionManager.removeAuctionItem(auctionId);
                        RedisManager.getRedisKeys().remove(String.valueOf(auctionId));

                        jedis.del(String.valueOf(auctionId));
                        jedis.close();
                    }
                }

            } else {

                switch (event.getSlot()) {
                    case 45:
                    case 53:
                        int page = Integer.valueOf(clicked.getItemMeta().getDisplayName());

                        buyer.closeInventory();

                        AuctionGui.openAuctionGui(buyer, page);
                        break;

                    case 49:
                        buyer.closeInventory();
                        SellGui.openSellGui(buyer);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Checks if player has enough money and if he or she does then
     * take the money and give him or her the auction item
     *
     * @param buyer       The buyer of the auction
     * @param auctionItem The item being auctioned
     */
    private boolean purchaseItem(Player buyer, AuctionItem auctionItem) {
        if (AuctionHouse.getEconomy().has(buyer, auctionItem.getPrice())) {
            AuctionHouse.getEconomy().withdrawPlayer(buyer, auctionItem.getPrice());

            if (auctionItem.getAmount() > 1) {
                ItemStack item = auctionItem.getItem();
                item.setAmount(auctionItem.getAmount());
                buyer.getInventory().addItem(item);

            } else {
                buyer.getInventory().addItem(auctionItem.getItem());
            }

            if (auctionItem.getItem().getItemMeta() == null) {
                buyer.sendMessage(ChatColor.GREEN + "[Auction House] You have successfully bought the auction(s) " + auctionItem.getItem().getType() + "!");

            } else {
                buyer.sendMessage(ChatColor.GREEN + "[Auction House] You have successfully bought the auction(s) " + auctionItem.getItem().getItemMeta().getDisplayName() + "!");
            }

            return true;

        } else {
            buyer.sendMessage(ChatColor.RED + "[Auction House] You don't have enough money for this auction!");
            return false;
        }
    }

    /**
     * Gives the seller of the specified auction the money from the purchase
     * and sends a message notifying the player if he or she is online
     *
     * @param auctionItem The auction being auctioned
     */
    private void sellItem(AuctionItem auctionItem) {
        AuctionHouse.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(auctionItem.getSeller()), auctionItem.getPrice());

        if (Bukkit.getOfflinePlayer(auctionItem.getSeller()).isOnline()) {
            Player player = Bukkit.getServer().getPlayer(auctionItem.getSeller());

            if (auctionItem.getItem().getItemMeta() == null) {
                player.sendMessage(ChatColor.GREEN + "[Auction House] Your auction(s) " + auctionItem.getItem().getItemMeta().getDisplayName() + "was purchased!");

            } else {
                player.sendMessage(ChatColor.GREEN + "[Auction House] Your auction(s) " + auctionItem.getItem().getType() + "was purchased!");
            }
        }
    }
}
