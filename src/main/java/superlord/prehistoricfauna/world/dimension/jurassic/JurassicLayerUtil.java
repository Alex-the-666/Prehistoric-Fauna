package superlord.prehistoricfauna.world.dimension.jurassic;

import java.util.function.LongFunction;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.LayerUtil;
import net.minecraft.world.gen.layer.ZoomLayer;

public class JurassicLayerUtil {
	
	private static Registry<Biome> biomeRegistry;
	
	static int getBiomeId(RegistryKey<Biome> define) {
		Biome biome = biomeRegistry.getValueForKey(define);
		return biomeRegistry.getId(biome);
	}
	
	public static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> makeLayers(LongFunction<C> contextFactory, Registry<Biome> registry) {
		biomeRegistry = registry;
		
		IAreaFactory<T> biomes = new JurassicBiomesLayer().apply(contextFactory.apply(1L));
		
		biomes = ZoomLayer.NORMAL.apply(contextFactory.apply(1000), biomes);
		biomes = ZoomLayer.NORMAL.apply(contextFactory.apply(1001), biomes);
		biomes = ZoomLayer.NORMAL.apply(contextFactory.apply(1002), biomes);
		biomes = ZoomLayer.NORMAL.apply(contextFactory.apply(1003), biomes);
		biomes = ZoomLayer.NORMAL.apply(contextFactory.apply(1004), biomes);
		biomes = ZoomLayer.NORMAL.apply(contextFactory.apply(1005), biomes);

		biomes = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, biomes, 1, contextFactory);
		
		return biomes;
	}
	
	public static Layer makeLayers(long seed, Registry<Biome> registry) {
		biomeRegistry = registry;
		IAreaFactory<LazyArea> areaFactory = makeLayers((contextSeed) -> new LazyAreaLayerContext(25, seed, contextSeed), registry);
		return new Layer(areaFactory);
	}

}
