package superlord.prehistoricfauna.common.entities;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.GameRules;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import superlord.prehistoricfauna.common.blocks.CamarasaurusEggBlock;
import superlord.prehistoricfauna.init.PFBlocks;
import superlord.prehistoricfauna.init.PFCustomDamageSource;
import superlord.prehistoricfauna.init.PFEntities;
import superlord.prehistoricfauna.init.PFItems;
import superlord.prehistoricfauna.init.SoundInit;

public class CamarasaurusEntity extends DinosaurEntity {

	private static final DataParameter<Boolean> HAS_EGG = EntityDataManager.createKey(CamarasaurusEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IS_DIGGING = EntityDataManager.createKey(CamarasaurusEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IS_JUVENILE = EntityDataManager.createKey(CamarasaurusEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> ALBINO = EntityDataManager.createKey(CamarasaurusEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> MELANISTIC = EntityDataManager.createKey(CamarasaurusEntity.class, DataSerializers.BOOLEAN);
	private int warningSoundTicks;
	private int isDigging;

	public CamarasaurusEntity(EntityType<? extends CamarasaurusEntity> type, World world) {
		super(type, world);
	}

	public boolean isDigging() {
		return this.dataManager.get(IS_DIGGING);
	}

	private void setDigging(boolean isDigging) {
		this.isDigging = isDigging ? 1 : 0;
		this.dataManager.set(IS_DIGGING, isDigging);
	}
	
	public boolean isJuvenile() {
		return this.dataManager.get(IS_JUVENILE);
	}

	private void setJuvenile(boolean isJuvenile) {
		this.dataManager.set(IS_JUVENILE, isJuvenile);
	}

	public boolean hasEgg() {
		return this.dataManager.get(HAS_EGG);
	}

	private void setHasEgg(boolean hasEgg) {
		this.dataManager.set(HAS_EGG, hasEgg);
	}

	public boolean isAlbino() {
		return this.dataManager.get(ALBINO);
	}

	private void setAlbino(boolean isAlbino) {
		this.dataManager.set(ALBINO, isAlbino);
	}

	public boolean isMelanistic() {
		return this.dataManager.get(MELANISTIC);
	}

	private void setMelanistic(boolean isMelanistic) {
		this.dataManager.set(MELANISTIC, isMelanistic);
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == PFItems.PTILOPHYLLUM_FRONDS.get();
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new CamarasaurusEntity.MeleeAttackGoal());
		this.goalSelector.addGoal(1, new CamarasaurusEntity.PanicGoal());
		this.goalSelector.addGoal(4, new CamarasaurusEntity.CamarasaurusFollowParentGoal(this, 1.25D));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new CamarasaurusEntity.HurtByTargetGoal());
		this.targetSelector.addGoal(2, new CamarasaurusEntity.AttackPlayerGoal());
		this.goalSelector.addGoal(8, new CamarasaurusEntity.LayEggGoal(this, 1.0D));
		this.goalSelector.addGoal(2, new CamarasaurusEntity.MateGoal(this, 1.0D));
	}
	
	@Override
	public void setGrowingAge(int age) {
		super.setGrowingAge(age);
		if (this.getGrowingAge() >= -12000 && this.getGrowingAge() < 0) {
			this.setJuvenile(true);
		} else if(this.getGrowingAge() >= 0) {
			this.setJuvenile(false);
		}
	}

	public static AttributeModifierMap.MutableAttribute createAttributes() {
		return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 200.0D).createMutableAttribute(Attributes.FOLLOW_RANGE, 20.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.22D).createMutableAttribute(Attributes.ATTACK_DAMAGE, 12.0D);
	}

	protected SoundEvent getAmbientSound() {
		return SoundInit.CAMARASAURUS_IDLE;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundInit.CAMARASAURUS_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundInit.CAMARASAURUS_DEATH;
	}

	protected void playWarningSound() {
		if (this.warningSoundTicks <= 0) {
			this.playSound(SoundInit.CAMARASAURUS_WARN, 1.0F, this.getSoundPitch());
			this.warningSoundTicks = 40;
		}
	}

	protected void registerData() {
		super.registerData();
		this.dataManager.register(HAS_EGG, false);
		this.dataManager.register(IS_DIGGING, false);
		this.dataManager.register(ALBINO, false);
		this.dataManager.register(MELANISTIC, false);
		this.dataManager.register(IS_JUVENILE, false);
	}

	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putBoolean("HasEgg", this.hasEgg());
		compound.putBoolean("IsAlbino", this.isAlbino());
		compound.putBoolean("IsMelanistic", this.isMelanistic());
	}

	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setHasEgg(compound.getBoolean("HasEgg"));
		this.setAlbino(compound.getBoolean("IsAlbino"));
		this.setMelanistic(compound.getBoolean("IsMelanistic"));
	}

	public void tick() {
		super.tick();
		if (this.warningSoundTicks > 0) {
			--this.warningSoundTicks;
		}
	}
	
	@Override
	public void livingTick() {
		super.livingTick();
		if (this.getMotion().x != 0 && !this.isChild() || this.getMotion().z != 0  && !this.isChild() || this.getMotion().y != 0 && !this.isChild() ) {
			for (Entity entity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(1, 0, 1))) {
				if (!(entity instanceof CamarasaurusEntity)) {
					entity.attackEntityFrom(PFCustomDamageSource.SAUROPOD_TRAMPLING, (float) 5.0D);
				}
			}
		}
	}

 
	public boolean attackEntityAsMob(Entity entityIn) {
		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)((int)this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
		if (flag) {
			this.applyEnchantments(this, entityIn);
		}

		return flag;
	}	

	class AttackPlayerGoal extends NearestAttackableTargetGoal<PlayerEntity> {
		public AttackPlayerGoal() {
			super(CamarasaurusEntity.this, PlayerEntity.class, 20, true, true, (Predicate<LivingEntity>)null);
		}

		public boolean shouldExecute() {
			if (CamarasaurusEntity.this.isChild()) {
				return false;
			} else {
				if (super.shouldExecute()) {
					for(CamarasaurusEntity camarasaurus : CamarasaurusEntity.this.world.getEntitiesWithinAABB(CamarasaurusEntity.class, CamarasaurusEntity.this.getBoundingBox().grow(8.0D, 4.0D, 8.0D))) {
						if (camarasaurus.isChild()) {
							return true;
						}
					}
				}
				return false;
			}
		}

		protected double getTargetDistance() {
			return super.getTargetDistance() * 0.5D;
		}
	}

	@Nullable
	public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		Random rand = new Random();
		int birthNumber = rand.nextInt(399);
		if (birthNumber >= 0 && birthNumber < 4) {
			this.setAlbino(true);
		} else if (birthNumber >= 4 && birthNumber < 7) {
			this.setMelanistic(true);
		}
		return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	class HurtByTargetGoal extends net.minecraft.entity.ai.goal.HurtByTargetGoal {
		public HurtByTargetGoal() {
			super(CamarasaurusEntity.this);
		}

		public void startExecuting() {
			super.startExecuting();
			if (CamarasaurusEntity.this.isChild()) {
				this.alertOthers();
				this.resetTask();
			}
		}

		protected void setAttackTarget(MobEntity mobIn, LivingEntity targetIn) {
			if (mobIn instanceof CamarasaurusEntity && !mobIn.isChild()) {
				super.setAttackTarget(mobIn, targetIn);
			}
		}
	}

	class MeleeAttackGoal extends net.minecraft.entity.ai.goal.MeleeAttackGoal {
		public MeleeAttackGoal() {
			super(CamarasaurusEntity.this, 1.25D, true);
		}

		protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
			double d0 = this.getAttackReachSqr(enemy);
			if (distToEnemySqr <= d0 && this.func_234040_h_()) {
				this.func_234039_g_();
				this.attacker.attackEntityAsMob(enemy);
			} else if (distToEnemySqr <= d0 * 2.0D) {
				if (this.func_234040_h_()) {
					this.func_234039_g_();
				}

				if (this.func_234041_j_() <= 10) {
					CamarasaurusEntity.this.playWarningSound();
				}
			} else {
				this.func_234039_g_();
			}

		}

		public boolean shouldContinueExecuting() {
			float f = this.attacker.getBrightness();
			if (f >= 0.5F && this.attacker.getRNG().nextInt(100) == 0) {
				this.attacker.setAttackTarget((LivingEntity)null);
				return false;
			} else {
				return super.shouldContinueExecuting();
			}
		}

		public void resetTask() {
			super.resetTask();
		}

		protected double getAttackReachSqr(LivingEntity attackTarget) {
			return (double)(4.0F + attackTarget.getWidth());
		}
	}

	class PanicGoal extends net.minecraft.entity.ai.goal.PanicGoal {
		public PanicGoal() {
			super(CamarasaurusEntity.this, 2.0D);
		}

		public boolean shouldExecute() {
			return !CamarasaurusEntity.this.isChild() && !CamarasaurusEntity.this.isBurning() ? false : super.shouldExecute();
		}
	}

	static class LayEggGoal extends MoveToBlockGoal {
		private final CamarasaurusEntity camarasaurus;

		LayEggGoal(CamarasaurusEntity camarasaurus, double speedIn) {
			super(camarasaurus, speedIn, 16);
			this.camarasaurus = camarasaurus;
		}

		public boolean shouldExecute() {
			return this.camarasaurus.hasEgg() ? super.shouldExecute() : false;
		}

		public boolean shouldContinueExecuting() {
			return super.shouldContinueExecuting() && this.camarasaurus.hasEgg();
		}

		public void tick() {
			super.tick();
			BlockPos blockpos = new BlockPos(this.camarasaurus.getPositionVec());
			if (!this.camarasaurus.isInWater() && this.getIsAboveDestination()) {
				if (this.camarasaurus.isDigging < 1) {
					this.camarasaurus.setDigging(true);
				} else if (this.camarasaurus.isDigging > 200) {
					World world = this.camarasaurus.world;
					world.playSound((PlayerEntity)null, blockpos, SoundEvents.ENTITY_TURTLE_LAY_EGG, SoundCategory.BLOCKS, 0.3F, 0.9F + world.rand.nextFloat() * 0.2F);
					world.setBlockState(this.destinationBlock.up(), PFBlocks.CAMARASAURUS_EGG.getDefaultState().with(CamarasaurusEggBlock.EGGS, Integer.valueOf(this.camarasaurus.rand.nextInt(4) + 1)), 3);
					this.camarasaurus.setHasEgg(false);
					this.camarasaurus.setDigging(false);
					this.camarasaurus.setInLove(600);
				}
				if (this.camarasaurus.isDigging()) {
					this.camarasaurus.isDigging++;
				}
			}
		}

		protected boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos) {
			if (!worldIn.isAirBlock(pos.up())) {
				return false;
			} else {
				Block block = worldIn.getBlockState(pos).getBlock();
				return block == PFBlocks.SILT || block == PFBlocks.HARDENED_SILT || block == Blocks.SAND;
			}
		}
	}

	static class MateGoal extends BreedGoal {
		private final CamarasaurusEntity camarasaurus;

		MateGoal(CamarasaurusEntity camarasaurus, double speedIn) {
			super(camarasaurus, speedIn);
			this.camarasaurus = camarasaurus;
		}

		public boolean shouldExecute() {
			return super.shouldExecute() && !this.camarasaurus.hasEgg();
		}

		protected void spawnBaby() {
			ServerPlayerEntity serverplayerentity = this.animal.getLoveCause();
			if (serverplayerentity == null && this.targetMate.getLoveCause() != null) {
				serverplayerentity = this.targetMate.getLoveCause();
			}

			if (serverplayerentity != null) {
				serverplayerentity.addStat(Stats.ANIMALS_BRED);
				CriteriaTriggers.BRED_ANIMALS.trigger(serverplayerentity, this.animal, this.targetMate, (AgeableEntity)null);
			}

			this.camarasaurus.setHasEgg(true);
			this.animal.resetInLove();
			this.targetMate.resetInLove();
			Random random = this.animal.getRNG();
			if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
				this.world.addEntity(new ExperienceOrbEntity(this.world, this.animal.getPosX(), this.animal.getPosY(), this.animal.getPosZ(), random.nextInt(7) + 1));
			}
		}
	}

	@Override
	public AgeableEntity func_241840_a(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
		CamarasaurusEntity entity = new CamarasaurusEntity(PFEntities.CAMARASAURUS_ENTITY, this.world);
		entity.onInitialSpawn((IServerWorld) world, this.world.getDifficultyForLocation(new BlockPos(entity.getPositionVec())), SpawnReason.BREEDING, (ILivingEntityData)null, (CompoundNBT)null);
		return entity;
	}
	
	class CamarasaurusFollowParentGoal extends Goal {
		private final CamarasaurusEntity babyCamarasaurusEntity;
		private CamarasaurusEntity parentCamarasaurusEntity;
		private final double moveSpeed;
		private int delayCounter;

		public CamarasaurusFollowParentGoal(CamarasaurusEntity camarasaurus, double speed) {
			this.babyCamarasaurusEntity = camarasaurus;
			this.moveSpeed = speed;
		}

		public boolean shouldExecute() {
			if (this.babyCamarasaurusEntity.isChild() && !this.babyCamarasaurusEntity.isJuvenile()) {
				List<CamarasaurusEntity> list = this.babyCamarasaurusEntity.world.getEntitiesWithinAABB(this.babyCamarasaurusEntity.getClass(), this.babyCamarasaurusEntity.getBoundingBox().grow(8.0D, 4.0D, 8.0D));
				CamarasaurusEntity camarasaurusEntity = null;
				double d0 = Double.MAX_VALUE;
				for (CamarasaurusEntity tyrannosaurusEntity1 : list) {
					if (!tyrannosaurusEntity1.isChild()) {
						double d1 = this.babyCamarasaurusEntity.getDistanceSq(tyrannosaurusEntity1);
						if (!(d1 > d0)) {
							d0 = d1;
							camarasaurusEntity = tyrannosaurusEntity1;
						}
					}
				}
				if (camarasaurusEntity == null) {
					return false;
				} else if (d0 < 9.0D) {
					return false;
				} else {
					this.parentCamarasaurusEntity = camarasaurusEntity;
					return true;
				}
			} else {
				return false;
			}
		}

		public boolean shouldContinueExecuting() {
			if (!this.babyCamarasaurusEntity.isJuvenile() || !this.babyCamarasaurusEntity.isChild()) {
				return false;
			} else if (!this.parentCamarasaurusEntity.isAlive()) {
				return false;
			} else  if(this.babyCamarasaurusEntity.isChild() && !this.babyCamarasaurusEntity.isJuvenile()){
				double d0 = this.babyCamarasaurusEntity.getDistanceSq(this.parentCamarasaurusEntity);
				return !(d0 < 9.0D) && !(d0 > 256.0D);
			} else {
				return false;
			}
		}

		public void startExecuting() {
			this.delayCounter = 0;
		}

		public void resetTask() {
			this.parentCamarasaurusEntity = null;
		}

		public void tick() {
			if (--this.delayCounter <= 0) {
				this.delayCounter = 10;
				this.babyCamarasaurusEntity.getNavigator().tryMoveToEntityLiving(this.parentCamarasaurusEntity, this.moveSpeed);
			}
		}
	}

}
