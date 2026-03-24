package games.polarbearbytes.mobxp.rules;

import games.polarbearbytes.mobxp.data.MobXPData;
import games.polarbearbytes.mobxp.gui.widgets.EditorContext;
import games.polarbearbytes.mobxp.utils.Utils;

/**
 * General rule for mobs that need to use the secondary xp, like chicken jockey, baby zombies, etc.
 */
public class SecondaryXPRule extends GeneralXPRule {
    @Override
    public void onSelect(EditorContext ctx, MobXPData data) {
        super.onSelect(ctx, data);
        ctx.secondaryXP.setValue(valueOrDefault(data.secondaryXP()));
        ctx.showSecondary(true);
    }

    /**
     * Builds the {@link MobXPData} record to include the secondary xp amount in the record
     * @param ctx {@link EditorContext} that controls the detail controls
     * @param id  Minecraft entity id
     * @return {@link MobXPData} record of mob xp details
     */
    @Override
    public MobXPData buildData(EditorContext ctx, String id) {
        return new MobXPData(
                id,
                Utils.tryParse(ctx.primaryXP.getValue(), -1),
                Utils.tryParse(ctx.secondaryXP.getValue(), -1),
                -1,
                ctx.enabledCheckbox.selected(),
                ctx.randomCheckbox.selected(),
                ctx.usePrimaryXPForBaby.selected()
        );
    }
}
