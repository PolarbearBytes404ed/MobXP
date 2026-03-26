package games.polarbearbytes.mobxp.mixin;

import games.polarbearbytes.mobxp.MobXP;
import games.polarbearbytes.mobxp.config.MobXPStateManager;
import games.polarbearbytes.mobxp.data.MobXPData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Objects;

/**
 * General mixin to modify mob classes to allow for custom xp, called if these classes overrode the getExperienceToDrop() method
 * The other mobs will be caught by the MobEntityMixin
 */
@Mixin({Animal.class, WaterAnimal.class, Piglin.class, Hoglin.class, Chicken.class, Zombie.class})
public abstract class EntityXPMixin extends Entity {

    protected EntityXPMixin(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(
            method = "getBaseExperienceReward(Lnet/minecraft/server/level/ServerLevel;)I",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getExperienceToDropMixin(ServerLevel world, CallbackInfoReturnable<Integer> cir) {
        String entityId = this.getEncodeId();
        if(entityId == null) return;

        MobXPStateManager state = MobXPStateManager.get(Objects.requireNonNull(world.getServer()));
        MobXPData data = state.getMobData(entityId);
        if(data == null){
            MobXP.LOGGER.error("{} not found in MobXPData list", entityId);
            return;
        }

        //Custom xp not enabled so no need to continue
        if(!data.enabled()) return;

        int xp = 0;
        EntityType<?> type = this.getType();

        if(type == EntityType.CHICKEN){
            Chicken chicken = (Chicken) (Object) this;

            if(data.primaryXP() <= -1 && data.secondaryXP() <= -1 && data.babyXP() <= -1) {
                //Enabled but all the xp fields are set to default
                return;
            } else if( chicken.isChickenJockey() ) {
                //Chicken has a rider
                xp = data.secondaryXP();
            } else if( chicken.isBaby() ){
                //Chicken is a baby
                xp = data.usePrimaryXPForBaby() ? data.primaryXP() : data.babyXP();
            } else {
                //Regular adult chicken
                xp = data.primaryXP();
            }
        } else if(type == EntityType.ZOMBIE){
            Zombie zombie = (Zombie) (Object) this;
            //If it is a zombie but not a baby we let it drop through to the MobEntityMixin
            if(!zombie.isBaby()){
                return;
            }
        } else {
            //All other mobs
            LivingEntity entity = (LivingEntity) (Object) this;
            int babyXP = data.babyXP();
            int adultXP = data.primaryXP();

            xp = entity.isBaby() ? (data.usePrimaryXPForBaby() ? adultXP : babyXP) : adultXP;
        }
        //Custom xp was enabled but left as default
        if(xp == -1) return;

        if(data.random()){
            xp = data.random() ? this.random.nextInt(xp) : xp;
        }
        cir.setReturnValue(xp);
    }
}
