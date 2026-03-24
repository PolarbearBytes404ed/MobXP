package games.polarbearbytes.mobxp.rules;

import games.polarbearbytes.mobxp.data.MobXPData;
import games.polarbearbytes.mobxp.gui.widgets.EditorContext;

/**
 * {@link XPRule} for Ender Dragon
 */
public class ChickenXPRule extends AllXPRule {
    @Override
    public void onSelect(EditorContext ctx, MobXPData data) {
        super.onSelect(ctx, data);
        ctx.setLabels("Normal", "Has Jockey");
    }
}