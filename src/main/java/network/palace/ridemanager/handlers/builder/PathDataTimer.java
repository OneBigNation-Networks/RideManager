package network.palace.ridemanager.handlers.builder;

import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.ridemanager.RideManager;
import network.palace.ridemanager.handlers.BuildSession;
import network.palace.ridemanager.handlers.actions.RideAction;
import network.palace.ridemanager.handlers.builder.actions.FakeExitAction;
import network.palace.ridemanager.handlers.builder.actions.FakeSpawnAction;
import network.palace.ridemanager.handlers.builder.actions.FakeStraightAction;
import network.palace.ridemanager.handlers.builder.actions.FakeTurnAction;
import network.palace.ridemanager.utils.MovementUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class PathDataTimer implements Runnable {
    @Override
    public void run() {
        for (BuildSession session : new ArrayList<>(RideManager.getRideBuilderUtil().getSessions())) {
            CPlayer player = Core.getPlayerManager().getPlayer(session.getUuid());
            if (!session.isPath() || player == null) continue;
            Location start = session.getSpawn();
            double speed = session.getSpeed();
            List<RideAction> actions = session.getActions();
            RideAction editAction = session.getEditAction();
            if (editAction != null) {
                actions.add(editAction);
            }
            for (RideAction action : actions) {
                boolean finished = false;
                switch (action.getActionType()) {
                    case WAIT: {
                        pathParticle(player, ((FakeSpawnAction) action).getLoc(), 0, 0, 1);
                        break;
                    }
                    case SPAWN: {
                        pathParticle(player, ((FakeSpawnAction) action).getLoc(), 1, 1, 0);
                        break;
                    }
                    case STRAIGHT:
                    case EXIT: {
                        Location original = start.clone();
                        Location to = action.getActionType().equals(ActionType.STRAIGHT) ? ((FakeStraightAction) action).getTo() : ((FakeExitAction) action).getTo();

                        if (to.equals(original)) {
                            finished = true;
                        }

                        pathParticle(player, start, true);

                        while (!finished) {
                            float yaw = (float) Math.toDegrees(Math.atan2(original.getZ() - to.getZ(), original.getX() - to.getX())) + 90;
                            double distance = original.distance(to);
                            Vector resultant = to.clone().subtract(start).toVector().normalize();
                            Vector change = resultant.multiply(new Vector(speed, speed, speed));
                            Location next = start.clone().add(change);
                            if (next.distance(original) >= distance) {
                                Vector v = to.toVector().subtract(start.toVector());
                                if (v.getY() == 0) {
                                    v.setY(MovementUtil.getYMin());
                                }
                                start = to;
                                start.setYaw(yaw);
                                finished = true;
                            } else {
                                Vector v = next.toVector().subtract(start.toVector());
                                if (v.getY() == 0) {
                                    v.setY(MovementUtil.getYMin());
                                }
                                start = next;
                                start.setYaw(yaw);
                            }
                            pathParticle(player, start, false);
                        }
                        break;
                    }
                    case TURN: {
                        Location original = start.clone();
                        FakeTurnAction ac = (FakeTurnAction) action;

                        Location to = ac.getTo();

                        Location p0 = ac.getP0();
                        Location p1 = null;
                        Location p2 = null;

                        float angle = 0;
                        double yDifference = 0;
                        double t = 0;

                        pathParticle(player, start, true);

                        pathParticle(player, p0, true);

                        while (!finished) {
                            if (p1 == null) {
                                double p1_x = original.getX();
                                double p1_z = original.getZ();

                                double p2_x = to.getX();
                                double p2_z = to.getZ();

                                p1 = new Location(original.getWorld(), p1_x, original.getY(), p1_z);
                                p2 = new Location(original.getWorld(), p2_x, original.getY(), p2_z);

                                yDifference = to.getY() - original.getY();

                                double x = 2 * (p2.getX() - p0.getX());
                                double z = 2 * (p2.getZ() - p0.getZ());

                                angle = (float) Math.toDegrees(((float) Math.atan2(z, x)));
                            }

                            if (t >= 1) {
                                Vector v = to.toVector().subtract(start.clone().toVector());
                                if (v.getY() == 0) {
                                    v.setY(MovementUtil.getYMin());
                                }
                                to.setYaw(original.getYaw() + angle);
                                start = to;
                                finished = true;
                                pathParticle(player, start, false);
                                break;
                            }

                            double x = (Math.pow((1 - t), 2) * p1.getX())
                                    + (2 * t * (1 - t) * p0.getX())
                                    + Math.pow(t, 2) * p2.getX();

                            double y;
                            if (yDifference != 0) {
                                y = original.getY() + (yDifference * t);
                            } else {
                                y = original.getY();
                            }

                            double z = (Math.pow((1 - t), 2) * p1.getZ())
                                    + (2 * t * (1 - t) * p0.getZ())
                                    + Math.pow(t, 2) * p2.getZ();

                            Location next = new Location(original.getWorld(), x, y, z, original.getYaw(), 0);

                            Vector v = next.toVector().subtract(start.clone().toVector());
                            if (v.getY() == 0) {
                                v.setY(MovementUtil.getYMin());
                            }
                            next.setYaw(original.getYaw() + (float) (angle * t));
                            start = next;
                            pathParticle(player, start, false);

                            t += speed * 0.1;
                        }

                        /*boolean clockwise = angle > 0;
                        double radius = 0;
                        float originAngle = 0;
                        float targetAngle = 0;
//                        float yawDifference;
                        double originalY = 0;
                        double yDifference = 0;
                        double yChange = 0;

                        pathParticle(player, start, true);

                        while (!finished) {
                            if (radius == 0) {
                                if (angle > 180 || angle == 0) {
                                    finished = true;
                                    Bukkit.getLogger().severe("Cannot have a turn travel more than 180 degrees or equal 0!");
                                    return;
                                }
                                radius = original.distance(origin);
                                originAngle = (float) Math.toDegrees(Math.atan2(origin.getX() - original.getX(), original.getZ() - origin.getZ()));
//                                yawDifference = cart.getYaw() - originAngle;
                                originalY = original.getY();
                                yDifference = 2 * (origin.getY() - original.getY());
                                yChange = MovementUtil.pythag((Math.abs(angle) * radius * Math.PI) / 180, yDifference);
                                targetAngle = originAngle + angle;
                            }
                            double angleChange = angle / ((Math.abs((2 * Math.PI * radius) / (360.0 / angle))) / (speed * 1.66));
                            float dynamicAngle;
                            if ((clockwise && originAngle + angleChange > targetAngle) || (!clockwise && originAngle + angleChange < targetAngle)) {
                                dynamicAngle = targetAngle;
                                finished = true;
                            } else {
                                dynamicAngle = originAngle += angleChange;
                            }
                            Location rel = origin.clone();
                            Location current = start.clone();
                            rel.setY(current.getY());
                            Location target = Ride.getRelativeLocation(-dynamicAngle, radius, rel);
//                            target.setYaw(dynamicAngle + yawDifference);
//                            Bukkit.broadcastMessage(yDifference + " " + yChange + " " + cart.getSpeed() + " " + target.getRelativeY());
                            if (yDifference != 0) {
                                target.setY(target.getY() + (yChange / (20 / speed)));
                            }
//                            cart.setYaw(target.getYaw());
                            Vector v = target.toVector().subtract(start.clone().toVector());
                            if (v.getY() == 0) {
                                v.setY(MovementUtil.getYMin());
                            }
//                            cart.setVelocity(v);
//                            cart.teleport(target);
                            start = target;
                            pathParticle(player, start, false);
                        }*/
                        break;
                    }
                }
            }
        }
    }

    private void pathParticle(CPlayer player, Location loc, boolean action) {
        float r = 0, g = 0, b = 0;
        if (action) {
            g = 0.8f;
        } else {
            r = 1;
        }
        pathParticle(player, loc, r, g, b);
    }

    private void pathParticle(CPlayer player, Location loc, float r, float g, float b) {
        if (player.getLocation().distance(loc) > 15) return;
        player.getParticles().send(loc, Particle.REDSTONE, 0, r, g, b, 1);
    }
}
