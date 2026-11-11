package games.polarbearbytes.mobxp.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Widget for displaying a mob in teh same fashion the player is displayed in the inventory screen, e.g. follows mouse etc.
 * Uses InventoryScreen#drawEntity() to do the actual displaying
 */
public class MobWidget implements Renderable {
    protected LivingEntity entity;
    private static final AtomicInteger NEXT_WIDGET_ENTITY_ID = new AtomicInteger(-1);
    protected int x;
    protected int y;

    protected int width;
    protected int height;

    public MobWidget(EntityType<?> entityType, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        entity = (LivingEntity) entityType.create(Objects.requireNonNull(Minecraft.getInstance().level), EntitySpawnReason.EVENT);
        assert entity != null;

        entity.setId(NEXT_WIDGET_ENTITY_ID.getAndDecrement());
        if(NEXT_WIDGET_ENTITY_ID.get() == Integer.MIN_VALUE){
            NEXT_WIDGET_ENTITY_ID.set(-1);
        }

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
    public void extractRenderState(@NonNull GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks) {
        double offset = MODEL_OFFSET_FIX.getOrDefault(entity.getType(), 1F);
        double scale = MODEL_SCALE_FIX.getOrDefault(entity.getType(), 1F);
        render(scale, offset, context, mouseX, mouseY);
    }

    /**
     * Map of scales for specific mobs to use to make sure entity scales to fit into the region specified
     */
    private static final Map<EntityType<?>, Float> MODEL_SCALE_FIX = Map.<EntityType<?>, Float>ofEntries(
            Map.entry(EntityTypes.GIANT, 0.16666F),
            Map.entry(EntityTypes.GLOW_SQUID, 0.9F),
            Map.entry(EntityTypes.CAMEL_HUSK, 0.7F),
            Map.entry(EntityTypes.TRADER_LLAMA, 0.9F),
            Map.entry(EntityTypes.WARDEN, 0.6F),
            Map.entry(EntityTypes.HORSE, 0.9F),
            Map.entry(EntityTypes.WITCH, 0.9F),
            Map.entry(EntityTypes.LLAMA, 0.9F),
            Map.entry(EntityTypes.PIG, 1.5F),
            Map.entry(EntityTypes.SHEEP, 1.2F),
            Map.entry(EntityTypes.ENDER_DRAGON, 0.3F),
            Map.entry(EntityTypes.CAVE_SPIDER, 2F),
            Map.entry(EntityTypes.TADPOLE, 3F),
            Map.entry(EntityTypes.ENDERMAN, 0.8F),
            Map.entry(EntityTypes.GOAT, 1.1F),
            Map.entry(EntityTypes.DOLPHIN, 1.3F),
            Map.entry(EntityTypes.VEX, 1.5F),
            Map.entry(EntityTypes.COD, 2.5F),
            Map.entry(EntityTypes.DONKEY, 1.2F),
            Map.entry(EntityTypes.RAVAGER, 0.7F),
            Map.entry(EntityTypes.GHAST, 0.25F),
            Map.entry(EntityTypes.SKELETON_HORSE, 0.9F),
            Map.entry(EntityTypes.COW, 1.3F),
            Map.entry(EntityTypes.CREAKING, 0.8F),
            Map.entry(EntityTypes.NAUTILUS, 1.2F),
            Map.entry(EntityTypes.CREEPER, 1.3F),
            Map.entry(EntityTypes.BEE, 2F),
            Map.entry(EntityTypes.SALMON, 2F),
            Map.entry(EntityTypes.SHULKER, 1.2F),
            Map.entry(EntityTypes.MOOSHROOM, 0.9F),
            Map.entry(EntityTypes.RABBIT, 2.5F),
            Map.entry(EntityTypes.AXOLOTL, 2F),
            Map.entry(EntityTypes.HAPPY_GHAST, 0.35F),
            Map.entry(EntityTypes.GUARDIAN, 1.2F),
            Map.entry(EntityTypes.PARROT, 2F),
            Map.entry(EntityTypes.COPPER_GOLEM, 1.4F),
            Map.entry(EntityTypes.WITHER, 0.7F),
            Map.entry(EntityTypes.ZOMBIE_NAUTILUS, 1.2F),
            Map.entry(EntityTypes.BAT, 2F),
            Map.entry(EntityTypes.WITHER_SKELETON, 0.85F),
            Map.entry(EntityTypes.FROG, 2F),
            Map.entry(EntityTypes.IRON_GOLEM, 0.8F),
            Map.entry(EntityTypes.BLAZE, 1.2F),
            Map.entry(EntityTypes.ELDER_GUARDIAN, 0.6F),
            Map.entry(EntityTypes.SNIFFER, 0.65F),
            Map.entry(EntityTypes.CAMEL, 0.7F),
            Map.entry(EntityTypes.MAGMA_CUBE, 2.5F),
            Map.entry(EntityTypes.MANNEQUIN, 1.05F),
            Map.entry(EntityTypes.SLIME, 2.3F),
            Map.entry(EntityTypes.CAT, 2.5F),
            Map.entry(EntityTypes.PUFFERFISH, 3F),
            Map.entry(EntityTypes.FOX, 1.8F),
            Map.entry(EntityTypes.ALLAY, 2.7F),
            Map.entry(EntityTypes.TROPICAL_FISH, 3F),
            Map.entry(EntityTypes.OCELOT, 2F),
            Map.entry(EntityTypes.SILVERFISH, 2F),
            Map.entry(EntityTypes.ENDERMITE, 2.5F),
            Map.entry(EntityTypes.ARMADILLO, 2.2F),
            Map.entry(EntityTypes.CHICKEN, 2F),
            Map.entry(EntityTypes.WOLF, 1.7F)
    );

    /**
     * Map of Y offsets for specific mobs to make sure the mob is centered in the specified region vertically
     */
    private static final Map<EntityType<?>, Float> MODEL_OFFSET_FIX = Map.<EntityType<?>, Float>ofEntries(
            Map.entry(EntityTypes.GIANT, 0.1F),
            Map.entry(EntityTypes.GLOW_SQUID, 1.25F),
            Map.entry(EntityTypes.HOGLIN, 0.65F),
            Map.entry(EntityTypes.CAMEL_HUSK, 0.3F),
            Map.entry(EntityTypes.TRADER_LLAMA, 0.6F),
            Map.entry(EntityTypes.BREEZE, 0.85F),
            Map.entry(EntityTypes.WARDEN, 0.4F),
            Map.entry(EntityTypes.HORSE, 0.5F),
            Map.entry(EntityTypes.POLAR_BEAR, 0.8F),
            Map.entry(EntityTypes.PHANTOM, 0.8F),
            Map.entry(EntityTypes.ZOMBIFIED_PIGLIN, 0.8F),
            Map.entry(EntityTypes.WITCH, 0.6F),
            Map.entry(EntityTypes.LLAMA, 0.65F),
            Map.entry(EntityTypes.PIG, 1.2F),
            Map.entry(EntityTypes.HUSK, 0.7F),
            Map.entry(EntityTypes.TURTLE, 0.8F),
            Map.entry(EntityTypes.SHEEP, 0.9F),
            Map.entry(EntityTypes.ENDER_DRAGON, 0.6F),
            Map.entry(EntityTypes.CAVE_SPIDER, 1.6F),
            Map.entry(EntityTypes.TADPOLE, 2.7F),
            Map.entry(EntityTypes.ENDERMAN, 0.6F),
            Map.entry(EntityTypes.GOAT, 0.7F),
            Map.entry(EntityTypes.DOLPHIN, 1.1F),
            Map.entry(EntityTypes.VEX, 1.3F),
            Map.entry(EntityTypes.COD, 2.3F),
            Map.entry(EntityTypes.SKELETON, 0.8F),
            Map.entry(EntityTypes.PARCHED, 0.8F),
            Map.entry(EntityTypes.DONKEY, 0.9F),
            Map.entry(EntityTypes.RAVAGER, 0.5F),
            Map.entry(EntityTypes.SQUID, 1.4F),
            Map.entry(EntityTypes.GHAST, 0.5F),
            Map.entry(EntityTypes.SKELETON_HORSE, 0.6F),
            Map.entry(EntityTypes.CREAKING, 0.65F),
            Map.entry(EntityTypes.NAUTILUS, 0.9F),
            Map.entry(EntityTypes.PIGLIN_BRUTE, 0.8F),
            Map.entry(EntityTypes.CREEPER, 1.1F),
            Map.entry(EntityTypes.BEE, 1.7F),
            Map.entry(EntityTypes.ILLUSIONER, 0.8F),
            Map.entry(EntityTypes.SALMON, 1.8F),
            Map.entry(EntityTypes.SNOW_GOLEM, 0.8F),
            Map.entry(EntityTypes.MOOSHROOM, 0.55F),
            Map.entry(EntityTypes.VILLAGER, 0.8F),
            Map.entry(EntityTypes.STRAY, 0.8F),
            Map.entry(EntityTypes.DROWNED, 0.75F),
            Map.entry(EntityTypes.RABBIT, 2.1F),
            Map.entry(EntityTypes.AXOLOTL, 1.7F),
            Map.entry(EntityTypes.PILLAGER, 0.8F),
            Map.entry(EntityTypes.ZOGLIN, 0.8F),
            Map.entry(EntityTypes.STRIDER, 0.75F),
            Map.entry(EntityTypes.HAPPY_GHAST, 0.5F),
            Map.entry(EntityTypes.GUARDIAN, 0.9F),
            Map.entry(EntityTypes.PARROT, 1.85F),
            Map.entry(EntityTypes.COPPER_GOLEM, 0.85F),
            Map.entry(EntityTypes.WITHER, 0.2F),
            Map.entry(EntityTypes.ZOMBIE_NAUTILUS, 0.9F),
            Map.entry(EntityTypes.BAT, 1.7F),
            Map.entry(EntityTypes.WANDERING_TRADER, 0.8F),
            Map.entry(EntityTypes.WITHER_SKELETON, 0.65F),
            Map.entry(EntityTypes.FROG, 1.8F),
            Map.entry(EntityTypes.IRON_GOLEM, 0.65F),
            Map.entry(EntityTypes.PIGLIN, 0.8F),
            Map.entry(EntityTypes.BLAZE, 0.85F),
            Map.entry(EntityTypes.ELDER_GUARDIAN, 0.35F),
            Map.entry(EntityTypes.BOGGED, 0.75F),
            Map.entry(EntityTypes.MULE, 0.7F),
            Map.entry(EntityTypes.SNIFFER, 0.45F),
            Map.entry(EntityTypes.VINDICATOR, 0.8F),
            Map.entry(EntityTypes.ZOMBIE_HORSE, 0.55F),
            Map.entry(EntityTypes.CAMEL, 0.3F),
            Map.entry(EntityTypes.MAGMA_CUBE, 2.2F),
            Map.entry(EntityTypes.SPIDER, 0.8F),
            Map.entry(EntityTypes.MANNEQUIN, 0.8F),
            Map.entry(EntityTypes.EVOKER, 0.8F),
            Map.entry(EntityTypes.SLIME, 2F),
            Map.entry(EntityTypes.CAT, 2.2F),
            Map.entry(EntityTypes.PUFFERFISH, 2.6F),
            Map.entry(EntityTypes.FOX, 1.5F),
            Map.entry(EntityTypes.ALLAY, 2.4F),
            Map.entry(EntityTypes.TROPICAL_FISH, 2.95F),
            Map.entry(EntityTypes.OCELOT, 1.6F),
            Map.entry(EntityTypes.SILVERFISH, 1.7F),
            Map.entry(EntityTypes.ENDERMITE, 2.2F),
            Map.entry(EntityTypes.ARMADILLO, 1.9F),
            Map.entry(EntityTypes.CHICKEN, 1.55F),
            Map.entry(EntityTypes.ZOMBIE, 0.8F),
            Map.entry(EntityTypes.WOLF, 1.4F),
            Map.entry(EntityTypes.PANDA, 0.8F),
            Map.entry(EntityTypes.ZOMBIE_VILLAGER, 0.75F)
    );
}
