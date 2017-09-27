package me.tekkitcommando.auctionhouse.command;

import me.tekkitcommando.auctionhouse.gui.AuctionGui;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AuctionCommand implements CommandExecutor {

    /**
     * Checks whether the command send is named "auction", if it is check whether
     * there is a sub command for a page and it is a number, if it is than open the gui
     * at the page, if it is not a number send a message with the proper format. Also if there
     * is not sub arguments specified, open the first page of the gui
     *
     * Also checks whether the sender of the command is a player, if not than the gui will not open
     * and a message will be send the to the sender
     *
     * @param sender The sender of the command
     * @param cmd The command
     * @param cmdLabel The command label
     * @param args The arguments sent with the command
     *
     * @return Return to the console
     */
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
