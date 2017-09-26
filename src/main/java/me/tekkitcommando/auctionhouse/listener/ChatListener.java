package me.tekkitcommando.auctionhouse.listener;

import me.tekkitcommando.auctionhouse.auction.AuctionManager;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (AuctionManager.getPendingAuctions().containsKey(player)) {
            event.setCancelled(true);

            if (AuctionManager.getPendingAuction(player).getAmount() == 0) {
                if (NumberUtils.isNumber(event.getMessage())) {
                    if (player.getInventory().containsAtLeast(AuctionManager.getPendingAuction(player).getItem(), Integer.valueOf(event.getMessage()))) {
                        AuctionManager.getPendingAuction(player).setAmount(Integer.valueOf(event.getMessage()));
                        player.sendMessage(ChatColor.GREEN + "Amount added! Now please specify your price.");
                    } else {
                        player.sendMessage(ChatColor.RED + "You must have that much in your inventory! Please type the amount again.");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "The amount must be a number (obiously), try again!");
                }

            } else if (AuctionManager.getPendingAuction(player).getPrice() == 0.0 && AuctionManager.getPendingAuction(player).getAmount() > 0) {

                if (NumberUtils.isNumber(event.getMessage())) {
                    AuctionManager.getPendingAuction(player).setPrice(Double.valueOf(event.getMessage()));
                    player.sendMessage(ChatColor.GREEN + "Price set! Lastly, please specify the time (in hours) you want your auction to last.");
                } else {
                    player.sendMessage(ChatColor.RED + "The price must be a number (obiously), try again!");
                }

            } else if (AuctionManager.getPendingAuction(player).getHours() == 1 && AuctionManager.getPendingAuction(player).getPrice() > 0.0) {

                if (NumberUtils.isNumber(event.getMessage())) {
                    AuctionManager.getPendingAuction(player).setHours(Integer.valueOf(event.getMessage()));

                    if (AuctionManager.getPendingAuction(player).getScheduledTask() != null) {
                        AuctionManager.getPendingAuction(player).getScheduledTask().cancel(true);
                    }

                    AuctionManager.getPendingAuction(player).setupAuction();

                    AuctionManager.addAuctionItem(AuctionManager.getPendingAuction(player));
                    AuctionManager.removePendingAuction(player);

                    player.sendMessage(ChatColor.GREEN + "[Auction House] Auction added!");
                }
            }
        }
    }
}
