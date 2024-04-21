package band.kessokuteatime.reicollapsibleentries.core;

import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public interface ModPredicate extends Predicate<EntryStack<?>> {
    static Predicate<EntryStack<?>> pass() {
        return entryStack -> true;
    }

    static Predicate<EntryStack<?>> fail() {
        return entryStack -> false;
    }



    static Predicate<EntryStack<?>> mod(ModEntry... modEntries) {
        return entryStack -> Arrays.stream(modEntries).anyMatch(modEntry -> modEntry.contains(entryStack.getIdentifier()));
    }

    static Predicate<EntryStack<?>> mod(String... namespaces) {
        return entryStack -> entryStack.getIdentifier() != null && Arrays.asList(namespaces).contains(entryStack.getIdentifier().getNamespace());
    }

    static Predicate<EntryStack<?>> mod(Identifier... namespaces) {
        return mod(Arrays.stream(namespaces).map(Identifier::getNamespace).toArray(String[]::new));
    }



    static Predicate<EntryStack<?>> id(Identifier identifier) {
        return entryStack -> entryStack.getIdentifier() != null && entryStack.getIdentifier().equals(identifier);
    }

    static Predicate<EntryStack<?>> path(String... paths) {
        return entryStack -> entryStack.getIdentifier() != null && entryStack.getIdentifier().getPath().equals(ModEntry.joinAll(paths));
    }



    static Predicate<EntryStack<?>> pathContains(String... paths) {
        return entryStack -> entryStack.getIdentifier() != null
                && entryStack.getIdentifier().getPath().contains(ModEntry.joinAll(paths));
    }

    static Predicate<EntryStack<?>> pathContains(Identifier path) {
        return pathContains(path.getPath());
    }

    static Predicate<EntryStack<?>> idContains(Identifier identifier) {
        return mod(identifier).and(pathContains(identifier));
    }

    static Predicate<EntryStack<?>> pathContainsOnly(String... paths) {
        return pathContains(paths).and(path(paths).negate());
    }

    static Predicate<EntryStack<?>> pathContainsOnly(Identifier path) {
        return pathContainsOnly(path.getPath());
    }

    static Predicate<EntryStack<?>> idContainsOnly(Identifier identifier) {
        return mod(identifier).and(pathContainsOnly(identifier));
    }



    static Predicate<EntryStack<?>> pathLeading(String... paths) {
        return entryStack -> entryStack.getIdentifier() != null
                && entryStack.getIdentifier().getPath().startsWith(ModEntry.joinAll(paths));
    }

    static Predicate<EntryStack<?>> pathLeading(Identifier path) {
        return pathLeading(path.getPath());
    }

    static Predicate<EntryStack<?>> idLeading(Identifier identifier) {
        return mod(identifier).and(pathLeading(identifier));
    }

    static Predicate<EntryStack<?>> pathLeadingOnly(String... paths) {
        return pathLeading(paths).and(path(paths).negate());
    }

    static Predicate<EntryStack<?>> pathLeadingOnly(Identifier path) {
        return pathLeadingOnly(path.getPath());
    }

    static Predicate<EntryStack<?>> idLeadingOnly(Identifier identifier) {
        return mod(identifier).and(pathLeadingOnly(identifier));
    }



    static Predicate<EntryStack<?>> pathTrailing(String... paths) {
        return entryStack -> entryStack.getIdentifier() != null
                && entryStack.getIdentifier().getPath().endsWith(ModEntry.joinAll(paths));
    }

    static Predicate<EntryStack<?>> pathTrailing(Identifier path) {
        return pathTrailing(path.getPath());
    }

    static Predicate<EntryStack<?>> idTrailing(Identifier identifier) {
        return mod(identifier).and(pathTrailing(identifier));
    }

    static Predicate<EntryStack<?>> pathTrailingOnly(String... paths) {
        return pathTrailing(paths).and(path(paths).negate());
    }

    static Predicate<EntryStack<?>> pathTrailingOnly(Identifier path) {
        return pathTrailingOnly(path.getPath());
    }

    static Predicate<EntryStack<?>> idTrailingOnly(Identifier identifier) {
        return mod(identifier).and(pathTrailingOnly(identifier));
    }



    static Predicate<EntryStack<?>> tag(String... paths) {
        return entryStack -> entryStack.getTagsFor().anyMatch(tag -> tag.id().getPath().equals(ModEntry.joinAll(paths)));
    }

    static Predicate<EntryStack<?>> tag(TagKey<?> tagKey) {
        return entryStack -> entryStack.getTagsFor().anyMatch(tagKey::equals);
    }



    static Predicate<EntryStack<?>> type(EntryType<?> entryType) {
        return entryStack -> entryStack.getType().equals(entryType);
    }



    static Predicate<EntryStack<?>> iterate(
            Function<String, Predicate<EntryStack<?>>> pathPredication,
            String... array
    ) {
        return entryStack -> Arrays.stream(array).anyMatch(path -> pathPredication.apply(path).test(entryStack));
    }

    static Predicate<EntryStack<?>> dyeVariants(Function<DyeColor, Predicate<EntryStack<?>>> dyeColorPredication) {
        return entryStack -> Arrays.stream(DyeColor.values())
                .anyMatch(dyeColor -> dyeColorPredication.apply(dyeColor).test(entryStack));
    }
}
