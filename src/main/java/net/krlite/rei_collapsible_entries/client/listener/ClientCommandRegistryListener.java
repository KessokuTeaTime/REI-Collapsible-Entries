package net.krlite.rei_collapsible_entries.client.listener;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.krlite.rei_collapsible_entries.REICollapsibleEntries;
import net.krlite.rei_collapsible_entries.client.command.HeldItemTagsCommand;
import net.minecraft.command.CommandRegistryAccess;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ClientCommandRegistryListener implements ClientCommandRegistrationCallback {
    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(literal(REICollapsibleEntries.ID).then(literal("tags").executes(new HeldItemTagsCommand())));
    }
}
