package net.krlite.rei_collapsible_entries;

import me.shedaniel.rei.api.client.registry.entry.CollapsibleEntryRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.krlite.rei_collapsible_entries.client.listener.ClientCommandRegistryListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public class REICollapsibleEntries implements ClientModInitializer {
	public static final String NAME = "REI Collapsible Entries QOL", ID = "rei_collapsible_entries";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register(new ClientCommandRegistryListener());
	}

	enum ModEntry {
		C("c"),
		MC("minecraft"),
		AD_ASTRA("ad_astra"),
		AE2("ae2"),
		CATWALKS("catwalksinc"),
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


		public Text convertToTranslatableText(String prefix, Identifier identifier) {
			return Text.translatable(prefix + "." + identifier.getNamespace() + "." + String.join(".", identifier.getPath()));
		}

		/**
		 * Joins all the given sub strings into a single string by underscores, ignoring
		 * nulls and empties.
		 * <br />
		 * <code>["sub", null, "string"] -> ["sub_string"]</code>
		 *
		 * @param subs The sub strings to join.
		 * @return The joined string.
		 */
		public String joinAll(String... subs) {
			if (subs.length < 1)
				return "";
			return Arrays.stream(subs).filter(Objects::nonNull).filter(s -> !s.isEmpty()).reduce((f, s) -> f + "_" + s)
					.orElse(subs[0]);
		}

		/**
		 * Predicates all entries that are of the given {@link Identifier}.
		 *
		 * @param piece The identifier to predicate.
		 * @return The predicate result.
		 * @param <E> The entry type.
		 */
		public <E> Predicate<? extends EntryStack<E>> predicate(Identifier piece) {
			return entryStack -> entryStack.getIdentifier() != null && entryStack.getIdentifier().equals(piece);
		}

		/**
		 * Predicates all entries that start in the given {@link Identifier}.
		 *
		 * @param leading The identifier to predicate.
		 * @return The predicate result.
		 * @param <E> The entry type.
		 */
		public <E> Predicate<? extends EntryStack<E>> predicateLeading(Identifier leading) {
			return entryStack -> entryStack.getIdentifier() != null
					&& entryStack.getIdentifier().getNamespace().equals(leading.getNamespace())
					&& entryStack.getIdentifier().getPath().endsWith(leading.getPath());
		}

		/**
		 * Predicates all entries that end in the given {@link Identifier}.
		 *
		 * @param trailing The identifier to predicate.
		 * @return The predicate result.
		 * @param <E> The entry type.
		 */
		public <E> Predicate<? extends EntryStack<E>> predicateTrailing(Identifier trailing) {
			return entryStack -> entryStack.getIdentifier() != null
					&& entryStack.getIdentifier().getNamespace().equals(trailing.getNamespace())
					&& entryStack.getIdentifier().getPath().endsWith(trailing.getPath());
		}

		/**
		 * Gets a {@link Text} from the given {@link Identifier}, prefixed
		 * with <code>"tag"</code>.
		 *
		 * @param identifier The identifier.
		 * @return The tagged text.
		 */
		public Text tag(Identifier identifier) {
			return convertToTranslatableText("tag", identifier);
		}

		/**
		 * Gets a {@link Text} from the given {@link Identifier}, prefixed
		 * with <code>"col"</code>.
		 *
		 * @param identifier The identifier.
		 * @return The coled text.
		 */
		public Text col(Identifier identifier) {
			return convertToTranslatableText("col", identifier);
		}

		/**
		 * Registers a collapsible entry from the given {@link net.minecraft.registry.tag.TagKey}.
		 *
		 * @param registry The registry to register the entry to.
		 * @param modEntry The mod entry to register the entry for.
		 * @param tagPaths The tag's paths.
		 */
		public void registerCollapsibleEntryFromTag(
				CollapsibleEntryRegistry registry,
				REICollapsibleEntries.ModEntry modEntry, String... tagPaths
		) {
			registry.group(modEntry.id(tagPaths), tag(modEntry.id(tagPaths)),
					EntryIngredients.ofItemTag(modEntry.asItemTag(tagPaths)));
		}
	}
}
