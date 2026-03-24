package games.polarbearbytes.mobxp.mixin;

import games.polarbearbytes.mobxp.MobXP;
import games.polarbearbytes.mobxp.config.MobXPStateManager;
import games.polarbearbytes.mobxp.data.MobXPData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to make it so shouldDropExperience() return true if it did normally or if the custom xp was enabled
 */
@Mixin({LivingEntity.class, Tadpole.class})
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(
            method = "shouldDropExperience()Z",
            at = @At("TAIL"),
            cancellable = true
    )
    public void enableXPDropForBaby(CallbackInfoReturnable<Boolean> cir) {
        Level entityWorld = this.level();
        if(entityWorld.isClientSide() || entityWorld.getServer() == null) return;
        MobXPStateManager state = MobXPStateManager.get(entityWorld.getServer());
        MobXPData data = state.getMobData(getEncodeId());
        if(data == null){
            MobXP.LOGGER.error("{} not found in MobXPData list", getEncodeId());
            return;
        }

        cir.setReturnValue( cir.getReturnValue() || ( data.enabled() && (data.babyXP() > -1 || data.usePrimaryXPForBaby()) ) );
    }
}
