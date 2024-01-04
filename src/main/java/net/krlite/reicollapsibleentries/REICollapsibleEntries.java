package net.krlite.reicollapsibleentries;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.krlite.reicollapsibleentries.client.listener.ClientCommandRegistryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class REICollapsibleEntries implements ClientModInitializer {
	public static final String NAME = "REI Collapsible Entries QOL", ID = "reicollapsibleentries";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	public static final String[] DYE_COLORS = {
			"black", "red", "green", "brown", "blue", "purple",
			"cyan", "light_gray", "gray", "pink", "lime",
			"yellow", "light_blue", "magenta", "orange", "white"
	};

	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register(new ClientCommandRegistryListener());
	}
}
