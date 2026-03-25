package games.polarbearbytes.mobxp.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import games.polarbearbytes.mobxp.config.MobXPStateManager;
import games.polarbearbytes.mobxp.data.MobXPData;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.Holder;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;

/**
 * Commands for changing the mob xp details
 */
public class MobXPCommands {
    private enum XPtype {
        PRIMARY,
        SECONDARY,
        BABY
    }

    private enum XPFlag {
        ENABLED,
        RANDOM,
        BABYUSESPRIMARY
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection commandSelection) {
        dispatcher.register(
                Commands.literal("mobxp")
                        .requires(source -> {
                            ServerPlayer entity = source.getPlayer();
                            PermissionSet perms = source.permissions();
                            return Commands.LEVEL_OWNERS.check(perms) || (entity != null && Permissions.check(entity, "mobxp.manageXP"));
                        })
                        .then(
                                Commands.argument("entity", ResourceArgument.resource(commandBuildContext, Registries.ENTITY_TYPE))
                                        .suggests(SuggestionProviders.cast(SuggestionProviders.SUMMONABLE_ENTITIES))
                                        .executes(
                                                context -> showDetails(
                                                        context.getSource(),
                                                        ResourceArgument.getSummonableEntityType(context, "entity")
                                                )
                                        )
                                        .then(
                                                Commands.literal("primaryXP")
                                                        .then(
                                                                Commands.argument("xp", IntegerArgumentType.integer())
                                                                        .executes(context->updateXP(
                                                                                    context.getSource(),
                                                                                    ResourceArgument.getSummonableEntityType(context, "entity"),
                                                                                    XPtype.PRIMARY,
                                                                                    IntegerArgumentType.getInteger(context, "xp")
                                                                                )
                                                                        )
                                                        )
                                        )
                                        .then(
                                                Commands.literal("secondaryXP")
                                                        .then(
                                                                Commands.argument("xp", IntegerArgumentType.integer())
                                                                        .executes(context->updateXP(
                                                                                    context.getSource(),
                                                                                    ResourceArgument.getSummonableEntityType(context, "entity"),
                                                                                    XPtype.SECONDARY,
                                                                                    IntegerArgumentType.getInteger(context, "xp")
                                                                                )
                                                                        )
                                                        )
                                        )
                                        .then(
                                                Commands.literal("babyXP")
                                                        .then(
                                                                Commands.argument("xp", IntegerArgumentType.integer())
                                                                        .executes(context->updateXP(
                                                                                        context.getSource(),
                                                                                        ResourceArgument.getSummonableEntityType(context, "entity"),
                                                                                        XPtype.BABY,
                                                                                        IntegerArgumentType.getInteger(context, "xp")
                                                                                )
                                                                        )
                                                        )
                                        )
                                        .then(
                                                Commands.literal("enabled")
                                                        .then(
                                                                Commands.argument("value", BoolArgumentType.bool())
                                                                        .executes(context->updateFlag(
                                                                                        context.getSource(),
                                                                                        ResourceArgument.getSummonableEntityType(context, "entity"),
                                                                                        XPFlag.ENABLED,
                                                                                        BoolArgumentType.getBool(context, "value")
                                                                                )
                                                                        )
                                                        )
                                        )
                                        .then(
                                                Commands.literal("random")
                                                        .then(
                                                                Commands.argument("value", BoolArgumentType.bool())
                                                                        .executes(context->updateFlag(
                                                                                        context.getSource(),
                                                                                        ResourceArgument.getSummonableEntityType(context, "entity"),
                                                                                        XPFlag.RANDOM,
                                                                                        BoolArgumentType.getBool(context, "value")
                                                                                )
                                                                        )
                                                        )
                                        )
                                        .then(
                                                Commands.literal("usePrimaryXPForBaby")
                                                        .then(
                                                                Commands.argument("value", BoolArgumentType.bool())
                                                                        .executes(context->updateFlag(
                                                                                        context.getSource(),
                                                                                        ResourceArgument.getSummonableEntityType(context, "entity"),
                                                                                        XPFlag.BABYUSESPRIMARY,
                                                                                        BoolArgumentType.getBool(context, "value")
                                                                                )
                                                                        )
                                                        )
                                        )
                        )
        );
    }

    private static int showDetails(CommandSourceStack source, Holder.Reference<EntityType<?>> entityType) throws CommandSyntaxException {
        MobXPData data = MobXPStateManager.get(source.getServer()).getMobData(entityType.getRegisteredName());

        String primaryXP = data.primaryXP() == -1 ? "default" : String.valueOf(data.primaryXP());
        String secondaryXP = data.primaryXP() == -1 ? "default" : String.valueOf(data.secondaryXP());
        String babyXP = data.primaryXP() == -1 ? "default" : String.valueOf(data.babyXP());

        source.sendSuccess(() -> Component.translatable("mobxp.commands.mobxp.details", data.getName(), primaryXP, secondaryXP, babyXP, data.enabled(), data.random(), data.usePrimaryXPForBaby()), false);
        return 1;
    }

    private static int updateXP(CommandSourceStack source, Holder.Reference<EntityType<?>> entityType, XPtype xptype, int xp) throws CommandSyntaxException {
        MobXPData oData = MobXPStateManager.get(source.getServer()).getMobData(entityType.getRegisteredName());

        MobXPData data = switch(xptype){
            case XPtype.PRIMARY -> new MobXPData(oData.id(),xp,oData.secondaryXP(),oData.babyXP(),oData.enabled(),oData.random(),oData.usePrimaryXPForBaby());
            case XPtype.SECONDARY -> new MobXPData(oData.id(),oData.primaryXP(),xp,oData.babyXP(),oData.enabled(),oData.random(),oData.usePrimaryXPForBaby());
            case XPtype.BABY -> new MobXPData(oData.id(),oData.primaryXP(),oData.secondaryXP(),xp,oData.enabled(),oData.random(),oData.usePrimaryXPForBaby());
        };

        MobXPStateManager.get(source.getServer()).updateState(data);
        source.sendSuccess(() -> Component.translatable("mobxp.commands.mobxp.xpupdate", data.getName(), xptype.toString(), xp), false);
        return 1;
    }

    private static int updateFlag(CommandSourceStack source, Holder.Reference<EntityType<?>> entityType, XPFlag xpflag, boolean enabled) throws CommandSyntaxException {
        MobXPData oData = MobXPStateManager.get(source.getServer()).getMobData(entityType.getRegisteredName());

        MobXPData data = switch(xpflag){
            case XPFlag.ENABLED -> new MobXPData(oData.id(),oData.primaryXP(),oData.secondaryXP(),oData.babyXP(),enabled,oData.random(),oData.usePrimaryXPForBaby());
            case XPFlag.RANDOM -> new MobXPData(oData.id(),oData.primaryXP(),oData.secondaryXP(),oData.babyXP(),oData.enabled(),enabled,oData.usePrimaryXPForBaby());
            case XPFlag.BABYUSESPRIMARY -> new MobXPData(oData.id(),oData.primaryXP(),oData.secondaryXP(),oData.babyXP(),oData.enabled(),oData.random(),enabled);
        };

        MobXPStateManager.get(source.getServer()).updateState(data);
        source.sendSuccess(() -> Component.translatable("mobxp.commands.mobxp.flagupdate", data.getName(), xpflag.toString(), enabled), false);
        return 1;
    }


}