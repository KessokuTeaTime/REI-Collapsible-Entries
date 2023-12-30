package net.krlite.rei_collapsible_entries;

import me.shedaniel.rei.api.client.registry.entry.CollapsibleEntryRegistry;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.krlite.rei_collapsible_entries.util.ModPredicate;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;

import java.util.Arrays;

import static net.krlite.rei_collapsible_entries.util.ModEntry.*;

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

        types: {
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
            {/*
                // TODO: 2023/12/30
                Arrays.stream(new String[]{
                        "shulker_boxes", "ores", "dyes"
                }).forEach(tag -> registerCollapsibleEntryFromTag(registry, C, tag));*/

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

        minecraft: {
            // TODO: 2023/12/30
            // Tags
            Arrays.stream(new String[]{
                    "music_discs", "carpets", "banners", "candles", "beds",
                    "signs", "hanging_signs", "leaves", "logs", "planks",
                    "stairs", "slabs", "doors", "trapdoors", "fence_gates",
                    "boats", "walls", "fences", "trim_templates",
                    "decorated_pot_sherds", "swords", "shovels", "pickaxes",
                    "axes", "hoes", "small_flowers", "tall_flowers", "rails"
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

            // Potions
            Arrays.stream(new String[]{ null, "lingering", "splash" }).forEach(prefix ->
                    MC.buildCollection(prefix, "potions")
                            .predicate(ModPredicate.idTrailing(MC.id(joinAll(prefix, "potion"))))
                            .register(registry)
            );

            // Colored blocks
            Arrays.stream(new String[]{
                    "terracotta", "glazed_terracotta", "concrete",
                    "concrete_powder", "wool", "carpet"
            }).forEach(type -> MC.buildCollection("blocks", type)
                    .predicate(ModPredicate.pathDyeVariants(color -> joinAll(color, type)))
            );

            // Corals
            {
                final String[] TYPES = new String[]{ "tube", "brain", "bubble", "fire", "horn" };
                final String[] PREFIXES = new String[]{ null, "dead" };
                final String[] POSTFIXES = new String[]{ "coral", "coral_fan", "coral_block" };

                Arrays.stream(POSTFIXES).forEach(postfix ->
                        MC.buildCollection("blocks", postfix)
                                .predicate(ModPredicate.iterate(
                                        p -> ModPredicate.iterate(
                                                pp -> ModPredicate.path(joinAll(pp ,p)),
                                                PREFIXES
                                        ),
                                        TYPES
                                ))
                );
            }
/*
            // TODO: 2023/12/30
            // Skulls and heads
            {
                final String[] TYPES = new String[]{ "skull", "head" };

                registry.group(
                        MC.id("blocks", "skull_and_head"),
                        col(MC.id("blocks", "skull_and_head")),
                        entryStack -> MC.contains(entryStack.getIdentifier())
                                && Arrays.stream(TYPES)
                                .anyMatch(p -> entryStack.getIdentifier().getPath().endsWith(p))
                );
            }*/

            // Lights
            MC.buildCollection("blocks", "light")
                    .predicate(ModPredicate.idTrailing(Registries.BLOCK.getId(Blocks.LIGHT)))
                    .register(registry);
/*
            // TODO: 2023/12/30
            // Blocks
            Arrays.stream(new String[]{
                    "button", "pressure_plate", "copper", "sapling"
            }).forEach(type -> registry.group(
                    MC.id("blocks", type),
                    col(MC.id("blocks", type)),
                    predicateTrailing(MC.id(type))
            ));*/
        }

        // --- Ad Astra

        adAstra: {
            // Flags
            AD_ASTRA.buildCollection("flags")
                    .predicate(ModPredicate.idTrailing(AD_ASTRA.id("flag")))
                    .register(registry);

            // Globes
            AD_ASTRA.buildCollection("globes")
                    .predicate(ModPredicate.idTrailing(AD_ASTRA.id("globe")))
                    .register(registry);
        }

        // --- Applied Energetics 2

        ae2: {/*
            // TODO: 2023/12/30
            // Paint balls
            final String postfix = "paint_ball";
            Arrays.stream(new String[]{ null, "lumen" }).forEach(
                    type -> registry.group(
                            AE2.id(joinAll(type, "paint_balls")),
                            col(AE2.id(joinAll(type, "paint_balls"))),
                            EntryIngredients.ofItems(Arrays.stream(DYE_COLORS)
                                    .map(color -> AE2.item(joinAll(color, type, postfix)))
                                    .collect(Collectors.toList())))
            );*/
        }

        // --- Catwalks LLC.

        catwalksLLC: {
            // Paint rollers
            CATWALKS.buildTagged("filled_paint_rollers")
                    .predicate(ModPredicate.mod(CATWALKS)
                            .and(ModPredicate.tag(CATWALKS.itemTag("filled_paint_rollers"))
                                    .or(ModPredicate.pathTrailing("filled_paint_rollers"))))
                    .register(registry);
        }

        // --- Computer Craft

        computerCraft: {
            // Disks
            CC.buildCollection("disks")
                    .predicate(ModPredicate.id(CC.id("disk")))
                    .register(registry);
/*
            // TODO: 2023/12/30
            {
                final String[] POSTFIXES = { "advanced", "normal" };

                // Turtles and pocket computers
                Arrays.stream(new String[]{ "turtle", "pocket_computer" }).forEach(thing -> registry.group(
                        CC.id("things", thing),
                        col(CC.id("things", thing)),
                        entryStack -> CC.contains(entryStack.getIdentifier())
                                && Arrays.stream(POSTFIXES)
                                .map(p -> joinAll(thing, p))
                                .anyMatch(p -> entryStack.getIdentifier().getPath().equals(p))
                ));
            }*/
        }

        // --- Create

        create: {/*
            // TODO: 2023/12/30
            // Stone types
            Arrays.stream(new String[]{
                    "veridium", "scorchia", "scoria", "ochrum", "limestone",
                    "crimsite", "asurine", "tuff", "deepslate", "dripstone",
                    "calcite", "andesite", "diorite", "granite"
            }).forEach(type -> registry.group(
                    CREATE.id("stone_types", type),
                    tag(CREATE.id("stone_types", type)),
                    entryStack ->
                            CREATE.contains(entryStack.getIdentifier())
                                    && (entryStack.getTagsFor().anyMatch(tag -> tag.equals(CREATE.itemTag("stone_types", type)))
                                    || entryStack.getIdentifier().getPath().contains(type))
            ));*/
/*
            // TODO: 2023/12/30
            // Copper tiles & shingles
            Arrays.stream(new String[]{ "tile", "shingle" }).forEach(type -> registry.group(
                    CREATE.id("blocks", joinAll("copper", type)),
                    col(CREATE.id("blocks", joinAll("copper", type))),
                    entryStack ->
                            CREATE.contains(entryStack.getIdentifier())
                                    && entryStack.getIdentifier().getPath().contains(joinAll("copper", type))
            ));*/
/*
            // TODO: 2023/12/30
            // Toolboxes & seats
            Arrays.stream(new String[]{ "toolboxes", "seats" }).forEach(tag ->
                    CREATE.registerCollapsibleEntryFromTag(registry, tag)
            );*/
        }

        // --- Farmer's Delight

        farmersDelight: {
            // Canvas signs
            FARMERS_DELIGHT.registerCollapsibleEntryFromTag(registry, "canvas_signs");
        }

        // --- Hephaestus

        hephaestus: {
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
/*
            // TODO: 2023/12/30
            // Casts
            Arrays.stream(new String[]{"red_sand", "sand", "gold"}).forEach(cast ->
                    TC.registerCollapsibleEntryFromTag(registry, "casts", cast)
            );*/
/*
            // TODO: 2023/12/30
            // Tools
            Arrays.stream(new String[]{
                    "cleaver", "sword", "dagger", "scythe", "kama",
                    "broad_axe", "hand_axe", "excavator", "pickadze",
                    "mattock", "vein_hammer", "sledge_hammer", "pickaxe",
                    "crossbow", "longbow"
            }).forEach(tool -> registry.group(
                    TC.id("tools", tool),
                    col(TC.id("tools", tool)),
                    predicate(TC.id(tool))
            ));*/
/*
            // TODO: 2023/12/30
            // Parts
            Arrays.stream(new String[]{
                    "tough_handle", "tool_handle", "tool_binding",
                    "large_plate", "round_plate", "broad_blade",
                    "small_blade", "broad_axe_head", "small_axe_head",
                    "hammer_head", "pick_head", "repair_kit",
                    "bow_limb", "bow_grip", "bowstring"
            }).forEach(part -> registry.group(
                    TC.id("parts", part),
                    col(TC.id("parts", part)),
                    predicate(TC.id(part))
            ));*/

            // Anvils
            TC.buildCollection("anvils")
                    .predicate(ModPredicate.mod(TC)
                            .and(ModPredicate.path("scorched_anvil")
                                    .or(ModPredicate.path("tinkers_anvil"))))
                    .register(registry);
/*
            // TODO: 2023/12/30
            // Stations
            Arrays.stream(new String[]{ "part_builder", "tinker_station", "crafting_station" }).forEach(station -> registry.group(
                    TC.id("stations", station),
                    col(TC.id("stations", station)),
                    predicate(TC.id(station))
            ));*/
/*
            // TODO: 2023/12/30
            // Foundries & Smelteries
            Arrays.stream(new String[]{ "foundry", "smeltery" }).forEach(type -> registry.group(
                    TC.id("blocks", type),
                    tag(TC.id("blocks", type)),
                    EntryIngredients.ofItemTag(TC.itemTag(type))
            ));*/

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
/*
            // TODO: 2023/12/30
            // Slime grasses
            Arrays.stream(new String[]{ "ichor", "ender", "sky", "earth", "vanilla" }).forEach(type -> registry.group(
                    TC.id("slime_grasses", type),
                    col(TC.id("slime_grasses", type)),
                    entryStack ->
                            TC.contains(entryStack.getIdentifier())
                                    && entryStack.getIdentifier().getPath().endsWith(joinAll(type, "slime_grass"))
            ));*/
/*
            // TODO: 2023/12/30
            // Slime dirt & congealed slimes & slimes
            Arrays.stream(new String[]{ "slime_dirt", "congealed_slime", "slime" }).forEach(suffix -> registry.group(
                    TC.id("blocks", suffix),
                    col(TC.id("blocks", suffix)),
                    entryStack ->
                            TC.contains(entryStack.getIdentifier())
                                    && entryStack.getIdentifier().getPath().endsWith(suffix)
            ));*/
        }

        // --- Industrial Revolution

        // Modules
        INDREV.buildCollection("modules")
                .predicate(ModPredicate.idLeading(INDREV.id("module")))
                .register(registry);

        // --- Item Filters

        itemFilters: {
            // Filters
            ITEM_FILTERS.registerCollapsibleEntryFromTag(registry, "filters");
        }

        // --- Kibe

        kibe: {/*
            // TODO: 2023/12/30
            // Colorful blocks
            Arrays.stream(new String[]{ "sleeping_bag", "glider", "rune", "elevator" }).forEach(type -> registry.group(
                    KIBE.id("things", type),
                    col(KIBE.id("things", type)),
                    EntryIngredients.ofItems(Stream.concat(
                            Arrays.stream(DYE_COLORS).map(color -> KIBE.item(joinAll(color, type))),
                            Objects.equals(type, "glider") // The glider has right/left wings
                                    ? Stream.of(KIBE.item("glider_right_wing"), KIBE.item("glider_left_wing"))
                                    : Stream.empty()
                            )
                            .collect(Collectors.toList()))
            ));*/
        }

        // --- Promenade

        promenade: {
            // Piles
            PROMENADE.buildCollection("piles")
                            .predicate(ModPredicate.idTrailing(PROMENADE.id("pile")));
/*
            // TODO: 2023/12/30
            // Mushrooms & mushroom blocks
            Arrays.stream(new String[]{ null, "block" }).forEach(type -> registry.group(
                    PROMENADE.id("blocks", joinAll("mushroom", type)),
                    col(PROMENADE.id("blocks", joinAll("mushroom", type))),
                    entryStack ->
                            PROMENADE.contains(entryStack.getIdentifier())
                                    && entryStack.getIdentifier().getPath().contains(joinAll("mushroom", type))
            ));*/
        }
    }
}
