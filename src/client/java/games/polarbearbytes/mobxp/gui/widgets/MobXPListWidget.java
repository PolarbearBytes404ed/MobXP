package games.polarbearbytes.mobxp.gui.widgets;

import games.polarbearbytes.mobxp.data.MobXPData;
import games.polarbearbytes.mobxp.gui.screens.MobXPListScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Widget to list our custom mob entries
 */
public class MobXPListWidget extends ObjectSelectionList<MobXPEntry> {
    private final MobXPListScreen parent;
    private String previousFilter = "";
    private MobXPEntry previousSelected = null;
    private Double lastScrollY = null;
    private final Callback callback;
    private List<MobXPEntry> masterList = Lists.newArrayList();
    private HashMap<String, List<MobXPEntry>> filteredLists = new HashMap<>();

    public MobXPListWidget(MobXPListScreen parent, int x, int y, int width, int height, int numberItems, Callback selectCallback, MobXPListWidget previousList) {
        super(Minecraft.getInstance(), width, height, 0, height / numberItems);
        this.parent = parent;
        this.callback = selectCallback;
        this.setPosition(x, y);
        if(previousList != null){
            this.masterList = previousList.masterList;
            this.filteredLists = previousList.filteredLists;
            this.lastScrollY = previousList.scrollAmount();
            this.previousSelected = previousList.getSelected();
        }
    }

    @Override
    public int getRowWidth(){
        return this.width;
    }

    @Override
    public int scrollBarX(){
        return this.width + 2;
    }

    public void init(){
        refresh();
        if(this.lastScrollY != null){
            this.setScrollAmount(this.lastScrollY);
            this.lastScrollY = null;
        }
    }

    /**
     * Called to set a newly created instance with values from the old one. Needs to happen when screen is resized.
     */
    public void refresh(){
        masterList.forEach(this::addEntry);
        filter(this.parent.getSearch());
        this.setSelected(this.previousSelected);
        this.previousSelected = null;
    }

    @Override
    public void setSelected(@Nullable MobXPEntry entry) {
        super.setSelected(entry);
        this.callback.onValueChange(entry);
    }

    /**
     * Update the mob list to MobXPEntry entries created and sorted from the data list passed
     * @param dataList mob xp data list
     */
    public void update(HashMap<String, MobXPData> dataList) {
        List<MobXPEntry> list = dataList.values().stream()
                .sorted(Comparator.comparing(MobXPData::id))
                .map(MobXPEntry::new).toList();
        masterList = list;
        filteredLists.clear();
        clearEntries();
        list.forEach(this::addEntry);
    }

    /**
     * Callback interface to allow for passing a callback to tbe called when an entry has been selected
     */
    @Environment(EnvType.CLIENT)
    public interface Callback {
        void onValueChange(MobXPEntry entry);
    }

    /**
     * Search field changes calls this to filter the entries of the list, caches filtered lists to be more efficient (though it probably is a negligible amount in this instance)
     * @param filterString string to filter entries by
     */
    public void filter(String filterString){
        if(filterString.equals(previousFilter)) return;
        previousFilter = filterString;
        replaceEntries(
                filteredLists.computeIfAbsent(filterString,(filter )->
                        masterList.stream().filter(entry->
                                        entry.matches(filter))
                                .sorted(Comparator
                                        .comparing( MobXPEntry::getTitle )).toList()
                )
        );
        setSelected(null);
        setScrollAmount(0);
    }

    @Override
    protected void extractSelection(GuiGraphicsExtractor context, MobXPEntry entry, int color) {
        int i = entry.getX();
        int j = entry.getY();
        int k = i + entry.getWidth() - (this.scrollable() ? 6 : 0);
        int l = j + entry.getHeight();
        context.fill(i, j, k, l, color);
        context.fill(i + 1, j + 1, k - 1, l - 1, -16777216);
    }

}
