package superlord.prehistoricfauna.init;

import static superlord.prehistoricfauna.util.Reference.MOD_ID;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import superlord.prehistoricfauna.blocks.BlockBase;
import superlord.prehistoricfauna.blocks.BlockCretaceousFossil;
import superlord.prehistoricfauna.blocks.BlockCycadeoidea;
import superlord.prehistoricfauna.blocks.BlockJurassicFossil;
import superlord.prehistoricfauna.blocks.BlockLamp;
import superlord.prehistoricfauna.blocks.BlockMesh;
import superlord.prehistoricfauna.blocks.BlockModWall;
import superlord.prehistoricfauna.blocks.BlockMud;
import superlord.prehistoricfauna.blocks.BlockPaleozoicFossil;
import superlord.prehistoricfauna.blocks.BlockPrehistoricPlant;
import superlord.prehistoricfauna.machines.combiner.Block_DNACombiner;
import superlord.prehistoricfauna.machines.combiner.TileEntity_DNACombiner;
import superlord.prehistoricfauna.machines.culturevat.Block_CultureVat;
import superlord.prehistoricfauna.machines.culturevat.TileEntityCultureVat;
import superlord.prehistoricfauna.machines.extractor.Block_DNAExtractor;
import superlord.prehistoricfauna.machines.extractor.TileEntity_DNAExtractor;
import superlord.prehistoricfauna.util.IHasModel;

@Mod.EventBusSubscriber
public class ModBlocks {

	//Machines
	public static final Block_DNAExtractor ANALYZER = new Block_DNAExtractor(false);
	public static final Block_DNAExtractor ANALYZER_ACTIVE = new Block_DNAExtractor(true);
	public static final Block_DNACombiner COMBINER = new Block_DNACombiner(false);
	public static final Block_DNACombiner COMBINER_ACTIVE = new Block_DNACombiner(true);
    public static final Block_CultureVat CULTURE_VAT = new Block_CultureVat(false);
    public static final Block_CultureVat CULTURE_VAT_ACTIVE = new Block_CultureVat(true);
    
    //Fossils
    public static final BlockCretaceousFossil CRETACEOUS_FOSSIL = new BlockCretaceousFossil("cretaceous_fossil", Material.ROCK);
    public static final BlockJurassicFossil JURASSIC_FOSSIL = new BlockJurassicFossil("jurassic_fossil", Material.ROCK);
    public static final BlockPaleozoicFossil PALEOZOIC_FOSSIL = new BlockPaleozoicFossil("paleozoic_fossil", Material.ROCK);
    
