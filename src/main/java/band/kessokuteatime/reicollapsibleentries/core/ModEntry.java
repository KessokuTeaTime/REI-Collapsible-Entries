package band.kessokuteatime.reicollapsibleentries.core;

import me.shedaniel.rei.api.client.registry.entry.CollapsibleEntryRegistry;
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

    public Item item(String... paths) {
        return Registries.ITEM.get(id(paths));
    }

    public TagKey<Item> itemTag(String... paths) {
        return TagKey.of(RegistryKeys.ITEM, id(paths));
    }

    public ItemStack stack(int count, String... paths) {
        return new ItemStack(item(paths), count);
    }

    public ItemStack stack(String... paths) {
        return new ItemStack(item(paths), 1);
    }

    public boolean contains(@Nullable Identifier id) {
        return id != null && id.getNamespace().equals(modid());
    }

    public boolean contains(@NotNull Item item) {
        return contains(Registries.ITEM.getId(item));
    }

    /**
     * Gets a {@link Text} from the given {@link Identifier}, prefixed
     * with <code>"tag"</code>.
     *
     * @param paths The identifier's paths.
     * @return The tagged text.
     */
    public Text nameTagged(String... paths) {
        return convertToTranslatableText("tagged", id(paths));
    }

    /**
     * Gets a {@link Text} from the given {@link Identifier}, prefixed
     * with <code>"col"</code>.
     *
     * @param paths The identifier's paths.
     * @return The coled text.
     */
    public Text nameCollection(String... paths) {
        return convertToTranslatableText("collection", id(paths));
    }

    public ModPredicateBuilder build(Text name, String... paths) {
        return new ModPredicateBuilder(id(paths), name, ModPredicate.fail());
    }

    public ModPredicateBuilder buildTagged(String... paths) {
        return build(nameTagged(paths), paths);
    }

    public ModPredicateBuilder buildCollection(String... paths) {
        return build(nameCollection(paths), paths);
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
                nameTagged(tagPaths),
                EntryIngredients.ofItemTag(itemTag(tagPaths))
        );
    }

    public static Text convertToTranslatableText(String prefix, Identifier identifier) {
        return Text.translatable(prefix + "." + identifier.getNamespace() + "." + String.join(".", identifier.getPath()));
    }

    /**
     * Joins all the given paths into a single string by underscores, ignoring
     * nulls and empty strings.
     * <br />
     * <code>["sub", null, "string"] -> ["sub_string"]</code>
     *
     * @param paths The paths to join.
     * @return The joined string.
     */
    public static String joinAll(String... paths) {
        if (paths.length < 1)
            return "";
        return Arrays.stream(paths)
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .reduce((f, s) -> f + "_" + s)
                .orElse(paths[0]);
    }
}
