package superlord.prehistoricfauna.world.feature.config;

import com.mojang.serialization.Codec;

import net.minecraft.world.gen.feature.IFeatureConfig;

public class CrassostreaOystersConfig implements IFeatureConfig {
	public static int count;

	@SuppressWarnings("static-access")
	public CrassostreaOystersConfig(int count) {
		this.count = count;
	}

	public static final Codec<CrassostreaOystersConfig> field_236558_a_;
	public static final CrassostreaOystersConfig field_236559_b_ = new CrassostreaOystersConfig(count);

	static {
		field_236558_a_ = Codec.unit(() -> {
			return field_236559_b_;
		});
	}
}