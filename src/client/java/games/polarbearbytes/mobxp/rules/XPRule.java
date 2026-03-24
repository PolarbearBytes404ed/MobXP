package games.polarbearbytes.mobxp.rules;

import games.polarbearbytes.mobxp.data.MobXPData;
import games.polarbearbytes.mobxp.gui.widgets.EditorContext;

/**
 * Interface for rules to help deciding how to build our MobXPData, and what controls to display
 */
public interface XPRule {
    /** Called when a mob is selected */
    void onSelect(EditorContext ctx, MobXPData data);

    /** Create the MobXPData record from UI controls */
    MobXPData buildData(EditorContext ctx, String id);
}
