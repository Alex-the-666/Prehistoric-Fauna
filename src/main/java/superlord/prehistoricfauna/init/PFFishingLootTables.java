package superlord.prehistoricfauna.init;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.block.Blocks;
import net.minecraft.data.loot.FishingLootTables;
import net.minecraft.item.Items;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.FishingPredicate;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.conditions.EntityHasProperty;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.LocationCheck;
import net.minecraft.loot.functions.EnchantWithLevels;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.loot.functions.SetDamage;
import net.minecraft.loot.functions.SetNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

public class PFFishingLootTables extends FishingLootTables implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {

	public static final ILootCondition.IBuilder IN_HELL_CREEK = LocationCheck.builder(LocationPredicate.Builder.builder().biome(PFBiomes.HELL_CREEK_BIOME.getKey()));
	public static final ILootCondition.IBuilder IN_HELL_CREEK_HILLS = LocationCheck.builder(LocationPredicate.Builder.builder().biome(PFBiomes.HELL_CREEK_HILLS_BIOME.getKey()));
	public static final ILootCondition.IBuilder IN_HELL_CREEK_CLEARING = LocationCheck.builder(LocationPredicate.Builder.builder().biome(PFBiomes.HELL_CREEK_CLEARING_BIOME.getKey()));
	public static final ILootCondition.IBuilder IN_HELL_CREEK_RIVER = LocationCheck.builder(LocationPredicate.Builder.builder().biome(PFBiomes.HELL_CREEK_RIVER_BIOME.getKey()));
	public static final ILootCondition.IBuilder IN_ISCHIGUALASTO = LocationCheck.builder(LocationPredicate.Builder.builder().biome(PFBiomes.ISCHIGUALASTO_FOREST_BIOME.getKey()));
	public static final ILootCondition.IBuilder IN_ISCHIGUALASTO_CLEARING = LocationCheck.builder(LocationPredicate.Builder.builder().biome(PFBiomes.ISCHIGUALASTO_CLEARING_BIOME.getKey()));
	public static final ILootCondition.IBuilder IN_ISCHIGUALASTO_RIVER = LocationCheck.builder(LocationPredicate.Builder.builder().biome(PFBiomes.ISCHIGUALASTO_RIVER_BIOME.getKey()));
	public static final ILootCondition.IBuilder IN_ISCHIGUALASTO_HILLS = LocationCheck.builder(LocationPredicate.Builder.builder().biome(PFBiomes.ISCHIGUALASTO_HILLS_BIOME.getKey()));
	public static final ILootCondition.IBuilder IN_MORRISON = LocationCheck.builder(LocationPredicate.Builder.builder().biome(PFBiomes.MORRISON_SAVANNAH_BIOME.getKey()));
	public static final ILootCondition.IBuilder IN_MORRISON_HILLS = LocationCheck.builder(LocationPredicate.Builder.builder().biome(PFBiomes.MORRISON_HILLS_BIOME.getKey()));

