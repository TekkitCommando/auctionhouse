package me.tekkitcommando.auctionhouse.gui;

import me.tekkitcommando.auctionhouse.auction.AuctionItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SellGui {

    private static Inventory sellInv;

    public static void openSellGui(Player player, AuctionItem auctionItem, int amount,  int itemSlot) {
        int slot = 0;

        if (auctionItem == null) {
            sellInv = Bukkit.createInventory(null, player.getInventory().getSize(), ChatColor.GREEN + "Click The Item To Sell");
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                if (player.getInventory().getItem(i) != null && player.getInventory().getItem(i).getType() != Material.AIR) {
                    sellInv.setItem(slot, player.getInventory().getItem(i));
                    slot++;
                }
            }
            player.openInventory(sellInv);
            return;
        } else if (amount == 0) {
            sellInv = Bukkit.createInventory(null, 27, ChatColor.GREEN + "How many of that item?");
            if (player.getInventory().getItem(itemSlot).getAmount() > 9 && player.getInventory().getItem(itemSlot).getAmount() < 18) {
                for (int i = 1; i <= player.getInventory().getItem(itemSlot).getAmount(); i++) {
                    ItemStack item = auctionItem.getItem();
                    item.setAmount(i);
                    sellInv.setItem(i, item);
                }
                ItemStack item = auctionItem.getItem();
                item.setAmount(32);
                sellInv.setItem(19, item);

                ItemStack item2 = auctionItem.getItem();
                item.setAmount(64);
                sellInv.setItem(22, item2);

                ItemStack item3 = auctionItem.getItem();
                item.setAmount(128);
                sellInv.setItem(24, item3);

                ItemStack item4 = auctionItem.getItem();
                item.setAmount(256);
                sellInv.setItem(26, item4);

                player.openInventory(sellInv);
                return;
            }
        }

        // Price, Time

        player.openInventory(sellInv);
    }
}
