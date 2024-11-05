package net.makozort.makostpa.commands;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.makozort.makostpa.handlers.TPAHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TPACommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("tpa")
                        .requires(CommandSourceStack::isPlayer)
                        .then(
                                Commands.argument("player", EntityArgument.players()).executes(context -> {
                                    Player player = EntityArgument.getPlayer(context, "player");
                                    if (player != context.getSource().getPlayer()){
                                        return teleportToPlayer((context.getSource().getPlayer()), player);
                                    } else {
                                        context.getSource().getPlayer().sendSystemMessage(Component.literal("INVALID TARGET").withColor(0xFF0000));;
                                    }
                                    return 1;
                                }))
        );
    }

    private static boolean consumeEnderPearl(Player player) {
        // Iterate through the player's inventory
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);

            // Check if the current stack is an ender pearl
            if (stack.getItem() == Items.ENDER_PEARL && stack.getCount() > 0) {
                // Remove one ender pearl from the stack
                stack.shrink(1);
                // Optionally, send a message to the player
                player.sendSystemMessage(Component.literal("You have used an ender pearl.").withColor(0x00FF00)); // Green message
                return true; // Return true indicating that an ender pearl was consumed
            }
        }
        return false; // Return false if no ender pearl was found
    }

    private static int teleportToPlayer(Player from, Player to) throws CommandSyntaxException {
        if (!from.level().isClientSide) {
            TPAHandler.addRequest(from,to);

            //if (consumeEnderPearl(from)) {
            //    TPAHandler.addRequest(from,to);
            //} else {
            //    from.sendSystemMessage(Component.literal("this requires an enderpeal").withColor(0xFF0000));
            //    from.playSound(SoundEvents.NOTE_BLOCK_SNARE.value());
            //}

        }
        return 1;
    }
}
