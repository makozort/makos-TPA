package net.makozort.makostpa.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.makozort.makostpa.handlers.TPAHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class TPAccpetCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("tpaccept")
                        .requires(CommandSourceStack::isPlayer)
                        .then(
                                Commands.argument("player", EntityArgument.players()).executes(context -> {
                                    Player player = EntityArgument.getPlayer(context, "player");
                                    if (player != context.getSource().getPlayer()) {
                                        return processTeleport(player, context.getSource().getPlayer());
                                    } else {
                                        context.getSource().getPlayer().sendSystemMessage(Component.literal("INVALID TARGET").withColor(0xFF0000));
                                    }
                                    return 1;
                                }))
        );
    }

    private static int processTeleport(Player from, Player to) throws CommandSyntaxException {
        if (!from.level().isClientSide) {
            TPAHandler.handleAccept(from, to);
        }
        return 1;
    }
}
