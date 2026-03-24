package games.polarbearbytes.mobxp.rules;

import games.polarbearbytes.mobxp.data.MobXPData;
import games.polarbearbytes.mobxp.gui.widgets.EditorContext;

/**
 * {@link XPRule} for Ender Dragon
 */
public class EnderDragonXPRule extends SecondaryXPRule {
    @Override
    public void onSelect(EditorContext ctx, MobXPData data) {
        super.onSelect(ctx, data);
        ctx.setLabels("First Kill", "All Other Kills");
    }
}
