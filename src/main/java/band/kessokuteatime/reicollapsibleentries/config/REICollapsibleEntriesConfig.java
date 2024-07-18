package band.kessokuteatime.reicollapsibleentries.config;

import com.electronwill.nightconfig.core.serde.annotations.SerdeComment;
import com.electronwill.nightconfig.core.serde.annotations.SerdeDefault;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;
import java.util.function.Supplier;

@Config(name = "reicollapsibleentries")
@Config.Gui.Background(Config.Gui.Background.TRANSPARENT)
public class REICollapsibleEntriesConfig implements ConfigData {
    @ConfigEntry.Gui.Excluded
    private transient final Supplier<ArrayList<String>> customTagsProvider = ArrayList::new;

    @SerdeDefault(provider = "customTagsProvider")
    @SerdeComment("Define")
    public ArrayList<String> customTags = customTagsProvider.get();
}
