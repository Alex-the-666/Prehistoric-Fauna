package superlord.prehistoricfauna.entity.tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import superlord.prehistoricfauna.PrehistoricFauna;
import superlord.prehistoricfauna.init.ItemInit;
import superlord.prehistoricfauna.init.TileEntityRegistry;
import superlord.prehistoricfauna.inventory.ContainerPaleoscribe;
import superlord.prehistoricfauna.message.MessageUpdatePaleoscribe;
import superlord.prehistoricfauna.util.EnumPaleoPages;

public class TileEntityPaleoscribe extends LockableTileEntity implements ITickableTileEntity, ISidedInventory {
	
	private static final int[] slotsTop = new int[] {0};
	private static final int[] slotsSides = new int[] {1};
	private static final int[] slotsBottom = new int[] {0};
	private static final Random RANDOM = new Random();
	private static final ArrayList<EnumPaleoPages> EMPTY_LIST = new ArrayList<>();
	public final IIntArray furnaceData = new IIntArray() {
		@Override
		public int get(int index) {
			return 0;
		}
		@Override
		public void set(int index, int value) {
			
		}
		@Override
		public int size() {
			return 0;
		}
	};
	public float pageFlip;
	public float pageFlipPrev;
	public float pageHelp1;
	public float pageHelp2;
	public EnumPaleoPages[] selectedPages = new EnumPaleoPages[3];
	net.minecraftforge.items.IItemHandler handlerUp = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.Direction.UP);
	net.minecraftforge.items.IItemHandler handlerDown = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, Direction.DOWN);
	net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers = net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN);
	private Random localRand = new Random();
	private NonNullList<ItemStack> stacks = NonNullList.withSize(3, ItemStack.EMPTY);
	
	public TileEntityPaleoscribe() {
		super(TileEntityRegistry.PALEOSCRIBE);
	}
	
	@Override
	public void tick() {
		float f1 = this.pageHelp1;
		do {
			this.pageHelp1 += RANDOM.nextInt(4) - RANDOM.nextInt(4);
		} while (f1 == this.pageHelp1);
		this.pageFlipPrev = this.pageFlip;
		float f = (this.pageHelp1 - this.pageFlip) * 0.04F;
		float f3 = 0.02F;
		f = MathHelper.clamp(f, -f3, f3);
		this.pageHelp2 += (f - this.pageHelp2) * 0.9F;
		this.pageFlip += this.pageHelp2;
	}
	
	@Override
	public int getSizeInventory() {
		return 2;
	}
	
	@Override
	public ItemStack getStackInSlot(int index) {
		return this.stacks.get(index);
	}
	
	@SuppressWarnings({ "unused", "rawtypes" })
	private boolean canAddPage() {
		if(this.stacks.get(0).isEmpty()) {
			return false;
		} else {
			ItemStack itemstack = this.stacks.get(0).copy();
			
			if (itemstack.isEmpty()) {
				return false;
			}
			if (itemstack.getItem() != ItemInit.PALEOPEDIA.get()) {
				return false;
			}
			if(itemstack.getItem() == ItemInit.PALEOPEDIA.get()) {
				List list = EnumPaleoPages.possiblePages(itemstack);
				if (list == null || list.isEmpty()) {
					return false;
				}
			}
			if(this.stacks.get(2).isEmpty()) {
				return true;
			}
			int result = stacks.get(2).getCount() + itemstack.getCount();
			return result <= getInventoryStackLimit() && result <= this.stacks.get(2).getMaxStackSize();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ArrayList<EnumPaleoPages> getPossiblePages() {
		List list = EnumPaleoPages.possiblePages(this.stacks.get(0));
		if (list != null && !list.isEmpty()) {
			return (ArrayList<EnumPaleoPages>) list;
		}
		return EMPTY_LIST;
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count) {
		if(!this.stacks.get(index).isEmpty()) {
			ItemStack itemstack;
			if(this.stacks.get(index).getCount() <= count) {
				itemstack = this.stacks.get(index);
				this.stacks.set(index, ItemStack.EMPTY);
				return itemstack;
			} else {
				itemstack = this.stacks.get(index).split(count);
				if(this.stacks.get(index).getCount() == 0) {
					this.stacks.set(index, ItemStack.EMPTY);
				}
				return itemstack;
			}
		}else {
			return ItemStack.EMPTY;
		}
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		boolean flag = !stack.isEmpty() && stack.isItemEqual(this.stacks.get(index)) && ItemStack.areItemStackTagsEqual(stack, this.stacks.get(index));
		this.stacks.set(index, stack);
		if(!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
		if(index == 0 && !flag) {
			this.markDirty();
			selectedPages = randomizePages(getStackInSlot(0), getStackInSlot(1));
		}
	}
	
	public EnumPaleoPages[] randomizePages(ItemStack paleopedia, ItemStack paleopage) {
		if (!world.isRemote) {
			if(paleopedia.getItem() == ItemInit.PALEOPEDIA.get()) {
				List<EnumPaleoPages> possibleList = getPossiblePages();
				localRand.setSeed(this.world.getGameTime());
				Collections.shuffle(possibleList, localRand);
				if(possibleList.size() > 0) {
					selectedPages[0] = possibleList.get(0);
				} else {
					selectedPages[0] = null;
				}
				if (possibleList.size() > 1) {
					selectedPages[1] = possibleList.get(1);
				} else {
					selectedPages[1] = null;
				}
				if (possibleList.size() > 2) {
					selectedPages[2] = possibleList.get(2);
				} else {
					selectedPages[2] = null;
				}
			}
			int page1 = selectedPages[0] == null ? -1 : selectedPages[0].ordinal();
			int page2 = selectedPages[1] == null ? -1 : selectedPages[1].ordinal();
			int page3 = selectedPages[2] == null ? -1 : selectedPages[2].ordinal();
			PrehistoricFauna.sendMSGToAll(new MessageUpdatePaleoscribe(pos.toLong(), page1, page2, page3));
		}
		return selectedPages;
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.stacks);
		selectedPages = new EnumPaleoPages[3];
		for (int i = 0; i < 3; i++) {
			selectedPages[i] = EnumPaleoPages.fromInt(compound.getInt("SelectedPage" + (i + 1)));
		}
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		ItemStackHelper.saveAllItems(compound, this.stacks);
		for (int i = 0; i < 3; i++) {
			compound.putInt("SelectedPage" + (i + 1), selectedPages[i].ordinal());
		}
		return compound;
	}
	
	@Override
	public void openInventory(PlayerEntity palyer) {
		
	}
	
	@Override
	public void closeInventory(PlayerEntity player) {
		
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return true;
	}
	
	@Override
	public void clear() {
		this.stacks.clear();
	}
	
	public ITextComponent getName() {
		return new TranslationTextComponent("block.prehistoricfauna.paleoscribe");
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
		return false;
	}
	
	@Override
	public boolean hasCustomName() {
		return false;
	}
	
	@Override
	public int[] getSlotsForFace(Direction side) {
		return side == Direction.DOWN ? slotsBottom : (side == Direction.UP ? slotsTop : slotsSides);
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack itemStack, Direction direction) {
		return this.isItemValidForSlot(index, itemStack);
	}
	
	public String getGuiID() {
		return "prehistoricfauna:paleoscribe";
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		read(packet.getNbtCompound());
	}
	
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}
	
	@Override
	protected ITextComponent getDefaultName() {
		return getName();
	}
	
	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return null;
	}
	
	@Override
	public boolean isEmpty() {
		for(ItemStack itemstack : this.stacks) {
			if(!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
		if (!this.removed && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if(facing == Direction.DOWN)
				return handlers[1].cast();
			else
				return handlers[0].cast();
		}
		return super.getCapability(capability, facing);
	}
	
	@Nullable
	@Override
	public Container createMenu(int id, PlayerInventory playerinventory, PlayerEntity player) {
		return new ContainerPaleoscribe(id, this, playerinventory, furnaceData);
	}
	
}
