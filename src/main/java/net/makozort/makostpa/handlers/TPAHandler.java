package net.makozort.makostpa.handlers;


import net.makozort.makostpa.MakosTpa;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TPAHandler {


    private static Map<Player, TPARequest> requests = new HashMap<>();


    public static void addRequest(Player from, Player to) {
        Player[] id = new Player[]{from, to};
        if (!requests.keySet().contains(from) || requests == null) {
            requests.put(from, new TPARequest(from, to, id));
            from.sendSystemMessage(Component.literal("teleport request sent").withColor(0x00FF00)); // 0x00FF00 for green
            to.sendSystemMessage(Component.literal(String.format("you have received a teleport a request from %s,type /tpaccept %s to accept", from.getName().getString(),from.getName().getString())).withColor(0xFFFF00)); // yellow
            to.playNotifySound(SoundEvents.NOTE_BLOCK_PLING.value(), SoundSource.MASTER, .2f, 1);
        } else {
            from.sendSystemMessage(Component.literal("teleport request already pending").withColor(0xFF0000)); // red
        }
    }

    public static void tick(Player player) {
        for (Map.Entry<Player, TPARequest> entry : requests.entrySet()) {
            TPARequest r = entry.getValue();
            if (r.getFrom() == player) {
                if (!r.tick()) {
                    killRequest(player);
                }
            }
        }

    }

    public static void handleAccept(Player from, Player to) {
        Player[] id = new Player[]{from, to};
        for (Map.Entry<Player, TPARequest> entry : requests.entrySet()) {
            TPARequest tpaRequest = entry.getValue();

            // Check if the request ID matches the current entry's ID
            if (Arrays.equals(tpaRequest.getId(), id)) {
                // Execute the TPA request (e.g., teleport the player)
                tpaRequest.execute();
                // Remove the request from the map
                requests.remove(entry.getKey());
                return;
            }
        }
        // If we exit the loop without finding a matching request
        to.sendSystemMessage(Component.literal("Something went wrong with this request").withColor(0xFFFF00)); // yellow
    }


    public static Map<Player, TPARequest> requestMap() {
        return requests;
    }

    public static void killRequest(Player player) {
        if (requests.containsKey(player)) {
            TPARequest r = requests.get(player);

            try {
                r.getFrom().sendSystemMessage(Component.literal("a teleport request you sent timed out").withColor(0xFF0000));
                r.getTo().sendSystemMessage(Component.literal("a teleport request timed out").withColor(0xFF0000));
            } catch (Exception e) {
                MakosTpa.LOGGER.error(e.toString());
            }
            requests.remove(player);
        }
    }
}
