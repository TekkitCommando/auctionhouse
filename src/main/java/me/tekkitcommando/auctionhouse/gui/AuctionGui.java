package me.tekkitcommando.auctionhouse.gui;

import me.tekkitcommando.auctionhouse.auction.AuctionItem;
import me.tekkitcommando.auctionhouse.auction.AuctionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Arrays;

public class AuctionGui {

    private static Inventory auctionInv;

    public static Inventory getInventory() {
        return auctionInv;
    }

    /**
     * Opens the gui for the specified player at the specified page
     *
     * @param player Player that will see the gui
     * @param page   Page of the gui that will be seen
     */
    public static void openAuctionGui(Player player, int page) {
        auctionInv = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Auction House");

        try {

            if (AuctionManager.getAuctionItems().size() <= 44) {

                for (int slot = 0; slot < AuctionManager.getAuctionItems().size(); slot++)
                    setInventoryItem(auctionInv, slot);

            } else {

                if (page > 1) {

                    for (int slot = 44 + page; slot <= 44 * page; slot++)
                        setInventoryItem(auctionInv, slot);

                } else {

                    for (int slot = 0; slot <= 44; slot++)
                        setInventoryItem(auctionInv, slot);

                }
            }

            setControlButtons(auctionInv, page);
            player.openInventory(auctionInv);
        } catch (JedisException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the gui slot to the retrieved Auction Item based on
     * the slot.
     *
     * @param inv  The gui to set the auction item in
     * @param slot The slot where the auction item will be
     */
    private static void setInventoryItem(Inventory inv, int slot) {

        AuctionItem auctionItem = AuctionManager.getAuctionItem(slot);
        ItemStack is;

        if (auctionItem.getItem() != null) {
            is = auctionItem.getItem();

            if (auctionItem.getAmount() > 1) {
                is.setAmount(auctionItem.getAmount());
            }

        } else {
            return;
        }

        ItemMeta im = is.getItemMeta();
        im.setLore(Arrays.asList(String.valueOf(auctionItem.getId()), String.valueOf(auctionItem.getPrice()), "Seller: " + Bukkit.getPlayer(auctionItem.getSeller()).getName()));

        is.setItemMeta(im);

        inv.setItem(slot, is);
    }

    private static void setControlButtons(Inventory inv, int page) {
        if (page - 1 != 0) {
            ItemStack prev = new ItemStack(Material.ARROW);
            ItemMeta prevMeta = prev.getItemMeta();

            prevMeta.setDisplayName(String.valueOf(page - 1));
            prev.setItemMeta(prevMeta);

            inv.setItem(45, prev);
        }

        ItemStack add = new ItemStack(Material.ANVIL);
        ItemMeta addMeta = add.getItemMeta();

        addMeta.setDisplayName("Add Auction");
        add.setItemMeta(addMeta);

        inv.setItem(49, add);

        if (page * 44 < AuctionManager.getAuctionItems().size()) {
            ItemStack next = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = next.getItemMeta();

            nextMeta.setDisplayName(String.valueOf(page + 1));
            next.setItemMeta(nextMeta);

            inv.setItem(53, next);
        }
    }
}
