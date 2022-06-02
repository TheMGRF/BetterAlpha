package net.minecraft.server.world.chunk;

public class NibbleArray {

    public final byte[] data;

    public NibbleArray(int i) {
        this.data = new byte[i >> 1];
    }

    public NibbleArray(byte[] abyte) {
        this.data = abyte;
    }

    public int a(int i, int j, int k) {
        int l = i << 11 | k << 7 | j;
        int i1 = l >> 1;
        int j1 = l & 1;

        return j1 == 0 ? this.data[i1] & 15 : this.data[i1] >> 4 & 15;
    }

    public void a(int i, int j, int k, int l) {
        int i1 = i << 11 | k << 7 | j;
        int j1 = i1 >> 1;
        int k1 = i1 & 1;

        if (k1 == 0) {
            this.data[j1] = (byte) (this.data[j1] & 240 | l & 15);
        } else {
            this.data[j1] = (byte) (this.data[j1] & 15 | (l & 15) << 4);
        }
    }

    public boolean hasData() {
        return this.data != null;
    }
}
