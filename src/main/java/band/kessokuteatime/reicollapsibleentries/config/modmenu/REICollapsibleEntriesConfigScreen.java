package band.kessokuteatime.reicollapsibleentries.config.modmenu;

import band.kessokuteatime.reicollapsibleentries.core.ModEntry;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import band.kessokuteatime.reicollapsibleentries.REICollapsibleEntries;
import me.shedaniel.clothconfig2.gui.entries.StringListEntry;
import me.shedaniel.clothconfig2.impl.builders.AbstractFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.StringFieldBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;

public class REICollapsibleEntriesConfigScreen {
    private final ConfigBuilder configBuilder = ConfigBuilder.create()
            .setTitle(ModEntry.THIS.name("screen", "config", "title"))
            .transparentBackground()
            .setShouldListSmoothScroll(true)
            .setSavingRunnable(() -> {
                System.out.println(REICollapsibleEntries.CONFIG.get().customTags);
                REICollapsibleEntries.CONFIG.save();
                System.out.println(REICollapsibleEntries.CONFIG.get().customTags);
            });
    private final ConfigEntryBuilder entryBuilder = configBuilder.entryBuilder();

    public REICollapsibleEntriesConfigScreen(Screen parent) {
        configBuilder.setParentScreen(parent);
        REICollapsibleEntries.CONFIG.load();

        initEntries();
    }

    public Screen build() {
        return configBuilder.build();
    }

    private void initEntries() {
        ConfigCategory category = configBuilder.getOrCreateCategory(Text.empty());

        REICollapsibleEntries.CONFIG.get().customTags.forEach(tag -> {
            category.addEntry(listEntry(tag).build());
        });
    }

    private AbstractFieldBuilder<String, StringListEntry, StringFieldBuilder> listEntry(String value) {
        MutableText tag;
        @Nullable Identifier id = Identifier.tryParse(value);

        if (id == null) {
            tag = Text.translatable("tagged.#")
                    .append(value)
                    .formatted(Formatting.RED, Formatting.ITALIC);
        } else {
            tag = REICollapsibleEntries.paintIdentifier(id);
        }

        return entryBuilder.startStrField(tag, value)
                .setErrorSupplier(string -> {
                    return Optional.empty();
                })
                .setSaveConsumer(newValue -> {
                    REICollapsibleEntries.CONFIG.get().customTags.remove(value);
                    REICollapsibleEntries.CONFIG.get().customTags.add(newValue);
                });
    }
}
