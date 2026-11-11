package games.polarbearbytes.mobxp.gui.widgets;

import games.polarbearbytes.mobxp.data.MobXPData;
import games.polarbearbytes.mobxp.gui.screens.MobXPListScreen;
import games.polarbearbytes.mobxp.rules.XPRule;
import games.polarbearbytes.mobxp.rules.XPRules;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

/**
 * "Panel" widget for displaying the editor controls for mob xp details
 */
public class MobXPDetailsPanel implements Renderable, GuiEventListener, NarratableEntry {
    private boolean visible = true;
    private String title;
    private String id;

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    private final Font font;

    private final EditorContext editorContext;
    private XPRule activeRule;

    private static final int MARGIN = 5;
    private static final int MARGIN_LEFT = 20;
    private static final int MARGIN_RIGHT = 20;
    private static final int FIELD_HEIGHT = 20;
    private static final int LINE_HEIGHT = FIELD_HEIGHT + MARGIN;

    private final MobXPListScreen parent;

    public MobXPDetailsPanel(MobXPListScreen parent, int x, int y, int width, int height){
        this.parent = parent;
        font = Minecraft.getInstance().font;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        int placementX = x + MARGIN_LEFT;
        int placementY = y + 20;

        Component xpLabelText = Component.translatable("config.mobxp.xp.label");
        int xpLabelWidth = font.width(xpLabelText);

        int fieldX = placementX + xpLabelWidth + MARGIN;
        int fieldWidth = (width - (MARGIN_LEFT + MARGIN_RIGHT) - (xpLabelWidth + MARGIN) - MARGIN) / 2;

        StringWidget primaryXPLabel = new StringWidget(fieldX, placementY, fieldWidth, FIELD_HEIGHT, Component.translatable("config.mobxp.primaryxp.label"), font);
        StringWidget secondaryXPLabel = new StringWidget(fieldX + fieldWidth + MARGIN, placementY, fieldWidth, FIELD_HEIGHT, Component.translatable("config.mobxp.secondaryxp.label"), font);

        placementY += LINE_HEIGHT;

        StringWidget xpLabel = new StringWidget(placementX, placementY, xpLabelWidth, FIELD_HEIGHT, xpLabelText, font);

        EditBox primaryXPField = new EditBox(font, fieldX, placementY, fieldWidth, FIELD_HEIGHT, Component.translatable("config.mobxp.primaryxp.label"));
        EditBox secondaryXPField = new EditBox(font, fieldX + fieldWidth + MARGIN, placementY, fieldWidth, FIELD_HEIGHT, Component.translatable("config.mobxp.secondaryxp.label"));

        placementY += LINE_HEIGHT;

        Checkbox enabledCheckbox = Checkbox.builder(Component.translatable("config.mobxp.enabled.label"), font)
                .pos(placementX, placementY)
                .build();

        placementY += LINE_HEIGHT;

        Checkbox randomCheckbox = Checkbox.builder(Component.translatable("config.mobxp.use_randomxp.label"), font)
                .pos(placementX, placementY)
                .build();

        placementY += LINE_HEIGHT;

        Component babyLabelText = Component.translatable("config.mobxp.babyxp.label");
        int babyLabelWidth = font.width(babyLabelText);
        fieldX = placementX + babyLabelWidth + MARGIN;

        Component mediumCubeLabelText = Component.translatable("config.mobxp.mediumcubexp.label");
        int mediumCubeLabelWidth = font.width(mediumCubeLabelText);
        int mediumCubeFieldX = placementX + mediumCubeLabelWidth + MARGIN;

        Component smallCubeLabelText = Component.translatable("config.mobxp.smallcubexp.label");
        int smallCubeLabelWidth = font.width(smallCubeLabelText);
        int smallCubeFieldX = placementX + smallCubeLabelWidth + MARGIN;

        StringWidget babyLabel = new StringWidget(placementX, placementY, babyLabelWidth, FIELD_HEIGHT, babyLabelText, font);
        EditBox babyXPField = new EditBox(font, fieldX, placementY, fieldWidth, FIELD_HEIGHT, Component.literal("Value"));

        placementY += LINE_HEIGHT;
        Checkbox usePrimaryXPForBaby = Checkbox.builder(Component.translatable("config.mobxp.use_adultxp.label"), font)
                .pos(placementX, placementY)
                .build();

        placementY -= LINE_HEIGHT;
        StringWidget mediumCubeLabel = new StringWidget(placementX, placementY, mediumCubeLabelWidth, FIELD_HEIGHT, mediumCubeLabelText, font);
        EditBox mediumCubeXPField = new EditBox(font, mediumCubeFieldX, placementY, fieldWidth, FIELD_HEIGHT, Component.literal("Value"));

        placementY += LINE_HEIGHT;
        StringWidget smallCubeLabel = new StringWidget(placementX, placementY, smallCubeLabelWidth, FIELD_HEIGHT, smallCubeLabelText, font);
        EditBox smallCubeXPField = new EditBox(font, smallCubeFieldX, placementY, fieldWidth, FIELD_HEIGHT, Component.literal("Value"));

        placementY += LINE_HEIGHT;
        Checkbox usePrimaryXPForAllCubes = Checkbox.builder(Component.translatable("config.mobxp.use_adultxp_all_cubes.label"), font)
                .pos(placementX, placementY)
                .build();

        int startX = x + MARGIN;

        int buttonWidth = (width - MARGIN * 3) / 2;
        int buttonY = height - FIELD_HEIGHT;

        Button applyButton = Button.builder(Component.translatable("config.mobxp.apply_button.label"), _ -> applyChanges())
                .pos(startX, buttonY)
                .size(buttonWidth, FIELD_HEIGHT)
                .build();
        Button saveButton = Button.builder(Component.translatable("config.mobxp.save_button.label"), _ -> saveAndClose())
                .pos(startX + buttonWidth + MARGIN, buttonY)
                .size(buttonWidth, FIELD_HEIGHT)
                .build();

        parent.add(this);
        parent.add(xpLabel);
        parent.add(primaryXPField);
        parent.add(secondaryXPField);

        parent.add(primaryXPLabel);
        parent.add(secondaryXPLabel);

        parent.add(enabledCheckbox);
        parent.add(randomCheckbox);

        parent.add(babyLabel);
        parent.add(babyXPField);
        parent.add(mediumCubeLabel);
        parent.add(mediumCubeXPField);
        parent.add(smallCubeLabel);
        parent.add(smallCubeXPField);
        parent.add(usePrimaryXPForBaby);
        parent.add(usePrimaryXPForAllCubes);

        parent.add(applyButton);
        parent.add(saveButton);

        editorContext = new EditorContext(
                primaryXPField,
                primaryXPLabel,
                secondaryXPField,
                secondaryXPLabel,
                enabledCheckbox,
                randomCheckbox,
                xpLabel,
                babyLabel,
                babyXPField,
                mediumCubeLabel,
                mediumCubeXPField,
                smallCubeLabel,
                smallCubeXPField,
                usePrimaryXPForBaby,
                usePrimaryXPForAllCubes,
                applyButton,
                saveButton
        );

        editorContext.hideAll();
    }

