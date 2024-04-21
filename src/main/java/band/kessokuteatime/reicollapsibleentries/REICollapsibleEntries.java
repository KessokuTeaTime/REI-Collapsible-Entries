package band.kessokuteatime.reicollapsibleentries;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import me.shedaniel.rei.RoughlyEnoughItemsCore;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import band.kessokuteatime.reicollapsibleentries.client.listener.ClientCommandRegistryListener;
import band.kessokuteatime.reicollapsibleentries.config.REICollapsibleEntriesConfig;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class REICollapsibleEntries implements ClientModInitializer {
	public static final String NAME = "REI Collapsible Entries", ID = "reicollapsibleentries";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static final ConfigHolder<REICollapsibleEntriesConfig> CONFIG;

	static {
		AutoConfig.register(REICollapsibleEntriesConfig.class, Toml4jConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(REICollapsibleEntriesConfig.class);
	}

	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register(new ClientCommandRegistryListener());

		CONFIG.registerSaveListener((configHolder, config) -> {
			RoughlyEnoughItemsCore._reloadPlugins(null);
			return ActionResult.PASS;
		});
	}

	public static MutableText paintIdentifier(@NotNull Identifier identifier) {
		return Text.translatable("tagged.#").formatted(Formatting.GRAY)
				.append(Text.literal(identifier.getNamespace()).formatted(Formatting.AQUA))
				.append(Text.literal(":").formatted(Formatting.GRAY))
				.append(Text.literal(identifier.getPath()).formatted(Formatting.YELLOW));
	}
}
