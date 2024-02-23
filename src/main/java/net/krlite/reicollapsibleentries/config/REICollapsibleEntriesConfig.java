package net.krlite.reicollapsibleentries.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.ArrayList;

@Config(name = "reicollapsibleentries")
public class REICollapsibleEntriesConfig implements ConfigData {
    public ArrayList<String> customTags = new ArrayList<>();
}
