package band.kessokuteatime.reicollapsibleentries.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.ArrayList;

@Config(name = "reicollapsibleentries")
@Config.Gui.Background(Config.Gui.Background.TRANSPARENT)
public class REICollapsibleEntriesConfig implements ConfigData {
    public ArrayList<String> customTags = new ArrayList<>();
}
