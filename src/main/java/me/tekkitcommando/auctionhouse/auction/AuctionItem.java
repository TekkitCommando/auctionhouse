package me.tekkitcommando.auctionhouse.auction;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class AuctionItem {

    private ItemStack item;
    private int id;
    private int amount;
    private UUID seller;
    //    private OfflinePlayer seller;
    private double price;

    public AuctionItem(ItemStack item, int id, int amount, UUID seller, double price) {
        this.item = item;
        this.id = id;
        this.amount = amount;
        this.seller = seller;
        this.price = price;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

//    public OfflinePlayer getSeller() {
//        return seller;
//    }

//    public void setSeller(OfflinePlayer seller) {
//        this.seller = seller;
//    }

    public UUID getSeller() {
        return seller;
    }

    public void setSeller(UUID seller) {
        this.seller = seller;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
