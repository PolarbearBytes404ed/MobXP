package games.polarbearbytes.mobxp.rules;

import games.polarbearbytes.mobxp.data.MobXPData;
import games.polarbearbytes.mobxp.gui.widgets.EditorContext;
import games.polarbearbytes.mobxp.mixin.client.CheckboxWidgetAccessor;
import games.polarbearbytes.mobxp.utils.Utils;

/**
 * General {@link XPRule} to apply for any mob that does not have / need a specific XPRule
 */
public class GeneralXPRule implements XPRule {
    /**
     * Sets the editor controls based on passed data, and uses EditorContext methods to show appropriate controls
     * @param ctx {@link EditorContext} that holds the controls for the editor and methods for making changes to the controls (visibility etc.)
     * @param data {@link MobXPData} record of mob's xp details
     */
    @Override
    public void onSelect(EditorContext ctx, MobXPData data) {
        ctx.primaryXP.setValue(valueOrDefault(data.primaryXP()));
        ctx.showSecondary(false);
        ctx.showBaby(false);

        ((CheckboxWidgetAccessor) ctx.enabledCheckbox).mobxp$setChecked(data.enabled());
        ((CheckboxWidgetAccessor) ctx.randomCheckbox).mobxp$setChecked(data.random());
        ctx.showNormal();
        ctx.setLabels("","");
    }

    /**
     * Builds the {@link MobXPData} record for general mobs, i.e. no secondary xp.
     * @param ctx {@link EditorContext} that holds the controls for the editor and methods for making changes to the controls (visibility etc.)
     * @param id Minecraft entity id
     * @return {@link MobXPData} record of mob's xp details
     */
    @Override
    public MobXPData buildData(EditorContext ctx, String id) {
        return new MobXPData(
                id,
                Utils.tryParse(ctx.primaryXP.getValue(), -1),
                -1,
                -1,
                ctx.enabledCheckbox.selected(),
                ctx.randomCheckbox.selected(),
                ctx.usePrimaryXPForBaby.selected()
        );
    }

    protected String valueOrDefault(Integer value) {
        return value == -1 ? "default" : value.toString();
    }
}
