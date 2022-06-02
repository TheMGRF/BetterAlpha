package net.minecraft.server.world.chunk;

public final class ChunkCoordinates {

    public final int a;
    public final int y;
    public final int b;

    public ChunkCoordinates(int i, int j, int k) {
        this.a = i;
        this.y = j;
        this.b = k;
    }

    public boolean equals(Object object) {
        if (!(object instanceof ChunkCoordinates)) {
            return false;
        } else {
            ChunkCoordinates chunkcoordinates = (ChunkCoordinates) object;

            return this.a == chunkcoordinates.a && this.y == chunkcoordinates.y && this.b == chunkcoordinates.b;
        }
    }

    public int hashCode() {
        return this.a + this.b << 8 + this.y << 16;
    }
}
