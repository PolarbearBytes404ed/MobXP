package games.polarbearbytes.mobxp.mixin;

import games.polarbearbytes.mobxp.config.MobXPStateManager;
import games.polarbearbytes.mobxp.data.MobXPData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.end.EnderDragonFight;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/**
 * Mixin for {@link EnderDragon} class to modify it to allow for custom experience points
 */
@Mixin(EnderDragon.class)
public class EnderDragonMixin extends Mob {
	@Shadow
	private EnderDragonFight dragonFight;
	@Shadow
	public int dragonDeathTime;
	@Final @Shadow
	private EnderDragonPart[] subEntities;

	protected EnderDragonMixin(EntityType<? extends Mob> entityType, Level world) {
		super(entityType, world);
    }

	@Inject(at = @At("HEAD"), method = "tickDeath()V", cancellable = true)
	private void tickDeathMixin(CallbackInfo ci) {
		/* Start Custom Experience Point Code */
		Level entityWorld = this.level();
		if(entityWorld.isClientSide() || entityWorld.getServer() == null) return;
		MobXPStateManager state = MobXPStateManager.get(entityWorld.getServer());
		MobXPData dragonData = state.getMobData(getEncodeId());
		int firstXP = dragonData.primaryXP();
		int secondaryXP = dragonData.secondaryXP();

		if(!dragonData.enabled() || (firstXP <= -1 && secondaryXP <= -1)) return;
		/* End Custom Experience Point Code */

		if (this.dragonFight != null) {
			this.dragonFight.updateDragon((EnderDragon) (Object) this);
		}

		++this.dragonDeathTime;
		if (this.dragonDeathTime >= 180 && this.dragonDeathTime <= 200) {
			float f = (this.random.nextFloat() - 0.5F) * 8.0F;
			float g = (this.random.nextFloat() - 0.5F) * 4.0F;
			float h = (this.random.nextFloat() - 0.5F) * 8.0F;
			this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX() + (double)f, this.getY() + (double)2.0F + (double)g, this.getZ() + h, 0.0F, 0.0F, 0.0F);
		}

		/* Start Custom Experience Point Code */
		int xp = secondaryXP <= -1 ? 500 : secondaryXP;
		if (this.dragonFight != null && !this.dragonFight.hasPreviouslyKilledDragon()) {
			xp = firstXP <= -1 ? 12000 : firstXP;
		}
		xp = dragonData.random() ? this.random.nextInt( xp ) : xp;
		/* End Custom Experience Point Code */

		Level level = this.level();
		if (level instanceof ServerLevel serverLevel) {
			if (this.dragonDeathTime > 150 && this.dragonDeathTime % 5 == 0 && serverLevel.getGameRules().get(GameRules.MOB_DROPS)) {
				ExperienceOrb.award(serverLevel, this.position(), Mth.floor((float)xp * 0.08F));
			}

			if (this.dragonDeathTime == 1 && !this.isSilent()) {
				serverLevel.globalLevelEvent(1028, this.blockPosition(), 0);
			}
		}

		Vec3 deathMove = new Vec3(0.0, 0.1F, 0.0);
		this.move(MoverType.SELF, deathMove);

		for(EnderDragonPart dragonPart : this.subEntities) {
			dragonPart.setOldPosAndRot();
			dragonPart.setPos(dragonPart.position().add(deathMove));
		}

		if (this.dragonDeathTime >= 200 && this.level() instanceof ServerLevel level2) {
			if (level2.getGameRules().get(GameRules.MOB_DROPS)) {
				ExperienceOrb.award(level2, this.position(), Mth.floor(xp * 0.2F));
			}

			if (this.dragonFight != null) {
				this.dragonFight.setDragonKilled((EnderDragon) (Object)this);
			}

			this.remove(Entity.RemovalReason.KILLED);
			this.gameEvent(GameEvent.ENTITY_DIE);
		}

		ci.cancel();
	}
}