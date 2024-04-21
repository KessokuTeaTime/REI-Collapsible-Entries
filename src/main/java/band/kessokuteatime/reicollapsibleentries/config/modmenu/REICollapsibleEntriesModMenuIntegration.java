package band.kessokuteatime.reicollapsibleentries.config.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import band.kessokuteatime.reicollapsibleentries.config.REICollapsibleEntriesConfig;

public class REICollapsibleEntriesModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new REICollapsibleEntriesConfigScreen(parent).build();
    }
}
