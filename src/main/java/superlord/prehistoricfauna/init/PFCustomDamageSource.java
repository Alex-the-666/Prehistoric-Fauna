package superlord.prehistoricfauna.init;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class PFCustomDamageSource {
	
    public static final DamageSource HENOSTONE_TRAP = new DamageCustomDeathMessage("trap");
    
    public static final DamageSource SAUROPOD_TRAMPLING = new DamageCustomDeathMessage("trample");
    
    public static final DamageSource BLEEDING = new DamageCustomDeathMessage("bleeding");

	
	static class DamageCustomDeathMessage extends DamageSource{

        public DamageCustomDeathMessage(String damageTypeIn) {
            super(damageTypeIn);
        }

        public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
            LivingEntity livingentity = entityLivingBaseIn.getAttackingEntity();
            String s = "death.attack." + this.damageType;
            int index = entityLivingBaseIn.getRNG().nextInt(3);
            String s1 = s + "." + index;
            String s2 = s + ".attacker_" + index;
            return livingentity != null ? new TranslationTextComponent(s2, entityLivingBaseIn.getDisplayName(), livingentity.getDisplayName()) : new TranslationTextComponent(s1, entityLivingBaseIn.getDisplayName());
        }

    }

}
