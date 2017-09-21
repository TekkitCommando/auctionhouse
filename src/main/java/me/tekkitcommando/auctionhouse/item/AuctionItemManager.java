package me.tekkitcommando.auctionhouse.item;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AuctionItemManager {

    private static List<AuctionItem> auctionItems = new ArrayList<>();

    public static AuctionItem getAuctionItem(ItemStack item) {
        for (AuctionItem auctionItem : auctionItems) {
            if (auctionItem.getItem() == item) {
                return auctionItem;
            }
        }

        return null;
    }

    public static void removeAuctionItem(AuctionItem item) {
        if (auctionItems.contains(item)) {
            auctionItems.remove(item);
        }
    }
}
