package net.krlite.rei_collapsible_entries.util;

import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.EntryType;

import java.util.function.Predicate;

public interface ModPredicate extends Predicate<EntryStack<?>> {
    static ModPredicate pass() {
        return entryStack -> true;
    }

    static ModPredicate fail() {
        return entryStack -> false;
    }

    static ModPredicate full(String... paths) {
        return entryStack -> entryStack.getIdentifier() != null && entryStack.getIdentifier().getPath().equals(ModEntry.joinAll(paths));
    }

    static ModPredicate leading(String... paths) {
        return entryStack -> entryStack.getIdentifier() != null
                && entryStack.getIdentifier().getPath().startsWith(ModEntry.joinAll(paths));
    }

    static ModPredicate leadingOnly(String... paths) {
        return (ModPredicate) leading(paths).and(full(paths).negate());
    }

    static ModPredicate trailing(String... paths) {
        return entryStack -> entryStack.getIdentifier() != null
                && entryStack.getIdentifier().getPath().endsWith(ModEntry.joinAll(paths));
    }

    static ModPredicate trailingOnly(String... paths) {
        return (ModPredicate) trailing(paths).and(full(paths).negate());
    }

    static ModPredicate tag(String... paths) {
        return entryStack -> entryStack.getTagsFor().anyMatch(tag -> tag.id().getPath().equals(ModEntry.joinAll(paths)));
    }

    static ModPredicate mod(ModEntry modEntry) {
        return entryStack -> modEntry.checkContains(entryStack.getIdentifier());
    }

    static ModPredicate type(EntryType<?> entryType) {
        return entryStack -> entryStack.getType().equals(entryType);
    }
}
