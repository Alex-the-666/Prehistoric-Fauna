package superlord.prehistoricfauna.client.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import superlord.prehistoricfauna.PrehistoricFauna;
import superlord.prehistoricfauna.client.model.TriceratopsSkull;
import superlord.prehistoricfauna.common.entities.TriceratopsSkullEntity;

public class TriceratopsSkullRenderer extends MobRenderer<TriceratopsSkullEntity, TriceratopsSkull> {

    private static final ResourceLocation SKULL = new ResourceLocation(PrehistoricFauna.MOD_ID, "textures/entities/skeleton/triceratops_skull.png");

    public TriceratopsSkullRenderer(EntityRendererManager rm) {
        super(rm, new TriceratopsSkull(), 1.0F);
    }
    @Override
	public ResourceLocation getEntityTexture(TriceratopsSkullEntity entity) {
    	return SKULL;
    }
}