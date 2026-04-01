package games.polarbearbytes.mobxp.gui.widgets;

import games.polarbearbytes.mobxp.data.MobXPData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.ObjectSelectionList.Entry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

/**
 * Custom {@link #Entry} class for our {@link ObjectSelectionList}, display mob entity model, name, xp amount, and options
 */
public class MobXPEntry extends Entry<MobXPEntry> {
    private MobXPData data;
    private final String title;
    private MobWidget mobWidget;
    private static final int MARGIN = 5;

    public MobXPEntry(MobXPData data) {
        this.data = data;
        this.title = data.getName();
    }

    @Override
    public int getHeight(){
        return super.getHeight() + MARGIN * 2;
    }

    @Override
    public void setHeight(int height){
        super.setHeight(height);
        this.mobWidget = new MobWidget(BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.parse(data.id())), getContentX(), getContentY(), getContentHeight(), getContentHeight());
    }

    @Override
    public void setWidth(int width){
        super.setWidth(width);
        this.mobWidget = new MobWidget(BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.parse(data.id())), getContentX(), getContentY(), getContentHeight(), getContentHeight());
    }

    /**
     * Method to detect if this entry matches the passed string, based on the minecraft ID and readable name
     * @param match string to match against
     * @return {@code boolean} true if matched, false otherwise
     */
    public boolean matches(String match){
        return data.id().contains(match.toLowerCase()) || title.toLowerCase().contains(match.toLowerCase());
    }

    public String getTitle(){
        return title;
    }

    public MobXPData getData(){
        return data;
    }
    public void setData(MobXPData data){
        this.data = data;
    }

    @Override
    public void extractContent(@NonNull GuiGraphicsExtractor context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        Font font = Minecraft.getInstance().font;

        this.mobWidget.extractRenderState(context,mouseX,mouseY,deltaTicks);

        int labelHeight = font.lineHeight + 2;

        int textX = getContentHeight() + MARGIN * 2;
        int textY = getContentY() + MARGIN;

        context.text(font, data.getName(), textX,  textY, 0xFFFFFFFF, false);
        String xp = String.valueOf(data.primaryXP());
        xp = xp.equals("-1") ? "default" : xp;

        context.text(font, Component.translatable("config.mobxp.xp",xp).getString(), textX, textY+labelHeight, 0xFFFFFFFF, false);
        context.text(font, Component.translatable("config.mobxp.enabled", data.enabled()).getString(), textX, textY+labelHeight*2, 0xFFFFFFFF, false);
        context.text(font, Component.translatable("config.mobxp.use_randomxp", data.random()).getString(), textX, textY+labelHeight*3, 0xFFFFFFFF, false);
    }

    @Override
    public @NonNull Component getNarration() {
        return Component.nullToEmpty( data.getName() );
    }
}