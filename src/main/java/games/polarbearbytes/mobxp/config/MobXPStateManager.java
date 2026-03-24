package games.polarbearbytes.mobxp.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.polarbearbytes.mobxp.data.MobXPData;
import net.minecraft.resources.Identifier;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static games.polarbearbytes.mobxp.MobXP.MOD_ID;


/**
 * State manager for maintaining the custom mob xp
 */
public class MobXPStateManager extends SavedData {
    private HashMap<String, MobXPData> mobXPDataList = new HashMap<>();

    public static final Codec<MobXPStateManager> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(
                    Codec.STRING,
                    MobXPData.CODEC
            ).fieldOf("mobXPDataList").forGetter(MobXPStateManager::getRawMap)
    ).apply(instance, MobXPStateManager::new));

    public static final SavedDataType<MobXPStateManager> TYPE = new SavedDataType<>(Identifier.fromNamespaceAndPath(MOD_ID,"mob_xp_data"),MobXPStateManager::new, CODEC, DataFixTypes.PLAYER);

    public MobXPStateManager(){}

    public MobXPStateManager(Map<String, MobXPData> mobXPDataList){
        this.mobXPDataList = new HashMap<>();
        this.mobXPDataList.putAll(mobXPDataList);
    }

    public static MobXPStateManager get(MinecraftServer server){
        return server.overworld().getDataStorage().computeIfAbsent(TYPE);
    }

    private Map<String, MobXPData> getRawMap(){
        return this.mobXPDataList;
    }

    /**
     * Updates the mob state data in our hashmap.
     *
     * @param mobData The mob xp details
     */
    public void updateState(MobXPData mobData){
        this.mobXPDataList.put(mobData.id(),mobData);
        setDirty();
    }

    /**
     * Get the xp data of a mob
     *
     * @param id The id of the mob
     * @return MobXPData the mob's xp details
     */
    public MobXPData getMobData(String id){
        return this.mobXPDataList.getOrDefault(id, MobXPData.EMPTY(id));
    }

    public List<MobXPData> getList(){
        return mobXPDataList.values().stream().toList();
    }
}

