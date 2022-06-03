package net.minecraft.server.inventory;

import net.minecraft.server.item.ItemArmor;
import net.minecraft.server.item.ItemStack;
import net.minecraft.server.nbt.NBTBase;
import net.minecraft.server.nbt.NBTTagCompound;
import net.minecraft.server.nbt.NBTTagList;
import net.minecraft.server.world.block.Block;
import net.minecraft.server.world.block.material.Material;
import net.minecraft.server.world.entity.impl.EntityHuman;

public class InventoryPlayer implements IInventory {

    public ItemStack[] inventorySlots = new ItemStack[37];
    public ItemStack[] armourSlots = new ItemStack[4];
    public ItemStack[] craftingSlots = new ItemStack[4];
    public int d = 0;
    private EntityHuman entityHuman;
    public boolean e = false;

    public InventoryPlayer(EntityHuman entityHuman) {
        this.entityHuman = entityHuman;
    }

    public ItemStack b() {
        return this.inventorySlots[this.d];
    }

    private int c(int i) {
        for (int j = 0; j < this.inventorySlots.length; ++j) {
            if (this.inventorySlots[j] != null && this.inventorySlots[j].c == i && this.inventorySlots[j].a < this.inventorySlots[j].b() && this.inventorySlots[j].a < this.d()) {
                return j;
            }
        }

        return -1;
    }

    private int g() {
        for (int i = 0; i < this.inventorySlots.length; ++i) {
            if (this.inventorySlots[i] == null) {
                return i;
            }
        }

        return -1;
    }

    private int a(int i, int j) {
        int k = this.c(i);

        if (k < 0) {
            k = this.g();
        }

        if (k < 0) {
            return j;
        } else {
            if (this.inventorySlots[k] == null) {
                this.inventorySlots[k] = new ItemStack(i, 0);
            }

            int l = j;

            if (j > this.inventorySlots[k].b() - this.inventorySlots[k].a) {
                l = this.inventorySlots[k].b() - this.inventorySlots[k].a;
            }

            if (l > this.d() - this.inventorySlots[k].a) {
                l = this.d() - this.inventorySlots[k].a;
            }

            if (l == 0) {
                return j;
            } else {
                j -= l;
                this.inventorySlots[k].a += l;
                this.inventorySlots[k].b = 5;
                return j;
            }
        }
    }

    public void c() {
        for (int i = 0; i < this.inventorySlots.length; ++i) {
            if (this.inventorySlots[i] != null && this.inventorySlots[i].b > 0) {
                --this.inventorySlots[i].b;
            }
        }
    }

    public boolean a(ItemStack itemstack) {
        if (itemstack.d == 0) {
            itemstack.a = this.a(itemstack.c, itemstack.a);
            if (itemstack.a == 0) {
                return true;
            }
        }

        int i = this.g();

        if (i >= 0) {
            this.inventorySlots[i] = itemstack;
            this.inventorySlots[i].b = 5;
            return true;
        } else {
            return false;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        ItemStack[] slots = this.inventorySlots;

        if (i >= slots.length) {
            i -= slots.length;
            slots = this.armourSlots;
        }

        if (i >= slots.length) {
            i -= slots.length;
            slots = this.craftingSlots;
        }

        slots[i] = itemstack;
    }

    public float a(Block block) {
        float f = 1.0F;

        if (this.inventorySlots[this.d] != null) {
            f *= this.inventorySlots[this.d].a(block);
        }

        return f;
    }

    public NBTTagList a(NBTTagList nbttaglist) {
        int i;
        NBTTagCompound nbttagcompound;

        for (i = 0; i < this.inventorySlots.length; ++i) {
            if (this.inventorySlots[i] != null) {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.a("Slot", (byte) i);
                this.inventorySlots[i].a(nbttagcompound);
                nbttaglist.a((NBTBase) nbttagcompound);
            }
        }

        for (i = 0; i < this.armourSlots.length; ++i) {
            if (this.armourSlots[i] != null) {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.a("Slot", (byte) (i + 100));
                this.armourSlots[i].a(nbttagcompound);
                nbttaglist.a((NBTBase) nbttagcompound);
            }
        }

        for (i = 0; i < this.craftingSlots.length; ++i) {
            if (this.craftingSlots[i] != null) {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.a("Slot", (byte) (i + 80));
                this.craftingSlots[i].a(nbttagcompound);
                nbttaglist.a((NBTBase) nbttagcompound);
            }
        }

        return nbttaglist;
    }

    public void b(NBTTagList nbttaglist) {
        this.inventorySlots = new ItemStack[36];
        this.armourSlots = new ItemStack[4];
        this.craftingSlots = new ItemStack[4];

        for (int i = 0; i < nbttaglist.b(); ++i) {
            NBTTagCompound nbttagcompound = (NBTTagCompound) nbttaglist.a(i);
            int j = nbttagcompound.b("Slot") & 255;

            if (j >= 0 && j < this.inventorySlots.length) {
                this.inventorySlots[j] = new ItemStack(nbttagcompound);
            }

            if (j >= 80 && j < this.craftingSlots.length + 80) {
                this.craftingSlots[j - 80] = new ItemStack(nbttagcompound);
            }

            if (j >= 100 && j < this.armourSlots.length + 100) {
                this.armourSlots[j - 100] = new ItemStack(nbttagcompound);
            }
        }
    }

    public int a() {
        return this.inventorySlots.length + 4;
    }

    public ItemStack a(int i) {
        ItemStack[] aitemstack = this.inventorySlots;

        if (i >= aitemstack.length) {
            i -= aitemstack.length;
            aitemstack = this.armourSlots;
        }

        if (i >= aitemstack.length) {
            i -= aitemstack.length;
            aitemstack = this.craftingSlots;
        }

        return aitemstack[i];
    }

    public int d() {
        return 64;
    }

    public boolean b(Block block) {
        if (block.bt != Material.d && block.bt != Material.e && block.bt != Material.t && block.bt != Material.s) {
            return true;
        } else {
            ItemStack itemstack = this.a(this.d);

            return itemstack != null ? itemstack.b(block) : false;
        }
    }

    public int e() {
        int i = 0;
        int j = 0;
        int k = 0;

        for (int l = 0; l < this.armourSlots.length; ++l) {
            if (this.armourSlots[l] != null && this.armourSlots[l].a() instanceof ItemArmor) {
                int i1 = this.armourSlots[l].c();
                int j1 = this.armourSlots[l].d;
                int k1 = i1 - j1;

                j += k1;
                k += i1;
                int l1 = ((ItemArmor) this.armourSlots[l].a()).bc;

                i += l1;
            }
        }

        if (k == 0) {
            return 0;
        } else {
            return (i - 1) * j / k + 1;
        }
    }

    public void b(int i) {
        for (int j = 0; j < this.armourSlots.length; ++j) {
            if (this.armourSlots[j] != null && this.armourSlots[j].a() instanceof ItemArmor) {
                this.armourSlots[j].a(i);
                if (this.armourSlots[j].a == 0) {
                    this.armourSlots[j].a(this.entityHuman);
                    this.armourSlots[j] = null;
                }
            }
        }
    }

    public void dropAll() {
        int i;

        for (i = 0; i < this.inventorySlots.length; ++i) {
            if (this.inventorySlots[i] != null) {
                this.entityHuman.dropItem(this.inventorySlots[i], true);
                this.inventorySlots[i] = null;
            }
        }

        for (i = 0; i < this.armourSlots.length; ++i) {
            if (this.armourSlots[i] != null) {
                this.entityHuman.dropItem(this.armourSlots[i], true);
                this.armourSlots[i] = null;
            }
        }
    }
}
