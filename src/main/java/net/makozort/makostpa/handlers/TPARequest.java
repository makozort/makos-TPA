package net.makozort.makostpa.handlers;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;
import java.util.Set;

public class TPARequest {

    private final Player FROM;
    private final Player TO;
    private final Player[] ID;
    private int timer = 600;

    public TPARequest(Player from, Player to, Player[] id) {
        this.FROM = from;
        this.TO = to;
        this.ID = id;  // Initialize ID with from and to
    }

    public boolean tick() {
        this.timer--;
        return (this.timer > 0);
    }

    public Player[] getId() {  // Change method to return UUID array
        return this.ID;
    }

    // Optionally, add methods to access FROM and TO if needed
    public Player getFrom() {
        return FROM;
    }

    public Player getTo() {
        return TO;
    }


    public void execute() {
        Set<RelativeMovement> set = EnumSet.noneOf(RelativeMovement.class); //idek what the fuck this is
        this.FROM.teleportTo((ServerLevel) this.TO.level(), this.TO.getX(), this.TO.getY(), this.TO.getZ(), set, this.TO.getYRot(), this.TO.getXRot());
        this.FROM.playSound(SoundEvents.ENDERMAN_TELEPORT);
        MobEffectInstance blindnessEffect = new MobEffectInstance(MobEffects.DARKNESS, 200, 0, false, false);
        MobEffectInstance moveEffect = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 0, false, false);
        MobEffectInstance weakEffect = new MobEffectInstance(MobEffects.WEAKNESS, 200, 0, false, false);
        MobEffectInstance hungEffect = new MobEffectInstance(MobEffects.HUNGER, 200, 0, false, false);
        MobEffectInstance nauEffect = new MobEffectInstance(MobEffects.CONFUSION, 200, 0, false, false);
        this.FROM.addEffect(blindnessEffect);
        this.FROM.addEffect(moveEffect);
        this.FROM.addEffect(weakEffect);
        this.FROM.addEffect(hungEffect);
        this.FROM.addEffect(nauEffect);
    }

    public String toSting() {
        return (this.FROM.getScoreboardName() + " " + this.TO.getScoreboardName());
    }
}

