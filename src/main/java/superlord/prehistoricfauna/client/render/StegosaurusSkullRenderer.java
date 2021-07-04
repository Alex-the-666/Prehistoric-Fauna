package superlord.prehistoricfauna.client.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import superlord.prehistoricfauna.PrehistoricFauna;
import superlord.prehistoricfauna.client.model.StegosaurusSkull;
import superlord.prehistoricfauna.common.entities.StegosaurusSkullEntity;

public class StegosaurusSkullRenderer extends MobRenderer<StegosaurusSkullEntity, StegosaurusSkull> {

    private static final ResourceLocation SKULL = new ResourceLocation(PrehistoricFauna.MOD_ID, "textures/entities/skeleton/stegosaurus_skull.png");

    public StegosaurusSkullRenderer(EntityRendererManager rm) {
        super(rm, new StegosaurusSkull(), 1.0F);
    }
    
    @Override
	public ResourceLocation getEntityTexture(StegosaurusSkullEntity entity) {
    	return SKULL;
    }
}