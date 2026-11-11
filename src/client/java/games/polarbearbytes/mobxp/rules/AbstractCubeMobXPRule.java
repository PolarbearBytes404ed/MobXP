package games.polarbearbytes.mobxp.rules;

import games.polarbearbytes.mobxp.data.MobXPData;
import games.polarbearbytes.mobxp.gui.widgets.EditorContext;
import games.polarbearbytes.mobxp.mixin.client.CheckboxWidgetAccessor;
import games.polarbearbytes.mobxp.utils.Utils;

public class AbstractCubeMobXPRule  extends GeneralXPRule {
    @Override
    public void onSelect(EditorContext ctx, MobXPData data) {
        super.onSelect(ctx, data);
        ctx.primaryXP().setValue(valueOrDefault(data.primaryXP()));
        ctx.mediumCubeXP().setValue(valueOrDefault(data.mediumCubeXP()));
        ctx.smallCubeXP().setValue(valueOrDefault(data.smallCubeXP()));
        ((CheckboxWidgetAccessor) ctx.usePrimaryXPForAllCubes()).mobxp$setChecked(data.usePrimaryXPForAllCubes());
        ctx.showCubes(true);
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
                -1,
                Utils.tryParse(ctx.mediumCubeXP().getValue(), -1),
                Utils.tryParse(ctx.smallCubeXP().getValue(), -1),
                ctx.enabledCheckbox().selected(),
                ctx.randomCheckbox().selected(),
                false,
                ctx.usePrimaryXPForAllCubes().selected()
        );
    }
}