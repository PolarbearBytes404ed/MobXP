package games.polarbearbytes.mobxp.gui.screens;

import games.polarbearbytes.mobxp.MobXPClient;
import games.polarbearbytes.mobxp.data.MobXPData;
import games.polarbearbytes.mobxp.gui.widgets.*;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;

/**
 * Screen for modifying mob xp
 */
public class MobXPListScreen extends Screen {
    private MobXPListWidget listWidget = null;
    private MobXPDetailsPanel detailsPanel;

    private static final float LIST_WIDTH_PERCENTAGE = 0.40F;
    private static final int MARGIN = 8;
    private static final int ITEMS_VISIBLE = 4;
    private static final int FIELD_HEIGHT = 20;

    private EditBox searchField;

    public MobXPListScreen() {
        super(Component.literal("Mob XP Editor"));
    }

    /**
     * Called when mob is selected from {@link #listWidget}
     * @param entry the mob entry that was selected
     */
    public void onMobSelected(MobXPEntry entry){
        if(entry == null) {
            detailsPanel.setDetails(null);
            return;
        }
        detailsPanel.setDetails(entry.getData());
    }

    @Override
    protected void init(){
        String lastSearch = "";
        if(this.searchField != null){
            lastSearch = this.searchField.getValue();
        }
        int listWidth = (int) (LIST_WIDTH_PERCENTAGE * width) - MARGIN * 2;

        Component labelText = Component.literal("Search:");
        int labelWidth = font.width(labelText) + MARGIN;
        int fieldWidth = listWidth - labelWidth;

        int layoutX = MARGIN;
        int layoutY = MARGIN;

        StringWidget searchLabelWidget = new StringWidget(layoutX, layoutY, labelWidth, FIELD_HEIGHT, labelText, font);
        searchField = new EditBox(font, layoutX + labelWidth, layoutY, fieldWidth, FIELD_HEIGHT, Component.literal(lastSearch));
        searchField.setValue(lastSearch);
        layoutY += FIELD_HEIGHT + MARGIN;
        int detailsWidth = width - listWidth - MARGIN * 3;
        int detailsHeight = height - FIELD_HEIGHT - MARGIN * 3;

        listWidget = new MobXPListWidget(this, layoutX, layoutY, listWidth, height - FIELD_HEIGHT - MARGIN * 3, ITEMS_VISIBLE, this::onMobSelected, listWidget);
        detailsPanel = new MobXPDetailsPanel(this, layoutX + listWidth + MARGIN, MARGIN, detailsWidth, detailsHeight);

        searchField.setResponder(listWidget::filter);

        int buttonWidth = width - listWidth - MARGIN * 3;
        int buttonY = height - FIELD_HEIGHT - MARGIN;

        Button cancelButton = Button.builder(Component.literal("Cancel"), _ -> onClose())
                .pos(listWidth + MARGIN * 2, buttonY)
                .size(buttonWidth, FIELD_HEIGHT)
                .build();

        listWidget.init();
        addRenderableWidget(searchLabelWidget);
        addRenderableWidget(searchField);

        addRenderableWidget(cancelButton);

        addRenderableWidget(listWidget);
    }

    public String getSearch(){
        return searchField.getValue();
    }

    /**
     * Update the mob list with data from server
     * @param dataList hash map of mob xp details keyed on minecraft id
     */
    public void updateList(HashMap<String, MobXPData> dataList){
        listWidget.update(dataList);
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks) {
        super.extractRenderState(context, mouseX, mouseY, deltaTicks);
    }

    public <T extends Renderable & GuiEventListener & NarratableEntry> void add(T drawableElement){
        this.addRenderableWidget(drawableElement);
    }

    public <T extends Renderable> T addDrawable(T drawable){
        return super.addRenderableOnly(drawable);
    }


    /**
     * Saves the changes of the currently selected mob to the respective places, and sends packet to server for updating on server side
     */
    public void applyChanges(MobXPData data){
        MobXPEntry entry = listWidget.getSelected();
        if(entry == null) return;
        entry.setData(data);
        MobXPClient.updateData(data);
    }

    /**
     * Apply the changes and close screen
     */
    public void saveAndClose(MobXPData data){
        applyChanges(data);
        onClose();
    }
}
