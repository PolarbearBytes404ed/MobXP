package games.polarbearbytes.mobxp.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import java.util.Map;

/**
 * Widget for displaying a mob in teh same fashion the player is displayed in the inventory screen, e.g. follows mouse etc.
 * Uses InventoryScreen#drawEntity() to do the actual displaying
 */
public class MobWidget implements Renderable {
    protected LivingEntity entity;

    protected int x;
    protected int y;

    protected int width;
    protected int height;

    public MobWidget(EntityType<?> entityType, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        entity = (LivingEntity) entityType.create(Minecraft.getInstance().level, EntitySpawnReason.EVENT);
        assert entity != null;

        entity.snapTo(0.0, 0.0, 0.0, 0.0f, 0.0f);
        entity.yBodyRot = 0.0f;
        entity.yHeadRot = 0.0f;
        entity.yBodyRotO = 0.0f;
        entity.yHeadRotO = 0.0f;
        entity.setXRot(0.0f);
        entity.tick();
    }

    /**
     * Renders the mob entity by calling InventoryScreen.drawEntity(), uses passed scale and offset to size and position mob in region
     * @param scale Used to scale the entity model
     * @param offset Used to position the entity model vertically
     * @param context drawing context
     * @param mouseX x position of mouse
     * @param mouseY y position of mouse
     */
    public void render(double scale, double offset, GuiGraphicsExtractor context, int mouseX, int mouseY) {
        int shift = (int) (offset * height);
        int size = (int)(height * 0.45f * scale);

        //InventoryScreen.drawEntity() is the actual work horse for displaying the entity model, let it do the work for us
        InventoryScreen.extractEntityInInventoryFollowsMouse(context,x, y-shift, x+width, y+height, size, 1F, mouseX, mouseY, entity );
    }

