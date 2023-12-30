package net.krlite.rei_collapsible_entries.util;

import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface ModPredicate extends Predicate<EntryStack<?>> {
    static ModPredicate pass() {
        return entryStack -> true;
    }

    static ModPredicate fail() {
        return entryStack -> false;
    }

    static ModPredicate mod(ModEntry... modEntries) {
        return entryStack -> Arrays.stream(modEntries).anyMatch(modEntry -> modEntry.contains(entryStack.getIdentifier()));
    }

    static ModPredicate mod(String... namespaces) {
        return entryStack -> entryStack.getIdentifier() != null && Arrays.asList(namespaces).contains(entryStack.getIdentifier().getNamespace());
    }

    static ModPredicate mod(Identifier... namespaces) {
        return mod(Arrays.stream(namespaces).map(Identifier::getNamespace).toArray(String[]::new));
    }

    static ModPredicate id(Identifier identifier) {
        return entryStack -> entryStack.getIdentifier() != null && entryStack.getIdentifier().equals(identifier);
    }

    static ModPredicate path(String... paths) {
        return entryStack -> entryStack.getIdentifier() != null && entryStack.getIdentifier().getPath().equals(ModEntry.joinAll(paths));
    }

    static ModPredicate pathLeading(String... paths) {
        return entryStack -> entryStack.getIdentifier() != null
                && entryStack.getIdentifier().getPath().startsWith(ModEntry.joinAll(paths));
    }

    static ModPredicate pathLeading(Identifier path) {
        return pathLeading(path.getPath());
    }

    static ModPredicate idLeading(Identifier identifier) {
        return (ModPredicate) mod(identifier).and(pathLeading(identifier));
    }

    static ModPredicate pathLeadingOnly(String... paths) {
        return (ModPredicate) pathLeading(paths).and(path(paths).negate());
    }

    static ModPredicate pathLeadingOnly(Identifier path) {
        return pathLeadingOnly(path.getPath());
    }

    static ModPredicate idLeadingOnly(Identifier identifier) {
        return (ModPredicate) mod(identifier).and(pathLeadingOnly(identifier));
    }

    static ModPredicate pathTrailing(String... paths) {
        return entryStack -> entryStack.getIdentifier() != null
                && entryStack.getIdentifier().getPath().endsWith(ModEntry.joinAll(paths));
    }

    static ModPredicate pathTrailing(Identifier path) {
        return pathTrailing(path.getPath());
    }

    static ModPredicate idTrailing(Identifier identifier) {
        return (ModPredicate) mod(identifier).and(pathTrailing(identifier));
    }

    static ModPredicate pathTrailingOnly(String... paths) {
        return (ModPredicate) pathTrailing(paths).and(path(paths).negate());
    }

    static ModPredicate pathTrailingOnly(Identifier path) {
        return pathTrailingOnly(path.getPath());
    }

    static ModPredicate idTrailingOnly(Identifier identifier) {
        return (ModPredicate) mod(identifier).and(pathTrailingOnly(identifier));
    }

    static ModPredicate tag(String... paths) {
        return entryStack -> entryStack.getTagsFor().anyMatch(tag -> tag.id().getPath().equals(ModEntry.joinAll(paths)));
    }

    static ModPredicate tag(TagKey<?> tagKey) {
        return entryStack -> entryStack.getTagsFor().anyMatch(tagKey::equals);
    }

    static ModPredicate type(EntryType<?> entryType) {
        return entryStack -> entryStack.getType().equals(entryType);
    }
}
