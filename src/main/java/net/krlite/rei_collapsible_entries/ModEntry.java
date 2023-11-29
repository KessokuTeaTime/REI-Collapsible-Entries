package net.krlite.rei_collapsible_entries;

import me.shedaniel.rei.api.client.registry.entry.CollapsibleEntryRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public enum ModEntry {
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

    /**
     * Gets a {@link Text} from the given {@link Identifier}, prefixed
     * with <code>"tag"</code>.
     *
     * @param paths The identifier's paths.
     * @return The tagged text.
     */
    public Text tag(String... paths) {
        return convertToTranslatableText("tag", id(paths));
    }

    /**
     * Gets a {@link Text} from the given {@link Identifier}, prefixed
     * with <code>"col"</code>.
     *
     * @param paths The identifier's paths.
     * @return The coled text.
     */
    public Text col(String... paths) {
        return convertToTranslatableText("col", id(paths));
    }

    /**
     * Registers a collapsible entry from the given {@link TagKey}.
     *
     * @param registry The registry to register the entry to.
     * @param tagPaths The tag's paths.
     */
    public void registerCollapsibleEntryFromTag(
            CollapsibleEntryRegistry registry,
            String... tagPaths
    ) {
        registry.group(
                id(tagPaths),
                tag(tagPaths),
                EntryIngredients.ofItemTag(asItemTag(tagPaths))
        );
    }

    public static Text convertToTranslatableText(String prefix, Identifier identifier) {
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
    public static String joinAll(String... subs) {
        if (subs.length < 1)
            return "";
        return Arrays.stream(subs)
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .reduce((f, s) -> f + "_" + s)
                .orElse(subs[0]);
    }
}