    /**
     * Overloaded to redirect to the {@link #render(double, double, GuiGraphicsExtractor, int, int)} method that can take scale and offset arguments, default use 1 nad 1 respectively (no scaling no offsetting)
     * @param context drawing context
     * @param mouseX x position of mouse
     * @param mouseY y position of mouse
     * @param deltaTicks ticks since last render
     */
    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks) {
        double offset = MODEL_OFFSET_FIX.getOrDefault(entity.getType(), 1F);
        double scale = MODEL_SCALE_FIX.getOrDefault(entity.getType(), 1F);
        render(scale, offset, context, mouseX, mouseY);
    }

    /**
     * Map of scales for specific mobs to use to make sure entity scales to fit into the region specified
     */
    private static final Map<EntityType<?>, Float> MODEL_SCALE_FIX = Map.<EntityType<?>, Float>ofEntries(
            Map.entry(EntityType.GIANT, 0.16666F),
            Map.entry(EntityType.GLOW_SQUID, 0.9F),
            Map.entry(EntityType.CAMEL_HUSK, 0.7F),
            Map.entry(EntityType.TRADER_LLAMA, 0.9F),
            Map.entry(EntityType.WARDEN, 0.6F),
            Map.entry(EntityType.HORSE, 0.9F),
            Map.entry(EntityType.WITCH, 0.9F),
            Map.entry(EntityType.LLAMA, 0.9F),
            Map.entry(EntityType.PIG, 1.5F),
            Map.entry(EntityType.SHEEP, 1.2F),
            Map.entry(EntityType.ENDER_DRAGON, 0.3F),
            Map.entry(EntityType.CAVE_SPIDER, 2F),
            Map.entry(EntityType.TADPOLE, 3F),
            Map.entry(EntityType.ENDERMAN, 0.8F),
            Map.entry(EntityType.GOAT, 1.1F),
            Map.entry(EntityType.DOLPHIN, 1.3F),
            Map.entry(EntityType.VEX, 1.5F),
            Map.entry(EntityType.COD, 2.5F),
            Map.entry(EntityType.DONKEY, 1.2F),
            Map.entry(EntityType.RAVAGER, 0.7F),
            Map.entry(EntityType.GHAST, 0.25F),
            Map.entry(EntityType.SKELETON_HORSE, 0.9F),
            Map.entry(EntityType.COW, 1.3F),
            Map.entry(EntityType.CREAKING, 0.8F),
            Map.entry(EntityType.NAUTILUS, 1.2F),
            Map.entry(EntityType.CREEPER, 1.3F),
            Map.entry(EntityType.BEE, 2F),
            Map.entry(EntityType.SALMON, 2F),
            Map.entry(EntityType.SHULKER, 1.2F),
            Map.entry(EntityType.MOOSHROOM, 0.9F),
            Map.entry(EntityType.RABBIT, 2.5F),
            Map.entry(EntityType.AXOLOTL, 2F),
            Map.entry(EntityType.HAPPY_GHAST, 0.35F),
            Map.entry(EntityType.GUARDIAN, 1.2F),
            Map.entry(EntityType.PARROT, 2F),
            Map.entry(EntityType.COPPER_GOLEM, 1.4F),
            Map.entry(EntityType.WITHER, 0.7F),
            Map.entry(EntityType.ZOMBIE_NAUTILUS, 1.2F),
            Map.entry(EntityType.BAT, 2F),
            Map.entry(EntityType.WITHER_SKELETON, 0.85F),
            Map.entry(EntityType.FROG, 2F),
            Map.entry(EntityType.IRON_GOLEM, 0.8F),
            Map.entry(EntityType.BLAZE, 1.2F),
            Map.entry(EntityType.ELDER_GUARDIAN, 0.6F),
            Map.entry(EntityType.SNIFFER, 0.65F),
            Map.entry(EntityType.CAMEL, 0.7F),
            Map.entry(EntityType.MAGMA_CUBE, 2.5F),
            Map.entry(EntityType.MANNEQUIN, 1.05F),
            Map.entry(EntityType.SLIME, 2.3F),
            Map.entry(EntityType.CAT, 2.5F),
            Map.entry(EntityType.PUFFERFISH, 3F),
            Map.entry(EntityType.FOX, 1.8F),
            Map.entry(EntityType.ALLAY, 2.7F),
            Map.entry(EntityType.TROPICAL_FISH, 3F),
            Map.entry(EntityType.OCELOT, 2F),
            Map.entry(EntityType.SILVERFISH, 2F),
            Map.entry(EntityType.ENDERMITE, 2.5F),
            Map.entry(EntityType.ARMADILLO, 2.2F),
            Map.entry(EntityType.CHICKEN, 2F),
            Map.entry(EntityType.WOLF, 1.7F)
    );

    /**
     * Map of Y offsets for specific mobs to make sure the mob is centered in the specified region vertically
     */
    private static final Map<EntityType<?>, Float> MODEL_OFFSET_FIX = Map.<EntityType<?>, Float>ofEntries(
            Map.entry(EntityType.GIANT, 0.1F),
            Map.entry(EntityType.GLOW_SQUID, 1.25F),
            Map.entry(EntityType.HOGLIN, 0.65F),
            Map.entry(EntityType.CAMEL_HUSK, 0.3F),
            Map.entry(EntityType.TRADER_LLAMA, 0.6F),
            Map.entry(EntityType.BREEZE, 0.85F),
            Map.entry(EntityType.WARDEN, 0.4F),
            Map.entry(EntityType.HORSE, 0.5F),
            Map.entry(EntityType.POLAR_BEAR, 0.8F),
            Map.entry(EntityType.PHANTOM, 0.8F),
            Map.entry(EntityType.ZOMBIFIED_PIGLIN, 0.8F),
            Map.entry(EntityType.WITCH, 0.6F),
            Map.entry(EntityType.LLAMA, 0.65F),
            Map.entry(EntityType.PIG, 1.2F),
            Map.entry(EntityType.HUSK, 0.7F),
            Map.entry(EntityType.TURTLE, 0.8F),
            Map.entry(EntityType.SHEEP, 0.9F),
            Map.entry(EntityType.ENDER_DRAGON, 0.6F),
            Map.entry(EntityType.CAVE_SPIDER, 1.6F),
            Map.entry(EntityType.TADPOLE, 2.7F),
            Map.entry(EntityType.ENDERMAN, 0.6F),
            Map.entry(EntityType.GOAT, 0.7F),
            Map.entry(EntityType.DOLPHIN, 1.1F),
            Map.entry(EntityType.VEX, 1.3F),
            Map.entry(EntityType.COD, 2.3F),
            Map.entry(EntityType.SKELETON, 0.8F),
            Map.entry(EntityType.PARCHED, 0.8F),
            Map.entry(EntityType.DONKEY, 0.9F),
            Map.entry(EntityType.RAVAGER, 0.5F),
            Map.entry(EntityType.SQUID, 1.4F),
            Map.entry(EntityType.GHAST, 0.5F),
            Map.entry(EntityType.SKELETON_HORSE, 0.6F),
            Map.entry(EntityType.CREAKING, 0.65F),
            Map.entry(EntityType.NAUTILUS, 0.9F),
            Map.entry(EntityType.PIGLIN_BRUTE, 0.8F),
            Map.entry(EntityType.CREEPER, 1.1F),
            Map.entry(EntityType.BEE, 1.7F),
            Map.entry(EntityType.ILLUSIONER, 0.8F),
            Map.entry(EntityType.SALMON, 1.8F),
            Map.entry(EntityType.SNOW_GOLEM, 0.8F),
            Map.entry(EntityType.MOOSHROOM, 0.55F),
            Map.entry(EntityType.VILLAGER, 0.8F),
            Map.entry(EntityType.STRAY, 0.8F),
            Map.entry(EntityType.DROWNED, 0.75F),
            Map.entry(EntityType.RABBIT, 2.1F),
            Map.entry(EntityType.AXOLOTL, 1.7F),
            Map.entry(EntityType.PILLAGER, 0.8F),
            Map.entry(EntityType.ZOGLIN, 0.8F),
            Map.entry(EntityType.STRIDER, 0.75F),
            Map.entry(EntityType.HAPPY_GHAST, 0.5F),
            Map.entry(EntityType.GUARDIAN, 0.9F),
            Map.entry(EntityType.PARROT, 1.85F),
            Map.entry(EntityType.COPPER_GOLEM, 0.85F),
            Map.entry(EntityType.WITHER, 0.2F),
            Map.entry(EntityType.ZOMBIE_NAUTILUS, 0.9F),
            Map.entry(EntityType.BAT, 1.7F),
            Map.entry(EntityType.WANDERING_TRADER, 0.8F),
            Map.entry(EntityType.WITHER_SKELETON, 0.65F),
            Map.entry(EntityType.FROG, 1.8F),
            Map.entry(EntityType.IRON_GOLEM, 0.65F),
            Map.entry(EntityType.PIGLIN, 0.8F),
            Map.entry(EntityType.BLAZE, 0.85F),
            Map.entry(EntityType.ELDER_GUARDIAN, 0.35F),
            Map.entry(EntityType.BOGGED, 0.75F),
            Map.entry(EntityType.MULE, 0.7F),
            Map.entry(EntityType.SNIFFER, 0.45F),
            Map.entry(EntityType.VINDICATOR, 0.8F),
            Map.entry(EntityType.ZOMBIE_HORSE, 0.55F),
            Map.entry(EntityType.CAMEL, 0.3F),
            Map.entry(EntityType.MAGMA_CUBE, 2.2F),
            Map.entry(EntityType.SPIDER, 0.8F),
            Map.entry(EntityType.MANNEQUIN, 0.8F),
            Map.entry(EntityType.EVOKER, 0.8F),
            Map.entry(EntityType.SLIME, 2F),
            Map.entry(EntityType.CAT, 2.2F),
            Map.entry(EntityType.PUFFERFISH, 2.6F),
            Map.entry(EntityType.FOX, 1.5F),
            Map.entry(EntityType.ALLAY, 2.4F),
            Map.entry(EntityType.TROPICAL_FISH, 2.95F),
            Map.entry(EntityType.OCELOT, 1.6F),
            Map.entry(EntityType.SILVERFISH, 1.7F),
            Map.entry(EntityType.ENDERMITE, 2.2F),
            Map.entry(EntityType.ARMADILLO, 1.9F),
            Map.entry(EntityType.CHICKEN, 1.55F),
            Map.entry(EntityType.ZOMBIE, 0.8F),
            Map.entry(EntityType.WOLF, 1.4F),
            Map.entry(EntityType.PANDA, 0.8F),
            Map.entry(EntityType.ZOMBIE_VILLAGER, 0.75F)
    );
}
