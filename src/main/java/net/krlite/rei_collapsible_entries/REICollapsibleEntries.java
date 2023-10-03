package net.krlite.rei_collapsible_entries;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class REICollapsibleEntries implements ClientModInitializer {
	public static final String NAME = "REI Collapsible Entries QOL", ID = "rei_collapsible_entries";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	@Override
	public void onInitializeClient() {
	}

	enum ModEntry {
		C("c"),
		MC("minecraft"),
		AD_ASTRA("ad_astra"),
		AE2("ae2"),
		CATWALKS("catwalksllc"),
		CC("computercraft"),
		CREATE("create"),
		FARMERS_DELIGHT("farmersdelight"),
		TC("tconstruct"),
		INDREV("indrev"),
		ITEM_FILTERS("itemfilters"),
		KIBE("kibe"),
		PROMENADE("promenade");

		final String modid;

		ModEntry(String modid) {
			this.modid = modid;
		}

		public String modid() {
			return modid;
		}

		public Identifier id(String... path) {
			return new Identifier(modid(), String.join("/", path));
		}

		public Item asItem(String... paths) {
			return Registries.ITEM.get(id(paths));
		}

		public TagKey<Item> asItemTag(String... paths) {
			return TagKey.of(RegistryKeys.ITEM, id(paths));
		}

		public ItemStack asStack(int count, String... paths) {
			return new ItemStack(asItem(paths), count);
		}

		public ItemStack asStack(String... paths) {
			return new ItemStack(asItem(paths), 1);
		}

		public boolean checkContains(@Nullable Identifier id) {
			return id != null && id.getNamespace().equals(modid());
		}

		public boolean checkContains(@NotNull Item item) {
			return checkContains(Registries.ITEM.getId(item));
		}
	}
}
