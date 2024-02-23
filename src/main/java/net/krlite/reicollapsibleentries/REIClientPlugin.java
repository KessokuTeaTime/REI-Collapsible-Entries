package net.krlite.reicollapsibleentries;

import me.shedaniel.rei.api.client.registry.entry.CollapsibleEntryRegistry;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.krlite.reicollapsibleentries.core.ModPredicate;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;

import java.util.Arrays;

import static net.krlite.reicollapsibleentries.core.ModEntry.*;

@SuppressWarnings("UnstableApiUsage")
public class REIClientPlugin implements me.shedaniel.rei.api.client.plugins.REIClientPlugin {
    private static final String[] DYE_COLORS = {
            "black", "red", "green", "brown", "blue", "purple",
            "cyan", "light_gray", "gray", "pink", "lime",
            "yellow", "light_blue", "magenta", "orange", "white"
    };

    @Override
    public void registerCollapsibleEntries(CollapsibleEntryRegistry registry) {
        REICollapsibleEntries.LOGGER.info("Registering quality-of-life collapsible entries for REI!");

        types:
        {
            // Fluids
            MC.buildCollection("fluids")
                    .predicate(ModPredicate.type(VanillaEntryTypes.FLUID))
                    .register(registry);

            // Spawn eggs
            MC.buildCollection("spawn_eggs")
                    .predicate(ModPredicate.pathTrailing("spawn_egg"))
                    .register(registry);
        }

        tags:
        {
            {
                // Ores
                Arrays.stream(new String[]{
                        "shulker_boxes", "ores", "dyes"
                }).forEach(tag -> C.registerCollapsibleEntryFromTag(registry, tag));

                // Glass blocks
                C.buildTagged("glass_blocks")
                        .predicate(ModPredicate.tag(C.itemTag("glass_blocks"))
                                .or(ModPredicate.type(VanillaEntryTypes.FLUID).negate()
                                        .and(ModPredicate.mod(TC)
                                                .or(ModPredicate.mod(AE2)))
                                        .and(ModPredicate.pathTrailing("glass")))) // Special case for glass in TC & AE2
                        .register(registry);

                // Glass panes
                C.buildTagged("glass_panes")
                        .predicate(ModPredicate.tag(C.itemTag("glass_panes"))
                                .or(ModPredicate.type(VanillaEntryTypes.FLUID).negate()
                                        .and(ModPredicate.mod(TC))
                                        .and(ModPredicate.pathTrailing("glass_pane")))) // Special case for glass panes in TC
                        .register(registry);
            }
        }

        // --- Minecraft

        minecraft:
        {
            // TODO: 2023/12/30
            // Tags
            Arrays.stream(new String[]{
                    "music_discs", "carpets", "banners", "candles", "beds",
                    "signs", "hanging_signs", "leaves", "logs", "planks",
                    "stairs", "slabs", "doors", "trapdoors", "fence_gates",
                    "boats", "walls", "fences", "trim_templates",
                    "decorated_pot_sherds", "swords", "shovels", "pickaxes",
                    "axes", "hoes", "small_flowers", "tall_flowers", "rails",
                    "saplings"
            }).forEach(tag -> MC.registerCollapsibleEntryFromTag(registry, tag));

            // Tools according to materials
            {
                final String[] MATERIALS = new String[]{"wooden", "stone", "golden", "iron", "diamond", "netherite"};
                final String[] TOOLS = new String[]{"sword", "shovel", "pickaxe", "axe", "hoe"};

                Arrays.stream(MATERIALS).forEach(material ->
                        MC.buildCollection("tools", material)
                                .predicate(ModPredicate.iterate(
                                        p -> ModPredicate.path(joinAll(material, p)),
                                        TOOLS
                                ))
                                .register(registry)
                );
            }

            // Armors according to materials & types
            {
                final String[] MATERIALS = new String[]{"leather", "chainmail", "iron", "diamond", "golden", "netherite"};
                final String[] ARMORS = new String[]{"helmet", "chestplate", "suit", "leggings", "pants", "boots"};

                Arrays.stream(MATERIALS).forEach(material ->
                        MC.buildCollection("armors", material)
                                .predicate(ModPredicate.iterate(
                                        p -> ModPredicate.path(joinAll(material, p)),
                                        ARMORS
                                ))
                                .register(registry)
                );

                Arrays.stream(ARMORS).forEach(type ->
                        MC.buildCollection("armor_types", type)
                                .predicate(ModPredicate.pathTrailing(type))
                                .register(registry)
                );
            }

            // Enchanted books
            MC.buildCollection("enchanted_books")
                    .predicate(ModPredicate.id(Registries.ITEM.getId(Items.ENCHANTED_BOOK)))
                    .register(registry);

            // Tipped arrows
            MC.buildCollection("tipped_arrows")
                    .predicate(ModPredicate.id(Registries.ITEM.getId(Items.TIPPED_ARROW)))
                    .register(registry);

            // Paintings
            MC.buildCollection("paintings")
                    .predicate(ModPredicate.id(Registries.ITEM.getId(Items.PAINTING)))
                    .register(registry);

            // Goat horns
            MC.buildCollection("goat_horns")
                    .predicate(ModPredicate.id(Registries.ITEM.getId(Items.GOAT_HORN)))
                    .register(registry);

            // Suspicious stews
            MC.buildCollection("suspicious_stews")
                    .predicate(ModPredicate.id(Registries.ITEM.getId(Items.SUSPICIOUS_STEW)))
                    .register(registry);

            // Banner patterns
            MC.buildCollection("banner_patterns")
                    .predicate(ModPredicate.pathTrailing("banner_pattern"))
                    .register(registry);

            // Horse armors
            MC.buildCollection("horse_armors")
                    .predicate(ModPredicate.pathTrailing("horse_armor"))
                    .register(registry);

            // Minecarts
            MC.buildCollection("minecarts")
                    .predicate(ModPredicate.idTrailing(Registries.ITEM.getId(Items.MINECART)))
                    .register(registry);

            // Potions
            Arrays.stream(new String[]{null, "lingering", "splash"}).forEach(prefix ->
                    MC.buildCollection(joinAll(prefix, "potions"))
                            .predicate(ModPredicate.idTrailing(MC.id(joinAll(prefix, "potion"))))
                            .register(registry)
            );

            // Colored blocks
            Arrays.stream(new String[]{
                    "terracotta", "glazed_terracotta", "concrete",
                    "concrete_powder", "wool", "carpet"
            }).forEach(type -> MC.buildCollection("blocks", type)
                    .predicate(ModPredicate.dyeVariants(dyeColor ->
                            ModPredicate.id(MC.id(joinAll(dyeColor.getName(), type)))
                    ))
                    .register(registry)
            );

            // Corals
            {
                final String[] TYPES = new String[]{"tube", "brain", "bubble", "fire", "horn"};
                final String[] PREFIXES = new String[]{null, "dead"};
                final String[] POSTFIXES = new String[]{"coral", "coral_fan", "coral_block"};

                Arrays.stream(POSTFIXES).forEach(postfix ->
                        MC.buildCollection("blocks", postfix)
                                .predicate(ModPredicate.iterate(
                                        p -> ModPredicate.iterate(
                                                pp -> ModPredicate.id(MC.id(joinAll(pp, p, postfix))),
                                                PREFIXES
                                        ),
                                        TYPES
                                ))
                                .register(registry)
                );
            }

            // Skulls and heads
            MC.buildCollection("blocks", "skull_and_head")
                    .predicate(ModPredicate.iterate(
                            ModPredicate::pathTrailing,
                            "skull", "head"
                    ))
                    .register(registry);

            // Lights
            MC.buildCollection("blocks", "light")
                    .predicate(ModPredicate.idTrailing(Registries.BLOCK.getId(Blocks.LIGHT)))
                    .register(registry);

            // ...
            Arrays.stream(new String[]{
                    "button", "pressure_plate", "copper"
            }).forEach(type -> MC.buildCollection("blocks", type)
                    .predicate(ModPredicate.pathTrailing(type))
                    .register(registry)
            );
        }

        // --- Ad Astra

        adAstra:
        {
            // Flags
            AD_ASTRA.buildCollection("flags")
                    .predicate(ModPredicate.idTrailing(AD_ASTRA.id("flag")))
                    .register(registry);

            // Globes
            AD_ASTRA.buildCollection("globes")
                    .predicate(ModPredicate.idTrailing(AD_ASTRA.id("globe")))
                    .register(registry);

            // Plates
            AD_ASTRA.buildCollection("plates")
                    .predicate(ModPredicate.idTrailing(AD_ASTRA.id("plate")))
                    .register(registry);

            // Materials
            Arrays.stream(new String[]{"iron", "steel", "desh", "ostrum", "calorite"}).forEach(material ->
                    AD_ASTRA.buildCollection("materials", material)
                            .predicate(ModPredicate.idContains(AD_ASTRA.id(material)))
                            .register(registry)
            );

            // Planet materials
            Arrays.stream(new String[]{"moon", "mars", "venus", "mercury", "glacio", "permafrost"}).forEach(planet ->
                    AD_ASTRA.buildCollection("planets", planet)
                            .predicate(ModPredicate.idContains(AD_ASTRA.id(planet)))
                            .register(registry)
            );
        }

        // --- Applied Energetics 2

        ae2:
        {
            // Paint balls
            final String postfix = "paint_ball";

            Arrays.stream(new String[]{null, "lumen"}).forEach(type ->
                    AE2.buildCollection(joinAll(type, "paint_balls"))
                            .predicate(ModPredicate.dyeVariants(dyeColor ->
                                    ModPredicate.id(AE2.id(joinAll(dyeColor.getName(), type, postfix)))
                            ))
                            .register(registry)
            );
        }

        // --- Catwalks LLC.

        catwalksLLC:
        {
            // Paint rollers
            CATWALKS.buildTagged("filled_paint_rollers")
                    .predicate(ModPredicate.mod(CATWALKS)
                            .and(ModPredicate.tag(CATWALKS.itemTag("filled_paint_rollers"))
                                    .or(ModPredicate.pathTrailing("filled_paint_rollers"))))
                    .register(registry);
        }

        // --- Computer Craft

        computerCraft:
        {
            // Disks
            CC.buildCollection("disks")
                    .predicate(ModPredicate.id(CC.id("disk")))
                    .register(registry);

            // Turtles and pocket computers
            Arrays.stream(new String[]{"turtle", "pocket_computer"}).forEach(thing ->
                    CC.buildCollection("things", thing)
                            .predicate(ModPredicate.iterate(
                                    p -> ModPredicate.id(CC.id(joinAll(thing, p))),
                                    "advanced", "normal"
                            ))
                            .register(registry)
            );
        }

        // --- Create

        create:
        {
            // Stone types
            Arrays.stream(new String[]{
                    "veridium", "scorchia", "scoria", "ochrum", "limestone",
                    "crimsite", "asurine", "tuff", "deepslate", "dripstone",
                    "calcite", "andesite", "diorite", "granite"
            }).forEach(type -> CREATE.buildTagged("stone_types", type)
                    .predicate(ModPredicate.mod(CREATE)
                            .and(ModPredicate.tag(CREATE.itemTag("stone_types", type))
                                    .or(ModPredicate.pathContains(type))))
                    .register(registry)
            );

            // Copper tiles & shingles
            Arrays.stream(new String[]{"tile", "shingle"}).forEach(type ->
                    CREATE.buildCollection("blocks", joinAll("copper", type))
                            .predicate(ModPredicate.idContains(CREATE.id(joinAll("copper", type))))
                            .register(registry)
            );

            // Toolboxes & seats
            Arrays.stream(new String[]{"toolboxes", "seats"}).forEach(tag ->
                    CREATE.registerCollapsibleEntryFromTag(registry, tag)
            );
        }

        // --- Farmer's Delight

        farmersDelight:
        {
            // Canvas signs
            FARMERS_DELIGHT.registerCollapsibleEntryFromTag(registry, "canvas_signs");
        }

        // --- Hephaestus

        hephaestus:
        {
            // Modifiers
            TC.buildCollection("modifiers")
                    .predicate(ModPredicate.mod(TC)
                            .and(ModPredicate.type(EntryType.deferred(TC.id("modifier_entry")))))
                    .register(registry);

            // Slime helmets
            TC.buildCollection("slime_helmets")
                    .predicate(ModPredicate.id(TC.id("slime_helmet")))
                    .register(registry);

            // Modifier Crystals
            TC.buildCollection("modifier_crystals")
                    .predicate(ModPredicate.id(TC.id("modifier_crystal")))
                    .register(registry);

            // Platforms
            TC.buildCollection("platforms")
                    .predicate(ModPredicate.idTrailing(TC.id("platform")))
                    .register(registry);

            // Casts
            Arrays.stream(new String[]{"red_sand", "sand", "gold"}).forEach(cast ->
                    TC.registerCollapsibleEntryFromTag(registry, "casts", cast)
            );

            // Tools
            Arrays.stream(new String[]{
                    "cleaver", "sword", "dagger", "scythe", "kama",
                    "broad_axe", "hand_axe", "excavator", "pickadze",
                    "mattock", "vein_hammer", "sledge_hammer", "pickaxe",
                    "crossbow", "longbow"
            }).forEach(tool -> TC.buildCollection("tools", tool)
                    .predicate(ModPredicate.id(TC.id(tool)))
                    .register(registry)
            );

            // Parts
            Arrays.stream(new String[]{
                    "tough_handle", "tool_handle", "tool_binding",
                    "large_plate", "round_plate", "broad_blade",
                    "small_blade", "broad_axe_head", "small_axe_head",
                    "hammer_head", "pick_head", "repair_kit",
                    "bow_limb", "bow_grip", "bowstring"
            }).forEach(part -> TC.buildCollection("parts", part)
                    .predicate(ModPredicate.id(TC.id(part)))
                    .register(registry)
            );

            // Anvils
            TC.buildCollection("anvils")
                    .predicate(ModPredicate.mod(TC)
                            .and(ModPredicate.path("scorched_anvil")
                                    .or(ModPredicate.path("tinkers_anvil"))))
                    .register(registry);

            // Stations
            Arrays.stream(new String[]{"part_builder", "tinker_station", "crafting_station"}).forEach(station ->
                    TC.buildCollection("stations", station)
                            .predicate(ModPredicate.id(TC.id(station)))
                            .register(registry)
            );

            // Foundries & Smelteries
            Arrays.stream(new String[]{"foundry", "smeltery"}).forEach(type ->
                    TC.buildTagged("blocks", type)
                            .predicate(ModPredicate.tag(TC.itemTag(type)))
                            .register(registry)
            );

            // Buckets
            MC.buildCollection("buckets")
                    .predicate(ModPredicate.mod(MC, TC, CREATE, INDREV, AD_ASTRA, KIBE)
                            .and(ModPredicate.pathTrailingOnly("bucket"))
                            .and(ModPredicate.pathTrailing("potion_bucket").negate()))
                    .register(registry);

            // Potion buckets
            TC.buildCollection("buckets", "potion")
                    .predicate(ModPredicate.id(TC.id("potion_bucket")))
                    .register(registry);

            // Slime grasses
            Arrays.stream(new String[]{"ichor", "ender", "sky", "earth", "vanilla"}).forEach(type ->
                    TC.buildCollection("slime_grasses", type)
                            .predicate(ModPredicate.idTrailing(TC.id(type, "slime_grass")))
                            .register(registry)
            );

            // Slime dirt & congealed slimes & slimes
            Arrays.stream(new String[]{"slime_dirt", "congealed_slime", "slime"}).forEach(suffix ->
                    TC.buildCollection("blocks", suffix)
                            .predicate(ModPredicate.idTrailing(TC.id(suffix)))
            );
        }

        // --- Industrial Revolution

        // Modules
        INDREV.buildCollection("modules")
                .predicate(ModPredicate.idLeading(INDREV.id("module")))
                .register(registry);

        // --- Item Filters

        itemFilters:
        {
            // Filters
            ITEM_FILTERS.registerCollapsibleEntryFromTag(registry, "filters");
        }

        // --- Kibe

        kibe:
        {
            // Colored blocks
            Arrays.stream(new String[]{"sleeping_bag", "glider", "rune", "elevator"}).forEach(thing ->
                    KIBE.buildCollection("things", thing)
                            .predicate(ModPredicate.dyeVariants(
                                    dyeColor -> ModPredicate.id(KIBE.id(joinAll(dyeColor.getName(), thing)))
                                            .or(thing.equals("glider")
                                                    ? ModPredicate.idLeading(KIBE.id(thing)) // Glider's parts
                                                    : ModPredicate.fail()
                                            ))
                            )
                            .register(registry)
            );

            // Kibes
            KIBE.buildCollection("kibes")
                    .predicate(ModPredicate.idTrailing(KIBE.id("kibe")))
                    .register(registry);

            // Kibes
            KIBE.buildCollection("rings")
                    .predicate(ModPredicate.idTrailing(KIBE.id("ring")))
                    .register(registry);

            // Tanks
            KIBE.buildCollection("tanks")
                    .predicate(ModPredicate.id(KIBE.id("tank")))
                    .register(registry);

            // Spikes and belts
            Arrays.stream(new String[]{"spikes", "belt"}).forEach(type ->
                    KIBE.buildCollection("blocks", type)
                            .predicate(ModPredicate.idTrailing(KIBE.id(type)))
                            .register(registry)
            );

            // Stone generators
            Arrays.stream(new String[]{"cobblestone", "basalt"}).forEach(type ->
                    KIBE.buildCollection(joinAll(type, "generators"))
                            .predicate(ModPredicate.idLeading(KIBE.id(joinAll(type, "generator"))))
                            .register(registry)
            );
        }

        // --- Promenade

        promenade:
        {
            // Piles
            PROMENADE.buildCollection("piles")
                    .predicate(ModPredicate.idTrailing(PROMENADE.id("pile")));

            // Mushrooms & mushroom blocks
            Arrays.stream(new String[]{null, "block"}).forEach(type ->
                    PROMENADE.buildCollection("blocks", joinAll("mushroom", type))
                            .predicate(ModPredicate.idContains(PROMENADE.id(joinAll("mushroom", type))))
                            .register(registry)
            );
        }
    }
}
