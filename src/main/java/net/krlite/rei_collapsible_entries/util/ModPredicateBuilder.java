package net.krlite.rei_collapsible_entries.util;

import me.shedaniel.rei.api.client.registry.entry.CollapsibleEntryRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;

public class ModPredicateBuilder {
    private final Identifier identifier;
    private final Text name;
    private final Predicate<EntryStack<?>> predicate;

    ModPredicateBuilder(Identifier identifier, Text name, Predicate<EntryStack<?>> predicate) {
        this.identifier = identifier;
        this.name = name;
        this.predicate = predicate;
    }

    public ModPredicateBuilder name(Text name) {
        return new ModPredicateBuilder(identifier, name, predicate);
    }

    public ModPredicateBuilder predicate(Predicate<EntryStack<?>> predicate) {
        return new ModPredicateBuilder(identifier, name, predicate);
    }

    public ModPredicateBuilder negate() {
        return predicate(this.predicate.negate());
    }

    public boolean test(EntryStack<?> entryStack) {
        return predicate.test(entryStack);
    }

    public void register(CollapsibleEntryRegistry registry) {
        registry.group(
                identifier,
                name,
                predicate
        );
    }
}
