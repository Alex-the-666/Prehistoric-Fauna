package superlord.prehistoricfauna.item;

import java.util.List;
import java.util.function.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import superlord.prehistoricfauna.PrehistoricFauna;
import superlord.prehistoricfauna.entity.PrehistoricBoatEntity;

public class PrehistoricBoatItem extends Item {
    private static final Predicate<Entity> RIDERS = EntityPredicates.NOT_SPECTATING.and(Entity::canBeCollidedWith);
    private final PrehistoricBoatEntity.PFType type;

    public PrehistoricBoatItem(PrehistoricBoatEntity.PFType typeIn, Item.Properties properties) {
        super(properties);
        this.type = typeIn;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.ANY);
        if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
            return ActionResult.resultPass(itemstack);
        } else {
            Vec3d vec3d = playerIn.getLook(1.0F);
            List<Entity> list = worldIn.getEntitiesInAABBexcluding(playerIn, playerIn.getBoundingBox().expand(vec3d.scale(5.0D)).grow(1.0D), RIDERS);
            if (!list.isEmpty()) {
                Vec3d vec3d1 = playerIn.getEyePosition(1.0F);

                for (Entity entity : list) {
                    AxisAlignedBB axisalignedbb = entity.getBoundingBox().grow(entity.getCollisionBorderSize());
                    if (axisalignedbb.contains(vec3d1)) {
                        return ActionResult.resultPass(itemstack);
                    }
                }
            }

            if (raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
            	PrehistoricBoatEntity prehistoricBoatEntity = new PrehistoricBoatEntity(worldIn, raytraceresult.getHitVec().x, raytraceresult.getHitVec().y, raytraceresult.getHitVec().z);
                PrehistoricFauna.LOGGER.info("BOAT ENTITY: " + prehistoricBoatEntity.getEntityString());
                prehistoricBoatEntity.setPFBoatType(this.type);
                prehistoricBoatEntity.rotationYaw = playerIn.rotationYaw;
                if (!worldIn.hasNoCollisions(prehistoricBoatEntity, prehistoricBoatEntity.getBoundingBox().grow(-0.1D))) {
                    return ActionResult.resultFail(itemstack);
                } else {
                    if (!worldIn.isRemote) {
                        worldIn.addEntity(prehistoricBoatEntity);
                        if (!playerIn.abilities.isCreativeMode) {
                            itemstack.shrink(1);
                        }
                    }

                    playerIn.addStat(Stats.ITEM_USED.get(this));
                    return ActionResult.resultSuccess(itemstack);
                }
            } else {
                return ActionResult.resultPass(itemstack);
            }
        }
    }
}