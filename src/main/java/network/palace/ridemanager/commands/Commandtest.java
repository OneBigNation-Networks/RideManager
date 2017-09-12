package network.palace.ridemanager.commands;

import network.palace.core.Core;
import network.palace.core.command.CommandException;
import network.palace.core.command.CommandMeta;
import network.palace.core.command.CommandPermission;
import network.palace.core.command.CoreCommand;
import network.palace.core.player.CPlayer;
import network.palace.core.player.Rank;
import network.palace.ridemanager.RideManager;
import network.palace.ridemanager.handlers.Ride;
import network.palace.ridemanager.handlers.TeacupsRide;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Marc on 1/15/17.
 */
@CommandMeta(description = "Test ride system")
@CommandPermission(rank = Rank.SETTLER)
public class Commandtest extends CoreCommand {

    public Commandtest() {
        super("test");
    }

    @Override
    protected void handleCommandUnspecific(CommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            RideManager.getMovementUtil().loadRides();
        }
//        Player player = (Player) sender;
//        player.sendMessage(RideManager.getMovementUtil().getRides().get(0).getRelativeLocation(Double.parseDouble(args[0]), 4, player.getLocation()) + "");

        CPlayer player = Core.getPlayerManager().getPlayer((Player) sender);
        /*if (args.length == 1) {
            Ride ride = RideManager.getMovementUtil().getRide(args[0]);
            if (ride instanceof TeacupsRide) {
                TeacupsRide tea = (TeacupsRide) ride;
                tea.joinQueue(player);
                Bukkit.getScheduler().runTaskLater(RideManager.getInstance(), tea::start, 20L);
            } else if (ride instanceof CarouselRide) {
                CarouselRide car = (CarouselRide) ride;
                car.joinQueue(player);
                Bukkit.getScheduler().runTaskLater(RideManager.getInstance(), car::start, 20L);
            } else if (ride instanceof AerialCarouselRide) {
                AerialCarouselRide aer = (AerialCarouselRide) ride;
                aer.joinQueue(player);
                Bukkit.getScheduler().runTaskLater(RideManager.getInstance(), aer::start, 20L);
            } else if (ride instanceof SignRide) {
                SignRide s = (SignRide) ride;
                s.joinQueue(player);
                Bukkit.getScheduler().runTaskLater(RideManager.getInstance(), s::start, 20L);
            } else if (ride instanceof FileRide) {
                FileRide r = (FileRide) ride;
                r.joinQueue(player);
                Bukkit.getScheduler().runTaskLater(RideManager.getInstance(), r::start, 20L);
            } else if (ride instanceof ArmorStandRide) {
                ArmorStandRide r = (ArmorStandRide) ride;
                r.joinQueue(player);
                Bukkit.getScheduler().runTaskLater(RideManager.getInstance(), r::start, 20L);
            }
        }*/
        if (args.length == 2) {
            Ride ride = RideManager.getMovementUtil().getRide(args[0]);
            if (ride instanceof TeacupsRide) {
                TeacupsRide tea = (TeacupsRide) ride;
                tea.setSpeed(Double.parseDouble(args[1]));
            }
        }
//        if (args.length == 1) {
//            TeacupsRide ride = new TeacupsRide("test", "Test", 1, 1, ((Player) sender).getLocation());
//            ride.spawn();
//            return;
//        }
//        if (args.length != 3) {
//            sender.sendMessage(ChatColor.RED + "/test x y z");
//            return;
//        }
//        try {
//            World w = Bukkit.getWorlds().get(0);
//            Sign s = (Sign) w.getBlockAt((int) Double.parseDouble(args[0]), (int) Double.parseDouble(args[1]),
//                    (int) Double.parseDouble(args[2])).getState();
//            Location sloc = s.getLocation();
//            BlockFace direction = RideManager.getBlockFace(s.getLine(3));
//            float yaw = RideManager.getYaw(direction);
//            Location loc = new Location(w, sloc.getBlockX() + 0.5, Double.parseDouble(s.getLine(2)),
//                    sloc.getBlockZ() + 0.5, yaw, 0);
//            ItemStack item = new ItemStack(Material.DIAMOND_SWORD, 1, (byte) 5);
//            Cart c = new Cart(loc, item, 0.1, direction);
//            c.addPassenger(Core.getPlayerManager().getPlayer((Player) sender));
//        } catch (Exception e) {
//            e.printStackTrace();
//            sender.sendMessage(ChatColor.RED + "There was an error spawning a cart!");
//        }
    }
}
