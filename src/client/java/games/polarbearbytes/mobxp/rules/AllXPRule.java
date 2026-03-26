package games.polarbearbytes.mobxp.rules;

import games.polarbearbytes.mobxp.data.MobXPData;
import games.polarbearbytes.mobxp.gui.widgets.EditorContext;
import games.polarbearbytes.mobxp.utils.Utils;

/**
 * Rule for mobs that need all the details, like chickens as they could have separate xp for babies, jockeyed, and adult versions
 */
public class AllXPRule extends GeneralXPRule {
    @Override
    public void onSelect(EditorContext ctx, MobXPData data) {
        super.onSelect(ctx, data);
        ctx.secondaryXP().setValue(valueOrDefault(data.secondaryXP()));
        ctx.babyXP().setValue(valueOrDefault(data.babyXP()));
        ctx.showSecondary(true);
        ctx.showBaby(true);
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
                Utils.tryParse(ctx.primaryXP().getValue(), -1),
                Utils.tryParse(ctx.secondaryXP().getValue(), -1),
                Utils.tryParse(ctx.babyXP().getValue(), -1),
                ctx.enabledCheckbox().selected(),
                ctx.randomCheckbox().selected(),
                ctx.usePrimaryXPForBaby().selected()
        );
    }
}