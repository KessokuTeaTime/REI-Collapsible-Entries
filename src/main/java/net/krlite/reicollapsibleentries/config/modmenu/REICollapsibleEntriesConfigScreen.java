package net.krlite.reicollapsibleentries.config.modmenu;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.krlite.reicollapsibleentries.REICollapsibleEntries;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class REICollapsibleEntriesConfigScreen {
    private final ConfigBuilder configBuilder = ConfigBuilder.create()
            .setTitle(Text.translatable("screen.reicollapsibleentries.config.title"))
            .transparentBackground()
            .setShouldListSmoothScroll(true)
            .setSavingRunnable(REICollapsibleEntries.CONFIG::save);
    private final ConfigEntryBuilder entryBuilder = configBuilder.entryBuilder();

    public REICollapsibleEntriesConfigScreen(Screen parent) {
        configBuilder.setParentScreen(parent);

        initEntries();
    }

    public Screen build() {
        return configBuilder.build();
    }

    private void initEntries() {
        ConfigCategory category = configBuilder.getOrCreateCategory(Text.empty());

        category.addEntry(entryBuilder.startStrList(
                                Text.translatable("config.reicollapsibleentries.custom_tags"),
                                REICollapsibleEntries.CONFIG.get().customTags
                        )
                        .setExpanded(true)
                        .setSaveConsumer(value -> REICollapsibleEntries.CONFIG.get().customTags = new ArrayList<>(value))
                        .build()
        );
    }
}
