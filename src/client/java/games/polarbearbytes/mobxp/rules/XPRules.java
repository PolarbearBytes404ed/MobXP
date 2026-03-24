package games.polarbearbytes.mobxp.rules;

import java.util.HashMap;
import java.util.Map;

/**
 * XPRules Registry
 */
public final class XPRules {
    private static final XPRule DEFAULT = new GeneralXPRule();

    private static final Map<String, XPRule> RULES;
    static {
        Map<String, XPRule> tmp = new HashMap<>();
        //Dragons have different xp on first kill vs other kills
        tmp.put("minecraft:ender_dragon", new EnderDragonXPRule());
        //Chickens have different xp based on if it is a baby, has a jockey, or is an adult
        tmp.put("minecraft:chicken", new ChickenXPRule());
        //Mobs that have a baby variant
        tmp.put("minecraft:hoglin", new BabyVariantXPRule());
        tmp.put("minecraft:zoglin", new BabyVariantXPRule());
        tmp.put("minecraft:camel_husk", new BabyVariantXPRule());
        tmp.put("minecraft:camel", new BabyVariantXPRule());
        tmp.put("minecraft:cow", new BabyVariantXPRule());
        tmp.put("minecraft:fox", new BabyVariantXPRule());
        tmp.put("minecraft:pig", new BabyVariantXPRule());
        tmp.put("minecraft:villager", new BabyVariantXPRule());
        tmp.put("minecraft:zombie_horse", new BabyVariantXPRule());
        tmp.put("minecraft:piglin", new BabyVariantXPRule());
        tmp.put("minecraft:zombified_piglin", new BabyVariantXPRule());
        tmp.put("minecraft:sheep", new BabyVariantXPRule());
        tmp.put("minecraft:trader_llama", new BabyVariantXPRule());
        tmp.put("minecraft:horse", new BabyVariantXPRule());
        tmp.put("minecraft:mule", new BabyVariantXPRule());
        tmp.put("minecraft:donkey", new BabyVariantXPRule());
        tmp.put("minecraft:polar_bear", new BabyVariantXPRule());
        tmp.put("minecraft:llama", new BabyVariantXPRule());
        tmp.put("minecraft:turtle", new BabyVariantXPRule());
        tmp.put("minecraft:goat", new BabyVariantXPRule());
        tmp.put("minecraft:nautilus", new BabyVariantXPRule());
        tmp.put("minecraft:bee", new BabyVariantXPRule());
        tmp.put("minecraft:mooshroom", new BabyVariantXPRule());
        tmp.put("minecraft:drowned", new BabyVariantXPRule());
        tmp.put("minecraft:zombie", new BabyVariantXPRule());
        tmp.put("minecraft:rabbit", new BabyVariantXPRule());
        tmp.put("minecraft:axolotl", new BabyVariantXPRule());
        tmp.put("minecraft:strider", new BabyVariantXPRule());
        tmp.put("minecraft:happy_ghast", new BabyVariantXPRule());
        tmp.put("minecraft:parrot", new BabyVariantXPRule());
        tmp.put("minecraft:cat", new BabyVariantXPRule());
        tmp.put("minecraft:ocelot", new BabyVariantXPRule());
        tmp.put("minecraft:sniffer", new BabyVariantXPRule());
        tmp.put("minecraft:armadillo", new BabyVariantXPRule());
        tmp.put("minecraft:wolf", new BabyVariantXPRule());
        tmp.put("minecraft:panda", new BabyVariantXPRule());
        tmp.put("minecraft:zombie_villager", new BabyVariantXPRule());

        RULES = Map.copyOf(tmp);
    }

    public static XPRule forEntity(String id) {
        return RULES.getOrDefault(id, DEFAULT);
    }
}