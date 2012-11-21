package drceph.petrogen.common;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class ContainerPetroleumGenerator extends Container {
	
	public TileEntityPetroleumGenerator tileEntity;
	private EntityPlayer invokingPlayer;
	private int lastAmount = 0;
	private int lastLiquidId = 0;
	private int lastLiquidMeta = 0;
	private int lastCharge = 0;
	
	public ContainerPetroleumGenerator(IInventory playerInventory, TileEntityPetroleumGenerator tileEntity) {
		this.tileEntity = tileEntity;
		this.invokingPlayer = ((InventoryPlayer) playerInventory).player;
		layoutContainer(playerInventory,tileEntity);
	}

	private void layoutContainer(IInventory playerInventory, IInventory inventory) {
		addSlotToContainer(new Slot(inventory, 0, 22, 36));
		for (int inventoryRow = 0; inventoryRow < 3; inventoryRow++)
		{
			for (int inventoryColumn = 0; inventoryColumn < 9; inventoryColumn++)
			{
				addSlotToContainer(new Slot(playerInventory, inventoryColumn + inventoryRow * 9 + 9, 8 + inventoryColumn * 18, 84 + inventoryRow * 18));
			}
		}

		for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++)
		{
			addSlotToContainer(new Slot(playerInventory, hotbarSlot, 8 + hotbarSlot * 18, 142));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return tileEntity.isUseableByPlayer(entityplayer);
	}
	
	public EntityPlayer getPlayer() {
		return invokingPlayer;
	}

	@Override
	public void addCraftingToCrafters(ICrafting crafter) {
		// TODO Auto-generated method stub
		super.addCraftingToCrafters(crafter);
		
		crafter.sendProgressBarUpdate(this, 0, this.tileEntity.amount);
		crafter.sendProgressBarUpdate(this, 1, this.tileEntity.liquidId);
		crafter.sendProgressBarUpdate(this, 2, this.tileEntity.liquidMeta);
		crafter.sendProgressBarUpdate(this, 3, this.tileEntity.charge);
	}

	@Override
	public void updateCraftingResults() {
		// TODO Auto-generated method stub
		super.updateCraftingResults();
		for (int i = 0; i < this.crafters.size(); i++) {
			ICrafting crafter = (ICrafting)this.crafters.get(i);
			if (this.lastAmount != this.tileEntity.amount) {
				crafter.sendProgressBarUpdate(this, 0, this.tileEntity.amount);
			}
			if (this.lastLiquidId != this.tileEntity.liquidId) {
				crafter.sendProgressBarUpdate(this, 1, this.tileEntity.liquidId);
			}
			if (this.lastLiquidMeta != this.tileEntity.liquidMeta) {
				crafter.sendProgressBarUpdate(this, 2, this.tileEntity.liquidMeta);
			}
			if (this.lastCharge != this.tileEntity.charge) {
				crafter.sendProgressBarUpdate(this, 3, this.tileEntity.charge);
			}
		}
		
		this.lastAmount = this.tileEntity.amount;
		this.lastLiquidId = this.tileEntity.liquidId;
		this.lastLiquidMeta = this.tileEntity.liquidMeta;
		this.lastCharge = this.tileEntity.charge;
	}

	@Override
	public void onCraftMatrixChanged(IInventory par1iInventory) {
		// TODO Auto-generated method stub
		super.onCraftMatrixChanged(par1iInventory);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		// TODO Auto-generated method stub
		super.updateProgressBar(par1, par2);
		
		if (par1 == 0) {
			this.tileEntity.amount = par2;
		}
		if (par1 == 1) {
			this.tileEntity.liquidId = par2;
		}
		if (par1 == 2) {
			this.tileEntity.liquidMeta = par2;
		}
		if (par1 == 3) {
			this.tileEntity.charge = par2;
		}
	}
	
	
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slot) {
		// TODO Auto-generated method stub
		ItemStack stack = null;
        Slot slotObject = (Slot) inventorySlots.get(slot);

        //null checks and checks if the item can be stacked (maxStackSize > 1)
        if (slotObject != null && slotObject.getHasStack()) {
                ItemStack stackInSlot = slotObject.getStack();
                stack = stackInSlot.copy();

                //merges the item into player inventory since its in the tileEntity
                //this assumes only 1 slot, for inventories with > 1 slots, check out the Chest Container.
                if (slot == 0) {
                        if (!mergeItemStack(stackInSlot, 1,
                                        inventorySlots.size(), true)) {
                                return null;
                        }
                //places it into the tileEntity is possible since its in the player inventory
                } else if (!mergeItemStack(stackInSlot, 0, 1, false)) {
                        return null;
                }

                if (stackInSlot.stackSize == 0) {
                        slotObject.putStack(null);
                } else {
                        slotObject.onSlotChanged();
                }
        }

        return stack;
	}	

}