    /**
     * Sets the detail controls to the data in {@code details}, gets XPRule based on EntityType and displays the controls appropriately
     * @param details The mob's xp details (xp amount, enabled, use random amount of xp)
     */
    public void setDetails(@Nullable MobXPData details){
        if(details == null){
            id = "";
            title = "";
            activeRule = null;
            editorContext.hideAll();
            visible = false;
            return;
        }
        visible = true;
        id = details.id();
        title = details.getName();
        activeRule = XPRules.forEntity(id);
        activeRule.onSelect(editorContext, details);
    }

    /**
     * Builds the MobXPData record from the detail controls based on XPRule
     * @return {@link MobXPData}
     */
    public MobXPData getDetails(){
        return activeRule.buildData(editorContext, id);
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor context) {
        if(!visible) return;
        context.fill(x,y, x+width, y+height, 0x99000000);
        context.fill(x,y, x+width, y+24, 0xFF0a0a0a);

        context.centeredText(font, title, x + (width / 2), y+8, 0xFFFFFFFF);
    }

    /**
     * Saves the changes of the currently selected mob
     */
    private void applyChanges(){
        this.parent.applyChanges(getDetails());
    }

    /**
     * Apply the changes and close screen
     */
    private void saveAndClose(){
        this.parent.saveAndClose(getDetails());
    }

    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public @NonNull NarrationPriority narrationPriority() {
        if (isFocused()) {
            return NarratableEntry.NarrationPriority.FOCUSED;
        }

        return NarratableEntry.NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {
        output.add(
                NarratedElementType.TITLE,
                Component.literal("Mob XP Details")
        );
    }
}