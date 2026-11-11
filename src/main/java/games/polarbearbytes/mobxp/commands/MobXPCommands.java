package games.polarbearbytes.mobxp.commands;

import com.mojang.brigadier.CommandDispatcher;
import games.polarbearbytes.mobxp.config.MobXPStateManager;
import games.polarbearbytes.mobxp.data.MobXPData;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandPermissionLevels;
import net.minecraft.commands.arguments.*;
import net.minecraft.commands.suggestions.SuggestSummonableEntities;
import net.minecraft.core.Holder;
import net.minecraft.server.permissions.*;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

/**
 * Commands for changing the mob xp details
 */
public class MobXPCommands {
    private enum XPtype {
        PRIMARY,
        SECONDARY,
        BABY,
        CUBE
    }

    private enum XPFlag {
        ENABLED,
        RANDOM,
        BABYUSESPRIMARY,
        CUBESUSESPRIMARY
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection commandSelection) {
        dispatcher.register(
                Commands.literal("mobxp")
                        .requires(Commands.hasPermission(CommandPermissionLevels.LEVEL_OWNERS))
                        /*
                        .requires(source -> {
                            ServerPlayer entity = source.getPlayer();
                            if(entity == null) return false;
                            return entity.permissions().hasPermission(new Permission.HasCommandLevel(LevelBasedPermissionSet.OWNER.level()));
                                    //|| Permissions.check(entity, "mobxp.manageXP");
                        })*/
                        .then(
                                Commands.argument("entity", ResourceArgument.resource(Registries.ENTITY_TYPE))
                                        .suggests(SuggestSummonableEntities.suggestSummonableEntities())
                                        .executes(
                                                context -> showDetails(
                                                        context.getSource(),
                                                        ResourceArgument.getSummonableEntityType(context, "entity")
                                                )
                                        )
                                        .then(
                                                Commands.literal("primaryXP")
                                                        .then(
                                                                Commands.argument("xp", IntegerArgument.integer())
                                                                        .executes(context->updateXP(
                                                                                    context.getSource(),
                                                                                    ResourceArgument.getSummonableEntityType(context, "entity"),
                                                                                    XPtype.PRIMARY,
                                                                                    IntegerArgument.getInteger(context, "xp")
                                                                                )
                                                                        )
                                                        )
                                        )
                                        .then(
                                                Commands.literal("secondaryXP")
                                                        .then(
                                                                Commands.argument("xp", IntegerArgument.integer())
                                                                        .executes(context->updateXP(
                                                                                    context.getSource(),
                                                                                    ResourceArgument.getSummonableEntityType(context, "entity"),
                                                                                    XPtype.SECONDARY,
                                                                                    IntegerArgument.getInteger(context, "xp")
                                                                                )
                                                                        )
                                                        )
                                        )
                                        .then(
                                                Commands.literal("babyXP")
                                                        .then(
                                                                Commands.argument("xp", IntegerArgument.integer())
                                                                        .executes(context->updateXP(
                                                                                        context.getSource(),
                                                                                        ResourceArgument.getSummonableEntityType(context, "entity"),
                                                                                        XPtype.BABY,
                                                                                        IntegerArgument.getInteger(context, "xp")
                                                                                )
                                                                        )
                                                        )
                                        )
                                        .then(
                                                Commands.literal("enabled")
                                                        .then(
                                                                Commands.argument("value", BooleanArgument.bool())
                                                                        .executes(context->updateFlag(
                                                                                        context.getSource(),
                                                                                        ResourceArgument.getSummonableEntityType(context, "entity"),
                                                                                        XPFlag.ENABLED,
                                                                                        BooleanArgument.getBool(context, "value")
                                                                                )
                                                                        )
                                                        )
                                        )
                                        .then(
                                                Commands.literal("random")
                                                        .then(
                                                                Commands.argument("value", BooleanArgument.bool())
                                                                        .executes(context->updateFlag(
                                                                                        context.getSource(),
                                                                                        ResourceArgument.getSummonableEntityType(context, "entity"),
                                                                                        XPFlag.RANDOM,
                                                                                        BooleanArgument.getBool(context, "value")
                                                                                )
                                                                        )
                                                        )
                                        )
                                        .then(
                                                Commands.literal("usePrimaryXPForBaby")
                                                        .then(
                                                                Commands.argument("value", BooleanArgument.bool())
                                                                        .executes(context->updateFlag(
                                                                                        context.getSource(),
                                                                                        ResourceArgument.getSummonableEntityType(context, "entity"),
                                                                                        XPFlag.BABYUSESPRIMARY,
                                                                                        BooleanArgument.getBool(context, "value")
                                                                                )
                                                                        )
                                                        )
                                        )
                        )
        );
    }

