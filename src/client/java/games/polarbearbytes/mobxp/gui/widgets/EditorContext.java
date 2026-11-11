package games.polarbearbytes.mobxp.gui.widgets;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;

/**
 * class for applying actions like hiding, showing elements of details panel
 */
public record EditorContext(EditBox primaryXP, StringWidget primaryLabel, EditBox secondaryXP,
                            StringWidget secondaryLabel, Checkbox enabledCheckbox, Checkbox randomCheckbox,
                            StringWidget xpLabel,
                            StringWidget babyLabel, EditBox babyXP,
                            StringWidget mediumCubeLabel, EditBox mediumCubeXP,
                            StringWidget smallCubeLabel, EditBox smallCubeXP,
                            Checkbox usePrimaryXPForBaby,
                            Checkbox usePrimaryXPForAllCubes,
                            Button applyButton, Button saveCloseButton) {

    /**
     * Hide all the controls, labels
     */
    public void hideAll() {
        xpLabel.visible = false;
        primaryXP.visible = false;
        primaryLabel.visible = false;
        secondaryXP.visible = false;
        secondaryLabel.visible = false;
        enabledCheckbox.visible = false;
        randomCheckbox.visible = false;
        babyLabel.visible = false;
        babyXP.visible = false;
        mediumCubeLabel.visible = false;
        mediumCubeXP.visible = false;
        smallCubeLabel.visible = false;
        smallCubeXP.visible = false;
        usePrimaryXPForBaby.visible = false;
        usePrimaryXPForAllCubes.visible = false;
        applyButton.visible = false;
        saveCloseButton.visible = false;
    }

    /**
     * Show the controls, labels for normal mob details
     */
    public void showNormal() {
        xpLabel.visible = true;
        primaryXP.visible = true;
        enabledCheckbox.visible = true;
        randomCheckbox.visible = true;
        applyButton.visible = true;
        saveCloseButton.visible = true;
    }

    /**
     * Show the secondary controls, labels for mobs with secondary xp (like ender dragon)
     */
    public void showSecondary(boolean show) {
        secondaryXP.visible = show;
        primaryLabel.visible = show;
        secondaryLabel.visible = show;
    }

    /**
     * Show the baby detail controls, labels for mobs with baby xp (like baby zombie, animals etc.)
     */
    public void showBaby(boolean show) {
        babyLabel.visible = show;
        babyXP.visible = show;
        usePrimaryXPForBaby.visible = show;
    }

    /**
     * Show the cube detail controls, controls for cube type mobs xp (like slimes, magma, sulfur, etc.)
     */
    public void showCubes(boolean show) {
        mediumCubeLabel.visible = show;
        mediumCubeXP.visible = show;
        smallCubeLabel.visible = show;
        smallCubeXP.visible = show;
        usePrimaryXPForAllCubes.visible = show;
    }

    public void setLabels(String primaryLabel, String secondaryLabel) {
        this.primaryLabel.setMessage(Component.nullToEmpty(primaryLabel));
        this.secondaryLabel.setMessage(Component.nullToEmpty(secondaryLabel));
    }
}