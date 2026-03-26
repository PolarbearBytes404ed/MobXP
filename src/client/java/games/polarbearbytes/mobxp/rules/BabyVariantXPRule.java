package games.polarbearbytes.mobxp.rules;

import games.polarbearbytes.mobxp.data.MobXPData;
import games.polarbearbytes.mobxp.gui.widgets.EditorContext;
import games.polarbearbytes.mobxp.mixin.client.CheckboxWidgetAccessor;
import games.polarbearbytes.mobxp.utils.Utils;

/**
 * Rule for mobs that can have baby versions like animals, zombies, etc.
 */
public class BabyVariantXPRule extends GeneralXPRule {
    @Override
    public void onSelect(EditorContext ctx, MobXPData data) {
        super.onSelect(ctx, data);
        ctx.babyXP().setValue(valueOrDefault(data.babyXP()));
        ((CheckboxWidgetAccessor) ctx.usePrimaryXPForBaby()).mobxp$setChecked(data.usePrimaryXPForBaby());
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
                -1,
                Utils.tryParse(ctx.babyXP().getValue(), -1),
                ctx.enabledCheckbox().selected(),
                ctx.randomCheckbox().selected(),
                ctx.usePrimaryXPForBaby().selected()
        );
    }
}
