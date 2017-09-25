package me.tekkitcommando.auctionhouse.auction;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.*;

public class AuctionItem {

    private ItemStack item;
    private int id;
    private int amount;
    private UUID seller;
    private double price;
    private int hours;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture scheduledTask;

    public AuctionItem(ItemStack item, final int id, int amount, UUID seller, double price, int hours) {
        this.item = item;
        this.id = id;
        this.amount = amount;
        this.seller = seller;
        this.price = price;
        this.hours = hours;

        AuctionManager.addAuctionItem(this);

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