    public static final BlockBase GRAVEL_PATH = new BlockBase("gravel_path", Material.GROUND);
    public static final BlockBase RED_GRAVEL_PATH = new BlockBase("red_gravel_path", Material.SAND);
    public static final BlockBase HERRINGBONE_PATH = new BlockBase("herringbone", Material.ROCK);
    public static final BlockBase RED_HERRINGBONE_PATH = new BlockBase("red_herringbone", Material.ROCK);
    public static final BlockBase COBBLESTONE_BRICKS = new BlockBase("cobble_bricks", Material.ROCK);
    public static final BlockBase RED_COBBLESTONE_BRICKS = new BlockBase("red_cobble_bricks", Material.ROCK);
    public static final BlockModWall BRICK_WALL = new BlockModWall("brick_wall", Material.ROCK);
    public static final BlockModWall STONE_BRICK_WALL = new BlockModWall("stone_brick_wall", Material.ROCK);
    public static final BlockMud MUD = new BlockMud("mud", Material.GROUND);
    public static final BlockMesh MESH = new BlockMesh(Material.GLASS, true, "mesh");
    public static final BlockLamp LAMP = new BlockLamp("lamp", Material.GLASS);
    public static final BlockPrehistoricPlant COOKSONIA = new BlockPrehistoricPlant("cooksonia", Material.PLANTS);
    public static final BlockPrehistoricPlant ARCHAEAMPHORA = new BlockPrehistoricPlant("archaeamphora", Material.PLANTS);

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                CRETACEOUS_FOSSIL,
                JURASSIC_FOSSIL,
                ANALYZER,
                ANALYZER_ACTIVE,
                COMBINER,
                COMBINER_ACTIVE,
                GRAVEL_PATH,
                RED_GRAVEL_PATH,
                HERRINGBONE_PATH,
                RED_HERRINGBONE_PATH,
                COBBLESTONE_BRICKS,
                RED_COBBLESTONE_BRICKS,
                BRICK_WALL,
                STONE_BRICK_WALL,
                MUD,
                MESH,
                LAMP,
                COOKSONIA,
                ARCHAEAMPHORA,
                PALEOZOIC_FOSSIL,
                CULTURE_VAT,
                CULTURE_VAT_ACTIVE

        );
        GameRegistry.registerTileEntity(TileEntity_DNAExtractor.class, new ResourceLocation(MOD_ID, "tile_dna_extractor"));
        GameRegistry.registerTileEntity(TileEntity_DNACombiner.class, new ResourceLocation(MOD_ID, "tile_dna_combiner"));
        GameRegistry.registerTileEntity(TileEntityCultureVat.class, new ResourceLocation(MOD_ID, "tile_cultivate"));
    }

    @SubscribeEvent
    public static void onItemBlockRegister(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new ItemBlock(CRETACEOUS_FOSSIL).setRegistryName(CRETACEOUS_FOSSIL.getRegistryName()),
                new ItemBlock(JURASSIC_FOSSIL).setRegistryName(JURASSIC_FOSSIL.getRegistryName()),
                new ItemBlock(ANALYZER).setRegistryName(ANALYZER.getRegistryName()),
                new ItemBlock(ANALYZER_ACTIVE).setRegistryName(ANALYZER_ACTIVE.getRegistryName()),
                new ItemBlock(COMBINER).setRegistryName(COMBINER.getRegistryName()),
                new ItemBlock(COMBINER_ACTIVE).setRegistryName(COMBINER_ACTIVE.getRegistryName()),
                new ItemBlock(GRAVEL_PATH).setRegistryName(GRAVEL_PATH.getRegistryName()),
                new ItemBlock(RED_GRAVEL_PATH).setRegistryName(RED_GRAVEL_PATH.getRegistryName()),
                new ItemBlock(HERRINGBONE_PATH).setRegistryName(HERRINGBONE_PATH.getRegistryName()),
                new ItemBlock(RED_HERRINGBONE_PATH).setRegistryName(RED_HERRINGBONE_PATH.getRegistryName()),
                new ItemBlock(COBBLESTONE_BRICKS).setRegistryName(COBBLESTONE_BRICKS.getRegistryName()),
                new ItemBlock(RED_COBBLESTONE_BRICKS).setRegistryName(RED_COBBLESTONE_BRICKS.getRegistryName()),
                new ItemBlock(BRICK_WALL).setRegistryName(BRICK_WALL.getRegistryName()),
                new ItemBlock(STONE_BRICK_WALL).setRegistryName(STONE_BRICK_WALL.getRegistryName()),
                new ItemBlock(MUD).setRegistryName(MUD.getRegistryName()),
                new ItemBlock(MESH).setRegistryName(MESH.getRegistryName()),
                new ItemBlock(LAMP).setRegistryName(LAMP.getRegistryName()),
                new ItemBlock(COOKSONIA).setRegistryName(COOKSONIA.getRegistryName()),
                new ItemBlock(ARCHAEAMPHORA).setRegistryName(ARCHAEAMPHORA.getRegistryName()),
                new ItemBlock(PALEOZOIC_FOSSIL).setRegistryName(PALEOZOIC_FOSSIL.getRegistryName()),
                new ItemBlock(CULTURE_VAT).setRegistryName(CULTURE_VAT.getRegistryName()),
                new ItemBlock(CULTURE_VAT_ACTIVE).setRegistryName(CULTURE_VAT_ACTIVE.getRegistryName())
                new ItemBlock(CYCADEOIDEA).setRegistryName(CYCADEOIDEA.getRegistryName())

                );
    }


    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        registerModel(CRETACEOUS_FOSSIL);
        registerModel(JURASSIC_FOSSIL);
        registerModel(ANALYZER);
        registerModel(ANALYZER_ACTIVE);
        registerModel(COMBINER);
        registerModel(COMBINER_ACTIVE);
        registerModel(GRAVEL_PATH);
        registerModel(RED_GRAVEL_PATH);
        registerModel(HERRINGBONE_PATH);
        registerModel(RED_HERRINGBONE_PATH);
        registerModel(COBBLESTONE_BRICKS);
        registerModel(RED_COBBLESTONE_BRICKS);
        registerModel(BRICK_WALL);
        registerModel(STONE_BRICK_WALL);
        registerModel(MUD);
        registerModel(MESH);
        registerModel(LAMP);
        registerModel(COOKSONIA);
        registerModel(ARCHAEAMPHORA);
        registerModel(PALEOZOIC_FOSSIL);
        registerModel(CULTURE_VAT);
        registerModel(CULTURE_VAT_ACTIVE);
        registerModel(CYCADEOIDEA);
    }

    private static void registerModel(Block block) {
        if (block instanceof IHasModel) {
            ((IHasModel) block).registerModels();
        }
    }
}
