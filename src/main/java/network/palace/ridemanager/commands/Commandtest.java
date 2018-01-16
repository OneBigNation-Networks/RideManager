package network.palace.ridemanager.commands;

import network.palace.core.command.CommandException;
import network.palace.core.command.CommandMeta;
import network.palace.core.command.CommandPermission;
import network.palace.core.command.CoreCommand;
import network.palace.core.player.CPlayer;
import network.palace.core.player.Rank;
import network.palace.ridemanager.RideManager;
import network.palace.ridemanager.handlers.ride.Ride;
import network.palace.ridemanager.handlers.ride.file.FileRide;

/**
 * Created by Marc on 1/15/17.
 */
@CommandMeta(description = "Test ride system")
@CommandPermission(rank = Rank.DEVELOPER)
public class Commandtest extends CoreCommand {

    public Commandtest() {
        super("test");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        RideManager.getMappingUtil().reset();
        for (Ride ride : RideManager.getMovementUtil().getRides()) {
            if (!(ride instanceof FileRide)) continue;
            FileRide r = (FileRide) ride;
            r.loadFile(-1);
        }
//        Ride ride = RideManager.getMovementUtil().getRide("test");
//        if (ride == null) {
//            ride = new FileRide("test", "Testing", 1, 20, new Location(Bukkit.getWorlds().get(0), -219, 64, -190), "tot");
//            RideManager.getMovementUtil().addRide(ride);
//            player.sendMessage("Created!");
//            return;
//        }
//        FileRide file = (FileRide) ride;
//        if (file == null) {
//            player.sendMessage("Le problem");
//            return;
//        }
//        file.spawn(7000);
    }
}
