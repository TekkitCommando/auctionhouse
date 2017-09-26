package me.tekkitcommando.auctionhouse.command;

import me.tekkitcommando.auctionhouse.gui.AuctionGui;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AuctionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("auction") && args.length == 0) {
                AuctionGui.openAuctionGui(player, 1);

            } else if (cmd.getName().equalsIgnoreCase("auction") && args.length > 0) {

                if (NumberUtils.isNumber(args[0])) {
                    int page = Integer.parseInt(args[0]);
                    AuctionGui.openAuctionGui(player, page);

                } else {
                    player.sendMessage(ChatColor.RED + "[Auction House] Incorrect arguments!\n Proper format: /auction <page>");
                }
            }

        } else {
            sender.sendMessage("You must be a player to use that command!");
        }

        return true;
    }
}
