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

    /**
     * Creates the item based on the material and then adds an expiration timer to the
     * auction
     */
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

    /**
     * Allows access to the item that is being auctioned
     *
     * @return The item being auctioned
     */
    public ItemStack getItem() {
        return itemStack;
    }

    /**
     * Sets the auction item to the specified item
     */
    public void setItem(ItemStack item) {
        this.itemStack = item;
    }

    /**
     * Allows access to the string that specifies the item type
     *
     * @return The item type (material)
     */
    public String getItemType() {
        return item;
    }

    /**
     * Sets the item type to the specified item type
     */
    public void setItemType(String item) {
        this.item = item;
    }

    /**
     * Allows access to the id of the auction
     *
     * @return The id of the auction
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the auction id to the specified id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Allows access to the amount of the item being auctioned
     *
     * @return The amount of the item being auctioned
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the auction item to the specified amount
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Allows access to the UUID of the seller
     *
     * @return The uuid of the seller
     */
    public UUID getSeller() {
        return seller;
    }

    /**
     * Sets the seller of the auction item to the specified player UUID
     */
    public void setSeller(UUID seller) {
        this.seller = seller;
    }

    /**
     * Allows access to the price of the item being auctioned
     *
     * @return The price of the item being auctioned
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the auction item to the specified price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Allows access to the hours the item was set to expire in, this is
     * not a time left function.
     *
     * @see AuctionItem#getTimeLeft
     *
     * @return The hours the item was set to expire in
     */
    public int getHours() {
        return hours;
    }

    /**
     * Sets the hours that item expires in to the specified hours
     */
    public void setHours(int hours) {
        this.hours = hours;
    }

    /**
     * Allows access to the scheduled task that handles expiration of the auction
     *
     * @return The scheduled task that handles expiration
     */
    public ScheduledFuture getScheduledTask() {
        return scheduledTask;
    }

    /**
     * Sets the scheduled task to to the specified ScheduledFuture task
     */
    public void setScheduledTask(ScheduledFuture scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    /**
     * Allows access to the scheduler used by the scheduled task
     *
     * @return The scheduler used by the scheduled task
     */
    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    /**
     * Allows retrieval of the time left that the auction has before it expires
     *
     * @return The amount of time before the auction expires
     */
    public long getTimeLeft() {
        return scheduledTask.getDelay(TimeUnit.HOURS);
    }
}
