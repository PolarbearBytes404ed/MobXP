package games.polarbearbytes.mobxp.rules;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * XPRules Registry
 */
public final class XPRules {
    private static final XPRule DEFAULT = new GeneralXPRule();

    public static XPRule forEntity(String id) {
        return switch(id){
            case "minecraft:ender_dragon" -> new EnderDragonXPRule();
            case "minecraft:chicken" -> new ChickenXPRule();
            default -> {
                Level level = Minecraft.getInstance().level;
                if(level == null) yield DEFAULT;

                EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.parse(id));
                Entity entity = entityType.create(level, EntitySpawnReason.EVENT);

                if(entity instanceof AgeableMob){
                    yield new BabyVariantXPRule();
                } else {
                    yield DEFAULT;
                }
            }
        };
    }
}