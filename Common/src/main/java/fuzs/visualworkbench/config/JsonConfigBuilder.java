package fuzs.visualworkbench.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import fuzs.puzzleslib.json.JsonConfigFileUtil;
import fuzs.puzzleslib.json.JsonSerializationUtil;
import fuzs.visualworkbench.VisualWorkbench;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CraftingTableBlock;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonConfigBuilder {
    public static final JsonConfigBuilder INSTANCE = new JsonConfigBuilder();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .create();
    private static final String JSON_CONFIG_NAME = "visual_workbench.json";
    private static final List<ResourceLocation> DEFAULT_VISUAL_WORKBENCHES = Stream.of(
                    // Minecraft
                    "minecraft:crafting_table",
                    // Blue Skies
                    "blue_skies:bluebright_crafting_table",
                    "blue_skies:starlit_crafting_table",
                    "blue_skies:frostbright_crafting_table",
                    "blue_skies:lunar_crafting_table",
                    "blue_skies:dusk_crafting_table",
                    "blue_skies:maple_crafting_table",
                    "blue_skies:cherry_crafting_table",
                    // Blocks +
                    "blocksplus:spruce_crafting_table",
                    "blocksplus:birch_crafting_table",
                    "blocksplus:jungle_crafting_table",
                    "blocksplus:acacia_crafting_table",
                    "blocksplus:dark_oak_crafting_table",
                    "blocksplus:crimson_crafting_table",
                    "blocksplus:warped_crafting_table",
                    "blocksplus:bamboo_crafting_table",
                    "blocksplus:mushroom_crafting_table",
                    // More Crafting Tables for Forge!
                    "mctb:spruce_crafting_table",
                    "mctb:birch_crafting_table",
                    "mctb:acacia_crafting_table",
                    "mctb:jungle_crafting_table",
                    "mctb:dark_oak_crafting_table",
                    "mctb:warped_crafting_table",
                    "mctb:crimson_crafting_table",
                    "mctb:cherry_crafting_table",
                    "mctb:dead_crafting_table",
                    "mctb:fir_crafting_table",
                    "mctb:hellbark_crafting_table",
                    "mctb:jacaranda_crafting_table",
                    "mctb:magic_crafting_table",
                    "mctb:mahogany_crafting_table",
                    "mctb:palm_crafting_table",
                    "mctb:redwood_crafting_table",
                    "mctb:umbran_crafting_table",
                    "mctb:willow_crafting_table",
                    "mctb:azalea_crafting_table",
                    "mctb:blossom_crafting_table",
                    // BetterNether
                    "betternether:rubeus_crafting_table",
                    "betternether:nether_sakura_crafting_table",
                    "betternether:crafting_table_crimson",
                    "betternether:wart_crafting_table",
                    "betternether:crafting_table_warped",
                    "betternether:anchor_tree_crafting_table",
                    "betternether:willow_crafting_table",
                    "betternether:nether_mushroom_crafting_table",
                    "betternether:stalagnate_crafting_table",
                    "betternether:mushroom_fir_crafting_table",
                    "betternether:nether_reed_crafting_table",
                    // BetterEnd
                    "betterend:helix_tree_crafting_table",
                    "betterend:mossy_glowshroom_crafting_table",
                    "betterend:end_lotus_crafting_table",
                    "betterend:pythadendron_crafting_table",
                    "betterend:jellyshroom_crafting_table",
                    "betterend:tenanea_crafting_table",
                    "betterend:dragon_tree_crafting_table",
                    "betterend:lucernia_crafting_table",
                    "betterend:lacugrove_crafting_table",
                    "betterend:umbrella_tree_crafting_table",
                    // BetterEnd Reforked
                    "betterendforge:helix_tree_crafting_table",
                    "betterendforge:mossy_glowshroom_crafting_table",
                    "betterendforge:end_lotus_crafting_table",
                    "betterendforge:pythadendron_crafting_table",
                    "betterendforge:jellyshroom_crafting_table",
                    "betterendforge:tenanea_crafting_table",
                    "betterendforge:dragon_tree_crafting_table",
                    "betterendforge:lucernia_crafting_table",
                    "betterendforge:lacugrove_crafting_table",
                    "betterendforge:umbrella_tree_crafting_table",
                    // Crumbs
                    "crumbs:spruce_crafting_table",
                    "crumbs:birch_crafting_table",
                    "crumbs:jungle_crafting_table",
                    "crumbs:acacia_crafting_table",
                    "crumbs:dark_oak_crafting_table",
                    "crumbs:crimson_crafting_table",
                    "crumbs:warped_crafting_table",
                    // Oh The Biomes You'll Go
                    "byg:aspen_crafting_table",
                    "byg:baobab_crafting_table",
                    "byg:blue_enchanted_crafting_table",
                    "byg:cherry_crafting_table",
                    "byg:cika_crafting_table",
                    "byg:cypress_crafting_table",
                    "byg:ebony_crafting_table",
                    "byg:fir_crafting_table",
                    "byg:green_enchanted_crafting_table",
                    "byg:holly_crafting_table",
                    "byg:jacaranda_crafting_table",
                    "byg:mahogany_crafting_table",
                    "byg:mangrove_crafting_table",
                    "byg:maple_crafting_table",
                    "byg:pine_crafting_table",
                    "byg:rainbow_eucalyptus_crafting_table",
                    "byg:redwood_crafting_table",
                    "byg:skyris_crafting_table",
                    "byg:willow_crafting_table",
                    "byg:witch_hazel_crafting_table",
                    "byg:zelkova_crafting_table",
                    "byg:sythian_crafting_table",
                    "byg:embur_crafting_table",
                    "byg:palm_crafting_table",
                    "byg:lament_crafting_table",
                    "byg:bulbis_crafting_table",
                    "byg:nightshade_crafting_table",
                    "byg:ether_crafting_table",
                    "byg:imparius_crafting_table",
                    // Variant Crafting Tables [Forge]
                    "vct:spruce_crafting_table",
                    "vct:birch_crafting_table",
                    "vct:jungle_crafting_table",
                    "vct:acacia_crafting_table",
                    "vct:dark_oak_crafting_table",
                    "vct:mangrove_crafting_table",
                    "vct:crimson_crafting_table",
                    "vct:warped_crafting_table",
                    "vct:bop_cherry_crafting_table",
                    "vct:bop_dead_crafting_table",
                    "vct:bop_fir_crafting_table",
                    "vct:bop_hellbark_crafting_table",
                    "vct:bop_jacaranda_crafting_table",
                    "vct:bop_magic_crafting_table",
                    "vct:bop_mahogany_crafting_table",
                    "vct:bop_palm_crafting_table",
                    "vct:bop_redwood_crafting_table",
                    "vct:bop_umbran_crafting_table",
                    "vct:bop_willow_crafting_table",
                    "vct:canopy_crafting_table",
                    "vct:darkwood_crafting_table",
                    "vct:twilight_mangrove_crafting_table",
                    "vct:minewood_crafting_table",
                    "vct:sortingwood_crafting_table",
                    "vct:timewood_crafting_table",
                    "vct:transwood_crafting_table",
                    "vct:twilight_oak_crafting_table",
                    "vct:aspen_crafting_table",
                    "vct:grimwood_crafting_table",
                    "vct:kousa_crafting_table",
                    "vct:morado_crafting_table",
                    "vct:rosewood_crafting_table",
                    "vct:yucca_crafting_table",
                    "vct:maple_crafting_table",
                    "vct:bamboo_crafting_table",
                    "vct:azalea_crafting_table",
                    "vct:poise_crafting_table",
                    "vct:cherry_crafting_table",
                    "vct:willow_crafting_table",
                    "vct:wisteria_crafting_table",
                    "vct:driftwood_crafting_table",
                    "vct:river_crafting_table",
                    "vct:jacaranda_crafting_table",
                    "vct:redbud_crafting_table",
                    "vct:cypress_crafting_table",
                    "vct:brown_mushroom_crafting_table",
                    "vct:red_mushroom_crafting_table",
                    "vct:glowshroom_crafting_table",
                    "vct:twisted_crafting_table",
                    "vct:petrified_crafting_table",
                    "vct:eco_azalea_crafting_table",
                    "vct:eco_flowering_azalea_crafting_table",
                    "vct:eco_coconut_crafting_table",
                    "vct:eco_walnut_crafting_table",
                    "vct:fairy_ring_mushroom_crafting_table",
                    "vct:azure_crafting_table",
                    "vct:araucaria_crafting_table",
                    "vct:heidiphyllum_crafting_table",
                    "vct:liriodendrites_crafting_table",
                    "vct:metasequoia_crafting_table",
                    "vct:protojuniperoxylon_crafting_table",
                    "vct:protopiceoxylon_crafting_table",
                    "vct:zamites_crafting_table",
                    "vct:quark_azalea_crafting_table",
                    "vct:quark_blossom_crafting_table",
                    "vct:grongle_crafting_table",
                    "vct:smogstem_crafting_table",
                    "vct:wigglewood_crafting_table",
                    // Variant Crafting Tables [Fabric]
                    "variantcraftingtables:acacia_crafting_table",
                    "variantcraftingtables:birch_crafting_table",
                    "variantcraftingtables:dark_oak_crafting_table",
                    "variantcraftingtables:jungle_crafting_table",
                    "variantcraftingtables:spruce_crafting_table",
                    "variantcraftingtables:mangrove_crafting_table",
                    "variantcraftingtables:crimson_crafting_table",
                    "variantcraftingtables:warped_crafting_table",
                    "variantcraftingtables:rubber_crafting_table",
                    "variantcraftingtables:bamboo_crafting_table",
                    "variantcraftingtables:charred_crafting_table",
                    "variantcraftingtables:legacy_crafting_table",
                    "variantcraftingtables:white_oak_crafting_table",
                    "variantcraftingtables:herringbone_acacia_crafting_table",
                    "variantcraftingtables:herringbone_birch_crafting_table",
                    "variantcraftingtables:herringbone_dark_oak_crafting_table",
                    "variantcraftingtables:herringbone_jungle_crafting_table",
                    "variantcraftingtables:herringbone_oak_crafting_table",
                    "variantcraftingtables:herringbone_spruce_crafting_table",
                    "variantcraftingtables:herringbone_white_oak_crafting_table",
                    "variantcraftingtables:herringbone_bamboo_crafting_table",
                    "variantcraftingtables:herringbone_charred_crafting_table",
                    "variantcraftingtables:herringbone_crimson_crafting_table",
                    "variantcraftingtables:herringbone_warped_crafting_table",
                    "variantcraftingtables:cherry_oak_crafting_table",
                    "variantcraftingtables:dark_amaranth_crafting_table",
                    "variantcraftingtables:palm_crafting_table",
                    "variantcraftingtables:cypress_crafting_table",
                    "variantcraftingtables:dragons_blood_crafting_table",
                    "variantcraftingtables:elder_crafting_table",
                    "variantcraftingtables:juniper_crafting_table",
                    "variantcraftingtables:dreamwood_crafting_table",
                    "variantcraftingtables:livingwood_crafting_table",
                    "variantcraftingtables:mossy_dreamwood_crafting_table",
                    "variantcraftingtables:mossy_livingwood_crafting_table",
                    "variantcraftingtables:shimmerwood_crafting_table",
                    "variantcraftingtables:black_crafting_table",
                    "variantcraftingtables:blue_crafting_table",
                    "variantcraftingtables:brown_crafting_table",
                    "variantcraftingtables:cyan_crafting_table",
                    "variantcraftingtables:gray_crafting_table",
                    "variantcraftingtables:green_crafting_table",
                    "variantcraftingtables:light_blue_crafting_table",
                    "variantcraftingtables:light_gray_crafting_table",
                    "variantcraftingtables:lime_crafting_table",
                    "variantcraftingtables:magenta_crafting_table",
                    "variantcraftingtables:orange_crafting_table",
                    "variantcraftingtables:pink_crafting_table",
                    "variantcraftingtables:purple_crafting_table",
                    "variantcraftingtables:red_crafting_table",
                    "variantcraftingtables:white_crafting_table",
                    "variantcraftingtables:yellow_crafting_table",
                    "variantcraftingtables:ancient_oak_crafting_table",
                    "variantcraftingtables:blighted_balsa_crafting_table",
                    "variantcraftingtables:swamp_cypress_crafting_table",
                    "variantcraftingtables:willow_crafting_table",
                    "variantcraftingtables:mango_crafting_table",
                    "variantcraftingtables:wisteria_crafting_table",
                    "variantcraftingtables:bamboo_crafting_table_ve",
                    "variantcraftingtables:redwood_crafting_table",
                    "variantcraftingtables:azalea_crafting_table",
                    "variantcraftingtables:coconut_crafting_table",
                    "variantcraftingtables:flowering_azalea_crafting_table",
                    "variantcraftingtables:walnut_crafting_table",
                    "variantcraftingtables:stripped_bamboo_crafting_table",
                    "variantcraftingtables:crystal_crafting_table",
                    "variantcraftingtables:golden_oak_crafting_table",
                    "variantcraftingtables:orange_crafting_table_pl",
                    "variantcraftingtables:skyroot_crafting_table",
                    "variantcraftingtables:wisteria_crafting_table_pl",
                    "variantcraftingtables:cinnamon_crafting_table",
                    "variantcraftingtables:jade_crafting_table",
                    "variantcraftingtables:moon_crafting_table",
                    "variantcraftingtables:shadow_crafting_table"
            )
            .map(ResourceLocation::new)
            .collect(ImmutableList.toImmutableList());

    private Set<ResourceLocation> locationValues;
    private Set<Block> blockValues;

    public Stream<Block> getBlockStream() {
        return this.locationValues.stream()
                .filter(Registry.BLOCK::containsKey)
                .map(Registry.BLOCK::get)
                .filter(block -> block instanceof CraftingTableBlock);
    }

    public boolean isValidCraftingTable(ResourceLocation id, Block block) {
        return block instanceof CraftingTableBlock && this.locationValues.contains(id);
    }

    public boolean contains(Block block) {
        if (this.blockValues == null) {
            this.blockValues = this.getBlockStream().collect(Collectors.toSet());
        }
        return this.blockValues.contains(block);
    }

    public void clearCache() {
        this.blockValues = null;
    }

    public void load() {
        JsonConfigFileUtil.getAndLoad(JSON_CONFIG_NAME, JsonConfigBuilder::serialize, (FileReader reader) -> this.locationValues = Sets.newHashSet(JsonConfigBuilder.deserialize(reader)));
    }

    private static void serialize(File jsonFile) {
        final JsonObject jsonObject = JsonSerializationUtil.getConfigBase(String.format("Crafting table blocks to enable %s support for.", VisualWorkbench.MOD_NAME));
        Type token = new TypeToken<List<ResourceLocation>>(){}.getType();
        JsonElement jsonElement = GSON.toJsonTree(DEFAULT_VISUAL_WORKBENCHES, token);
        jsonObject.add("values", jsonElement);
        JsonConfigFileUtil.saveToFile(jsonFile, jsonObject);
    }

    private static ResourceLocation[] deserialize(FileReader reader) {
        final JsonElement values = JsonSerializationUtil.readJsonObject(reader).get("values");
        if (values != null) {
            return GSON.fromJson(values, ResourceLocation[].class);
        }
        VisualWorkbench.LOGGER.error("Unable to read {} json config file", VisualWorkbench.MOD_NAME);
        return new ResourceLocation[0];
    }
}
