package net.krlite.reicollapsibleentries;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import me.shedaniel.rei.RoughlyEnoughItemsCore;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.krlite.reicollapsibleentries.client.listener.ClientCommandRegistryListener;
import net.krlite.reicollapsibleentries.config.REICollapsibleEntriesConfig;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class REICollapsibleEntries implements ClientModInitializer {
	public static final String NAME = "REI Collapsible Entries QOL", ID = "reicollapsibleentries";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static final ConfigHolder<REICollapsibleEntriesConfig> CONFIG_HOLDER;
	public static final REICollapsibleEntriesConfig CONFIG;

	static {
		AutoConfig.register(REICollapsibleEntriesConfig.class, Toml4jConfigSerializer::new);
		CONFIG_HOLDER = AutoConfig.getConfigHolder(REICollapsibleEntriesConfig.class);
		CONFIG = CONFIG_HOLDER.get();
	}

	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register(new ClientCommandRegistryListener());
		CONFIG_HOLDER.registerSaveListener((configHolder, config) -> {
			RoughlyEnoughItemsCore._reloadPlugins(null);
			return ActionResult.PASS;
		});
	}

	public static MutableText paintIdentifier(Identifier identifier) {
		return
				Text.translatable("tagged.#").formatted(Formatting.YELLOW)
						.append(Text.literal(identifier.getNamespace()).formatted(Formatting.AQUA))
						.append(Text.literal(":").formatted(Formatting.GRAY))
						.append(Text.literal(identifier.getPath()).formatted(Formatting.YELLOW));
	}

	public static MutableText paintIdentifier(String identifierToParse) {
		return Optional.ofNullable(Identifier.tryParse(identifierToParse))
				.map(REICollapsibleEntries::paintIdentifier)
				.orElse(Text.translatable("tagged.none"));
	}
}