	@Override
	public void accept(BiConsumer<ResourceLocation, Builder> p_accept_1_) {
		p_accept_1_.accept(LootTables.GAMEPLAY_FISHING, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(TableLootEntry.builder(LootTables.GAMEPLAY_FISHING_JUNK).weight(10).quality(-2)).addEntry(TableLootEntry.builder(LootTables.GAMEPLAY_FISHING_TREASURE).weight(5).quality(2).acceptCondition(EntityHasProperty.builder(LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().fishing(FishingPredicate.func_234640_a_(true))))).addEntry(TableLootEntry.builder(LootTables.GAMEPLAY_FISHING_FISH).weight(85).quality(-1))));
		p_accept_1_.accept(LootTables.GAMEPLAY_FISHING_FISH, LootTable.builder().addLootPool(LootPool.builder().addEntry(ItemLootEntry.builder(Items.COD).weight(60)).addEntry(ItemLootEntry.builder(Items.SALMON).weight(25)).addEntry(ItemLootEntry.builder(Items.TROPICAL_FISH).weight(2)).addEntry(ItemLootEntry.builder(Items.PUFFERFISH).weight(13))));
		p_accept_1_.accept(LootTables.GAMEPLAY_FISHING_JUNK, LootTable.builder().addLootPool(LootPool.builder().addEntry(ItemLootEntry.builder(Blocks.LILY_PAD).weight(17)).addEntry(ItemLootEntry.builder(Items.LEATHER_BOOTS).weight(10).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.0F, 0.9F)))).addEntry(ItemLootEntry.builder(Items.LEATHER).weight(10)).addEntry(ItemLootEntry.builder(Items.BONE).weight(10)).addEntry(ItemLootEntry.builder(Items.POTION).weight(10).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> {
			nbt.putString("Potion", "minecraft:water");
		})))).addEntry(ItemLootEntry.builder(Items.FISHING_ROD).weight(2).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.0F, 0.9F)))).addEntry(ItemLootEntry.builder(Items.BOWL).weight(10)).addEntry(ItemLootEntry.builder(Items.INK_SAC).weight(1).acceptFunction(SetCount.builder(ConstantRange.of(10)))).addEntry(ItemLootEntry.builder(Blocks.TRIPWIRE_HOOK).weight(10)).addEntry(ItemLootEntry.builder(Items.ROTTEN_FLESH).weight(10)).addEntry(ItemLootEntry.builder(Blocks.BAMBOO).acceptCondition(IN_JUNGLE.alternative(IN_JUNGLE_HILLS).alternative(IN_JUNGLE_EDGE).alternative(IN_BAMBOO_JUNGLE).alternative(IN_MODIFIED_JUNGLE).alternative(IN_MODIFIED_JUNGLE_EDGE).alternative(IN_BAMBOO_JUNGLE_HILLS)).weight(10))));
		p_accept_1_.accept(PFLootTables.PREHISTORIC_FISHING, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(TableLootEntry.builder(PFLootTables.PREHISTORIC_FISHING_JUNK).weight(10).quality(-2)).addEntry(TableLootEntry.builder(PFLootTables.PREHISTORIC_FISHING_TREASURE).weight(5).quality(2).acceptCondition(EntityHasProperty.builder(LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().fishing(FishingPredicate.func_234640_a_(true))))).addEntry(TableLootEntry.builder(PFLootTables.PREHISTORIC_FISH).weight(85).quality(-1))));
		p_accept_1_.accept(PFLootTables.PREHISTORIC_FISH, LootTable.builder().addLootPool(LootPool.builder().addEntry(ItemLootEntry.builder(PFItems.CERATODUS.get()).acceptCondition(IN_ISCHIGUALASTO.alternative(IN_ISCHIGUALASTO_CLEARING).alternative(IN_ISCHIGUALASTO_HILLS).alternative(IN_ISCHIGUALASTO_HILLS).alternative(IN_ISCHIGUALASTO_RIVER).alternative(IN_MORRISON).alternative(IN_MORRISON_HILLS)).weight(60)).addEntry(ItemLootEntry.builder(PFItems.LEPISOSTEUS.get()).weight(50).acceptCondition(IN_HELL_CREEK.alternative(IN_HELL_CREEK_CLEARING).alternative(IN_HELL_CREEK_HILLS).alternative(IN_HELL_CREEK_RIVER))).addEntry(ItemLootEntry.builder(PFItems.MYLEDAPHUS.get()).weight(2).acceptCondition(IN_HELL_CREEK.alternative(IN_HELL_CREEK_CLEARING).alternative(IN_HELL_CREEK_HILLS).alternative(IN_HELL_CREEK_RIVER))).addEntry(ItemLootEntry.builder(PFItems.CYCLURUS.get()).weight(13)).acceptCondition(IN_HELL_CREEK.alternative(IN_HELL_CREEK_CLEARING).alternative(IN_HELL_CREEK_HILLS).alternative(IN_HELL_CREEK_HILLS).alternative(IN_HELL_CREEK_RIVER)).addEntry(ItemLootEntry.builder(PFItems.POTAMOCERATODUS.get()).acceptCondition(IN_MORRISON.alternative(IN_MORRISON_HILLS)).weight(25))));
		p_accept_1_.accept(PFLootTables.PREHISTORIC_FISHING_JUNK, LootTable.builder().addLootPool(LootPool.builder().addEntry(ItemLootEntry.builder(PFBlocks.ALGAE_CARPET).weight(17)).addEntry(ItemLootEntry.builder(PFBlocks.HORSETAIL).weight(10).acceptCondition(IN_HELL_CREEK.alternative(IN_HELL_CREEK_CLEARING).alternative(IN_HELL_CREEK_HILLS).alternative(IN_HELL_CREEK_RIVER).alternative(IN_MORRISON).alternative(IN_MORRISON_HILLS))).addEntry(ItemLootEntry.builder(Items.BONE).weight(10)).addEntry(ItemLootEntry.builder(Items.STRING).weight(5)).addEntry(ItemLootEntry.builder(Items.SLIME_BALL).weight(10)).addEntry(ItemLootEntry.builder(Items.STICK).weight(5)).addEntry(ItemLootEntry.builder(PFItems.MOSS_BALL.get()).weight(10).acceptCondition(IN_HELL_CREEK.alternative(IN_HELL_CREEK_CLEARING).alternative(IN_HELL_CREEK_HILLS).alternative(IN_HELL_CREEK_RIVER))).addEntry(ItemLootEntry.builder(Items.ROTTEN_FLESH).weight(10)).addEntry(ItemLootEntry.builder(PFItems.CRASSOSTREA_OYSTER.get()).weight(10).acceptCondition(IN_HELL_CREEK.alternative(IN_HELL_CREEK_CLEARING).alternative(IN_HELL_CREEK_HILLS).alternative(IN_HELL_CREEK_RIVER)))));
		p_accept_1_.accept(PFLootTables.PREHISTORIC_FISHING_TREASURE, LootTable.builder().addLootPool(LootPool.builder().addEntry(ItemLootEntry.builder(PFBlocks.SMALL_AMMONITE_FOSSIL)).addEntry(ItemLootEntry.builder(PFBlocks.MEDIUM_AMMONITE_FOSSIL)).addEntry(ItemLootEntry.builder(PFItems.PALEOPAGE.get())).addEntry(ItemLootEntry.builder(Items.NAME_TAG)).addEntry(ItemLootEntry.builder(Items.SADDLE)).addEntry(ItemLootEntry.builder(Items.BOW).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.0F, 0.25F))).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(30)).func_216059_e())).addEntry(ItemLootEntry.builder(Items.FISHING_ROD).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.0F, 0.25F))).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(30)).func_216059_e())).addEntry(ItemLootEntry.builder(Items.BOOK).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(30)).func_216059_e())).addEntry(ItemLootEntry.builder(Items.NAUTILUS_SHELL))));

	}


}
