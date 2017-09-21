package me.tekkitcommando.auctionhouse.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AuctionItem {

    private ItemStack item;
    private int id;
    private int amount;
    private Player seller;
    private double price;

    public AuctionItem(ItemStack item, int id, int amount, Player seller, double price) {
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

    public Player getSeller() {
        return seller;
    }

    public void setSeller(Player seller) {
        this.seller = seller;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
