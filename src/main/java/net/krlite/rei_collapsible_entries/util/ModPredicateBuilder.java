package net.krlite.rei_collapsible_entries.util;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModPredicateBuilder {
    private final Identifier identifier;
    private final Text name;
    private final ModPredicate predicate;

    ModPredicateBuilder(Identifier identifier, Text name, ModPredicate predicate) {
        this.identifier = identifier;
        this.name = name;
        this.predicate = predicate;
    }

    public ModPredicateBuilder name(Text name) {
        return new ModPredicateBuilder(identifier, name, predicate);
    }
}
