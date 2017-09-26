package me.tekkitcommando.auctionhouse.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SellGui {

    private static Inventory sellInv;

    public static Inventory getInventory() {
        return sellInv;
    }

    public static void openSellGui(Player player) {
        int slot = 0;
        int size = 9;

        if (player.getInventory().getSize() > 9) {

            if (player.getInventory().getSize() % 9 == 0) {
                size = player.getInventory().getSize();

            } else {
                int remainder = player.getInventory().getSize() % 9;
                int extraSlots = 9 - remainder;
                size = player.getInventory().getSize() + extraSlots;
            }
        }

        sellInv = Bukkit.createInventory(null, size, ChatColor.GREEN + "Click The Item To Sell");

        for (int i = 0; i < player.getInventory().getSize(); i++) {

            if (player.getInventory().getItem(i) != null && player.getInventory().getItem(i).getType() != Material.AIR) {
                sellInv.setItem(slot, player.getInventory().getItem(i));
                slot++;

            }
        }

        player.openInventory(sellInv);
    }
}
