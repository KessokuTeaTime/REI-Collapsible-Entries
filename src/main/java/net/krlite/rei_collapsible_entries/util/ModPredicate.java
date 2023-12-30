package net.krlite.rei_collapsible_entries.util;

import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.EntryType;

import java.util.function.Predicate;

public interface ModPredicate extends Predicate<EntryStack<?>> {
    default ModPredicate full(String... paths) {
        return entryStack -> entryStack.getIdentifier() != null && entryStack.getIdentifier().getPath().equals(ModEntry.joinAll(paths));
    }

    default ModPredicate leading(String... paths) {
        return entryStack -> entryStack.getIdentifier() != null
                && entryStack.getIdentifier().getPath().startsWith(ModEntry.joinAll(paths));
    }

    default ModPredicate trailing(String... paths) {
        return entryStack -> entryStack.getIdentifier() != null
                && entryStack.getIdentifier().getPath().endsWith(ModEntry.joinAll(paths));
    }

    default ModPredicate tag(String... paths) {
        return entryStack -> entryStack.getTagsFor().anyMatch(tag -> tag.id().getPath().equals(ModEntry.joinAll(paths)));
    }

    default ModPredicate mod(ModEntry modEntry) {
        return entryStack -> modEntry.checkContains(entryStack.getIdentifier());
    }

    default ModPredicate type(EntryType<?> entryType) {
        return entryStack -> entryStack.getType().equals(entryType);
    }
}
