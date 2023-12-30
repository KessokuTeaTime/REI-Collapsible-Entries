package net.krlite.rei_collapsible_entries.util;

import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.Arrays;
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
}
