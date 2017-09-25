package me.tekkitcommando.auctionhouse.listener;

import me.tekkitcommando.auctionhouse.auction.AuctionManager;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        if (AuctionManager.getPendingAuctions().containsKey(event.getPlayer())) {
            if (AuctionManager.getPendingAuction(event.getPlayer()).getAmount() == 0) {
                if (NumberUtils.isNumber(event.getMessage())) {
                    if (event.getPlayer().getInventory().containsAtLeast(AuctionManager.getPendingAuction(event.getPlayer()).getItem(), Integer.valueOf(event.getMessage()))) {
                        AuctionManager.getPendingAuction(event.getPlayer()).setAmount(Integer.valueOf(event.getMessage()));
                        event.getPlayer().sendMessage(ChatColor.GREEN + "Amount added! Now please specify your price.");
                    } else {
                        event.getPlayer().sendMessage(ChatColor.RED + "You must have that much in your inventory! Please type the amount again.");
                    }
                } else {
                    event.getPlayer().sendMessage(ChatColor.RED + "The amount must be a number (obiously), try again!");
                }
            } else if (AuctionManager.getPendingAuction(event.getPlayer()).getPrice() == 0.0 && AuctionManager.getPendingAuction(event.getPlayer()).getAmount() > 0) {
                if (NumberUtils.isNumber(event.getMessage())) {
                    AuctionManager.getPendingAuction(event.getPlayer()).setPrice(Double.valueOf(event.getMessage()));
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Price set! Lastly, please specify the time (in hours) you want your auction to last.");
                } else {
                    event.getPlayer().sendMessage(ChatColor.RED + "The price must be a number (obiously), try again!");
                }
            } else if (AuctionManager.getPendingAuction(event.getPlayer()).getHours() == 1 && AuctionManager.getPendingAuction(event.getPlayer()).getPrice() > 0.0) {
                if (NumberUtils.isNumber(event.getMessage())) {
                    AuctionManager.getPendingAuction(event.getPlayer()).setHours(Integer.valueOf(event.getMessage()));
                    AuctionManager.getPendingAuction(event.getPlayer()).getScheduledTask().cancel(true);

                    AuctionManager.getPendingAuction(event.getPlayer()).setScheduledTask(AuctionManager.getPendingAuction(event.getPlayer()).getScheduler().schedule(new Callable() {
                        @Override
                        public Object call() throws Exception {
                            AuctionManager.removeAuctionItem(AuctionManager.getPendingAuction(event.getPlayer()).getId());
                            return AuctionManager.getAuctionItems().containsKey(AuctionManager.getPendingAuction(event.getPlayer()).getId());
                        }
                    }, Integer.valueOf(event.getMessage()), TimeUnit.HOURS));

                    AuctionManager.addAuctionItem(AuctionManager.getPendingAuction(event.getPlayer()));
                    AuctionManager.removePendingAuction(event.getPlayer());

                    event.getPlayer().sendMessage(ChatColor.GREEN + "[Auction House] Auction added!");
                }
            }
        }
    }
}
