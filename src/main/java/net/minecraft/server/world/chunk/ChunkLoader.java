package net.minecraft.server.world.chunk;

import net.minecraft.server.nbt.CompressedStreamTools;
import net.minecraft.server.nbt.NBTBase;
import net.minecraft.server.nbt.NBTTagCompound;
import net.minecraft.server.nbt.NBTTagList;
import net.minecraft.server.world.World;
import net.minecraft.server.world.block.tile.TileEntity;
import net.minecraft.server.world.entity.Entity;
import net.minecraft.server.world.entity.EntityTypes;

import java.io.*;
import java.util.Iterator;

public class ChunkLoader implements IChunkLoader {

    private final File a;
    private final boolean b;

    public ChunkLoader(File file1, boolean flag) {
        this.a = file1;
        this.b = flag;
    }

    private File a(int i, int j) {
        String s = "c." + Integer.toString(i, 36) + "." + Integer.toString(j, 36) + ".dat";
        String s1 = Integer.toString(i & 63, 36);
        String s2 = Integer.toString(j & 63, 36);
        File file1 = new File(this.a, s1);

        if (!file1.exists()) {
            if (!this.b) {
                return null;
            }

            file1.mkdir();
        }

        file1 = new File(file1, s2);
        if (!file1.exists()) {
            if (!this.b) {
                return null;
            }

            file1.mkdir();
        }

        file1 = new File(file1, s);
        return !file1.exists() && !this.b ? null : file1;
    }

    public Chunk a(World world, int i, int j) {
        File file1 = this.a(i, j);

        if (file1 != null && file1.exists()) {
            try {
                FileInputStream fileinputstream = new FileInputStream(file1);
                NBTTagCompound nbttagcompound = CompressedStreamTools.a(fileinputstream);

                if (!nbttagcompound.a("Level")) {
                    System.out.println("Chunk file at " + i + "," + j + " is missing level data, skipping");
                    return null;
                }

                if (!nbttagcompound.j("Level").a("Blocks")) {
                    System.out.println("Chunk file at " + i + "," + j + " is missing block data, skipping");
                    return null;
                }

                Chunk chunk = a(world, nbttagcompound.j("Level"));

                if (!chunk.a(i, j)) {
                    System.out.println("Chunk file at " + i + "," + j + " is in the wrong location; relocating. (Expected " + i + ", " + j + ", got " + chunk.x + ", " + chunk.z + ")");
                    nbttagcompound.a("xPos", i);
                    nbttagcompound.a("zPos", j);
                    chunk = a(world, nbttagcompound.j("Level"));
                }

                return chunk;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return null;
    }

    public void a(World world, Chunk chunk) {
        world.h();
        File file1 = this.a(chunk.x, chunk.z);

        if (file1.exists()) {
            world.v -= file1.length();
        }

        try {
            File file2 = new File(this.a, "tmp_chunk.dat");
            FileOutputStream fileoutputstream = new FileOutputStream(file2);
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            nbttagcompound.a("Level", (NBTBase) nbttagcompound1);
            this.a(chunk, world, nbttagcompound1);
            CompressedStreamTools.a(nbttagcompound, fileoutputstream);
            fileoutputstream.close();
            if (file1.exists()) {
                file1.delete();
            }

            file2.renameTo(file1);
            world.v += file1.length();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void a(Chunk chunk, World world, NBTTagCompound nbttagcompound) {
        world.h();
        nbttagcompound.a("xPos", chunk.x);
        nbttagcompound.a("zPos", chunk.z);
        nbttagcompound.a("LastUpdate", world.lastUpdate);
        nbttagcompound.a("Blocks", chunk.blocks);
        nbttagcompound.a("Data", chunk.blockData.data);
        nbttagcompound.a("SkyLight", chunk.skyLight.data);
        nbttagcompound.a("BlockLight", chunk.blockLight.data);
        nbttagcompound.a("HeightMap", chunk.heightMap);
        nbttagcompound.a("TerrainPopulated", chunk.terrainPopulated);
        chunk.r = false;
        NBTTagList nbttaglist = new NBTTagList();

        Iterator<?> iterator;
        NBTTagCompound nbttagcompound1;

        for (int i = 0; i < chunk.entities.length; ++i) {
            iterator = chunk.entities[i].iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                chunk.r = true;
                nbttagcompound1 = new NBTTagCompound();
                if (entity.c(nbttagcompound1)) {
                    nbttaglist.a(nbttagcompound1);
                }
            }
        }

        nbttagcompound.a("Entities", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();

        iterator = chunk.tileEntities.values().iterator();

        while (iterator.hasNext()) {
            TileEntity tileentity = (TileEntity) iterator.next();

            nbttagcompound1 = new NBTTagCompound();
            tileentity.b(nbttagcompound1);
            nbttaglist1.a(nbttagcompound1);
        }

        nbttagcompound.a("TileEntities", nbttaglist1);
    }

    public static Chunk a(World world, NBTTagCompound nbttagcompound) {
        int x = nbttagcompound.d("xPos");
        int z = nbttagcompound.d("zPos");
        Chunk chunk = new Chunk(world, x, z);

        chunk.blocks = nbttagcompound.i("Blocks");
        chunk.blockData = new NibbleArray(nbttagcompound.i("Data"));
        chunk.skyLight = new NibbleArray(nbttagcompound.i("SkyLight"));
        chunk.blockLight = new NibbleArray(nbttagcompound.i("BlockLight"));
        chunk.heightMap = nbttagcompound.i("HeightMap");
        chunk.terrainPopulated = nbttagcompound.l("TerrainPopulated");
        if (!chunk.blockData.hasData()) {
            chunk.blockData = new NibbleArray(chunk.blocks.length);
        }

        if (chunk.heightMap == null || !chunk.skyLight.hasData()) {
            chunk.heightMap = new byte[256];
            chunk.skyLight = new NibbleArray(chunk.blocks.length);
            chunk.b();
        }

        if (!chunk.blockLight.hasData()) {
            chunk.blockLight = new NibbleArray(chunk.blocks.length);
            chunk.a();
        }

        NBTTagList entities = nbttagcompound.k("Entities");
        if (entities != null) {
            for (int k = 0; k < entities.b(); ++k) {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound) entities.a(k);
                Entity entity = EntityTypes.a(nbttagcompound1, world);

                chunk.r = true;
                if (entity != null) {
                    chunk.a(entity);
                }
            }
        }

        NBTTagList tileEntities = nbttagcompound.k("TileEntities");
        if (tileEntities != null) {
            for (int l = 0; l < tileEntities.b(); ++l) {
                NBTTagCompound nbttagcompound2 = (NBTTagCompound) tileEntities.a(l);
                TileEntity tileentity = TileEntity.c(nbttagcompound2);

                if (tileentity != null) {
                    chunk.a(tileentity);
                }
            }
        }

        return chunk;
    }

    public void a() {
    }

    public void b() {
    }

    public void b(World world, Chunk chunk) {
    }
}
