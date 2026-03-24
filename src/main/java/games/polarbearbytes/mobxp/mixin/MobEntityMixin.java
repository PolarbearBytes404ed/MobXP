package games.polarbearbytes.mobxp.mixin;

import games.polarbearbytes.mobxp.config.MobXPStateManager;
import games.polarbearbytes.mobxp.data.MobXPData;
import net.minecraft.world.entity.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Objects;

/**
 * General mob mixin to modify MobEntity class to allow for custom xp, covers the class EntityXPMixin doesn't modify
 */
@Mixin(Mob.class)
public abstract class MobEntityMixin extends LivingEntity {
    @Shadow
    protected int xpReward;
    @Shadow
    private DropChances dropChances;

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(
            method = "getBaseExperienceReward(Lnet/minecraft/server/level/ServerLevel;)I",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getExperienceToDropMixin(ServerLevel world, CallbackInfoReturnable<Integer> cir) {
        MobXPStateManager state = MobXPStateManager.get(Objects.requireNonNull(world.getServer()));
        MobXPData data = state.getMobData(getEncodeId());

        int xp = this.xpReward;

        /*
         EnderDragonEntity extends MobEntity so this method gets called on its death, but its XP is dropped from the
         updatePostDeath() method, so we need to check if entity is not dragon so we do not accidentally drop its xp here
         */
        if(data!=null && data.primaryXP() > -1 && data.enabled() && this.getType() != EntityType.ENDER_DRAGON){
            xp = data.random() ? this.random.nextInt( data.primaryXP() ) : data.primaryXP();
        }

        if (xp > 0) {
            int i = xp;
            for(EquipmentSlot equipmentSlot : EquipmentSlot.VALUES) {
                if (equipmentSlot.canIncreaseExperience()) {
                    ItemStack itemStack = this.getItemBySlot(equipmentSlot);
                    if (!itemStack.isEmpty() && this.dropChances.byEquipment(equipmentSlot) <= 1.0F) {
                        i += 1 + this.random.nextInt(3);
                    }
                }
            }
            cir.setReturnValue(i);
        } else {
            cir.setReturnValue(xp);
        }
    }
}
