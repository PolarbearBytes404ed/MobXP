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
        xpLabel.setVisible(false);
        primaryXP.setVisible(false);
        primaryLabel.setVisible(false);
        secondaryXP.setVisible(false);
        secondaryLabel.setVisible(false);
        enabledCheckbox.setVisible(false);
        randomCheckbox.setVisible(false);
        babyLabel.setVisible(false);
        babyXP.setVisible(false);
        mediumCubeLabel.setVisible(false);
        mediumCubeXP.setVisible(false);
        smallCubeLabel.setVisible(false);
        smallCubeXP.setVisible(false);
        usePrimaryXPForBaby.setVisible(false);
        usePrimaryXPForAllCubes.setVisible(false);
        applyButton.setVisible(false);
        saveCloseButton.setVisible(false);
    }

    /**
     * Show the controls, labels for normal mob details
     */
    public void showNormal() {
        xpLabel.setVisible(true);
        primaryXP.setVisible(true);
        enabledCheckbox.setVisible(true);
        randomCheckbox.setVisible(true);
        applyButton.setVisible(true);
        saveCloseButton.setVisible(true);
    }

    /**
     * Show the secondary controls, labels for mobs with secondary xp (like ender dragon)
     */
    public void showSecondary(boolean show) {
        secondaryXP.setVisible(show);
        primaryLabel.setVisible(show);
        secondaryLabel.setVisible(show);
    }

    /**
     * Show the baby detail controls, labels for mobs with baby xp (like baby zombie, animals etc.)
     */
    public void showBaby(boolean show) {
        babyLabel.setVisible(show);
        babyXP.setVisible(show);
        usePrimaryXPForBaby.setVisible(show);
    }

    /**
     * Show the cube detail controls, controls for cube type mobs xp (like slimes, magma, sulfur, etc.)
     */
    public void showCubes(boolean show) {
        mediumCubeLabel.setVisible(show);
        mediumCubeXP.setVisible(show);
        smallCubeLabel.setVisible(show);
        smallCubeXP.setVisible(show);
        usePrimaryXPForAllCubes.setVisible(show);
    }

    public void setLabels(String primaryLabel, String secondaryLabel) {
        this.primaryLabel.setMessage(Component.nullToEmpty(primaryLabel));
        this.secondaryLabel.setMessage(Component.nullToEmpty(secondaryLabel));
    }
}