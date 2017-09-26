package me.tekkitcommando.auctionhouse.auction;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.*;

public class AuctionItem {

    private String item;
    private transient ItemStack itemStack;
    private int id;
    private int amount;
    private UUID seller;
    private double price;
    private int hours;
    private transient ScheduledExecutorService scheduler;
    private transient ScheduledFuture scheduledTask;

    public void setupAuction() {
        itemStack = new ItemStack(Material.getMaterial(item));

        scheduler = Executors.newScheduledThreadPool(5);
        scheduledTask = scheduler.schedule(new Callable() {
            @Override
            public Object call() throws Exception {
                AuctionManager.removeAuctionItem(id);
                return AuctionManager.getAuctionItems().containsKey(id);
            }
        }, hours, TimeUnit.HOURS);
    }

    public ItemStack getItem() {
        return itemStack;
    }

    public String getItemType() {
        return item;
    }

    public void setItemType(String item) {
        this.item = item;
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

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public ScheduledFuture getScheduledTask() {
        return scheduledTask;
    }

    public void setScheduledTask(ScheduledFuture scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }
}
