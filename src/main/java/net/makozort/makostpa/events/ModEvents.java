package net.makozort.makostpa.events;


import net.makozort.makostpa.commands.TPACommand;
import net.makozort.makostpa.commands.TPAccpetCommand;
import net.makozort.makostpa.handlers.TPAHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;


public class ModEvents {

    @SubscribeEvent
    private void onCommandRegister(RegisterCommandsEvent event) {
        TPACommand.register(event.getDispatcher());
        TPAccpetCommand.register(event.getDispatcher());
    }


    @SubscribeEvent
    private void playerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        TPAHandler.killRequest(player);
    }

    @SubscribeEvent
    private void playerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            TPAHandler.tick(player);
        }
    }
}