    private static int showDetails(CommandSourceStack source, Holder.Reference<EntityType<?>> entityType) {
        MobXPData data = MobXPStateManager.get(source.getServer()).getMobData(entityType.getRegisteredName());

        String primaryXP = data.primaryXP() == -1 ? "default" : String.valueOf(data.primaryXP());
        String secondaryXP = data.primaryXP() == -1 ? "default" : String.valueOf(data.secondaryXP());
        String babyXP = data.primaryXP() == -1 ? "default" : String.valueOf(data.babyXP());
        String largeCubeXP = data.primaryXP() == -1 ? "default" : String.valueOf(data.primaryXP());
        String mediumCubeXP = data.primaryXP() == -1 ? "default" : String.valueOf(data.mediumCubeXP());
        String smallCubeXP = data.primaryXP() == -1 ? "default" : String.valueOf(data.smallCubeXP());

        source.sendSuccess(() -> Component.translatable("mobxp.commands.mobxp.details", data.getName(), primaryXP, secondaryXP, babyXP, largeCubeXP, mediumCubeXP, smallCubeXP, data.enabled(), data.random(), data.usePrimaryXPForBaby(), data.usePrimaryXPForAllCubes()), false);
        return 1;
    }

    private static int updateXP(CommandSourceStack source, Holder.Reference<EntityType<?>> entityType, XPtype xptype, int xp) {
        MobXPData oData = MobXPStateManager.get(source.getServer()).getMobData(entityType.getRegisteredName());

        MobXPData data = switch(xptype){
            case XPtype.PRIMARY -> new MobXPData(oData.id(),xp,oData.secondaryXP(),oData.babyXP(),oData.mediumCubeXP(),oData.smallCubeXP(),oData.enabled(),oData.random(),oData.usePrimaryXPForBaby(),oData.usePrimaryXPForAllCubes());
            case XPtype.SECONDARY -> new MobXPData(oData.id(),oData.primaryXP(),xp,oData.babyXP(),oData.mediumCubeXP(),oData.smallCubeXP(),oData.enabled(),oData.random(),oData.usePrimaryXPForBaby(),oData.usePrimaryXPForAllCubes());
            case XPtype.BABY -> new MobXPData(oData.id(),oData.primaryXP(),oData.secondaryXP(),xp,oData.mediumCubeXP(),oData.smallCubeXP(),oData.enabled(),oData.random(),oData.usePrimaryXPForBaby(),oData.usePrimaryXPForAllCubes());
            case XPtype.CUBE -> new MobXPData(oData.id(),xp,oData.secondaryXP(),oData.babyXP(),xp,xp,oData.enabled(),oData.random(),oData.usePrimaryXPForBaby(),oData.usePrimaryXPForAllCubes());
        };

        MobXPStateManager.get(source.getServer()).updateState(data);
        source.sendSuccess(() -> Component.translatable("mobxp.commands.mobxp.xpupdate", data.getName(), xptype.toString(), xp), false);
        return 1;
    }

    private static int updateFlag(CommandSourceStack source, Holder.Reference<EntityType<?>> entityType, XPFlag xpflag, boolean enabled) {
        MobXPData oData = MobXPStateManager.get(source.getServer()).getMobData(entityType.getRegisteredName());

        MobXPData data = switch(xpflag){
            case XPFlag.ENABLED -> new MobXPData(oData.id(),oData.primaryXP(),oData.secondaryXP(),oData.babyXP(),oData.mediumCubeXP(),oData.smallCubeXP(),enabled,oData.random(),oData.usePrimaryXPForBaby(),oData.usePrimaryXPForAllCubes());
            case XPFlag.RANDOM -> new MobXPData(oData.id(),oData.primaryXP(),oData.secondaryXP(),oData.babyXP(),oData.mediumCubeXP(),oData.smallCubeXP(),oData.enabled(),enabled,oData.usePrimaryXPForBaby(),oData.usePrimaryXPForAllCubes());
            case XPFlag.BABYUSESPRIMARY -> new MobXPData(oData.id(),oData.primaryXP(),oData.secondaryXP(),oData.babyXP(),oData.mediumCubeXP(),oData.smallCubeXP(),oData.enabled(),oData.random(),enabled,oData.usePrimaryXPForAllCubes());
            case XPFlag.CUBESUSESPRIMARY -> new MobXPData(oData.id(),oData.primaryXP(),oData.secondaryXP(),oData.babyXP(),oData.mediumCubeXP(),oData.smallCubeXP(),oData.enabled(),oData.random(), oData.usePrimaryXPForBaby(),enabled);
        };

        MobXPStateManager.get(source.getServer()).updateState(data);
        source.sendSuccess(() -> Component.translatable("mobxp.commands.mobxp.flagupdate", data.getName(), xpflag.toString(), enabled), false);
        return 1;
    }


}