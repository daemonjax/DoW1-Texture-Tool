/**
 * @author daemonjax
 */
public class DataEnty
{
    private static final byte[] START_PATTERN     = { 0x44, 0x41, 0x54, 0x41,
                                                      0x45, 0x4E, 0x54, 0x59 };
    enum Header
    {
        SECTION_NAME(0), UNKNOWN_1(8), UNKNOWN_2(12), UNKNOWN_3(16), NUM_NODES(20);
        static final int TOTAL_SIZE = 24;
        final int relativeOffset;

        private Header(final int relativeOffset) { this.relativeOffset = relativeOffset; }
    }

    enum Node
    {
        COUNTER(0), UNIQUE_ID(4), UNKNOWN_1(8), UNKNOWN_2(12), DECAL_SIZE(16), UNKNOWN_3(20);
        static final int TOTAL_SIZE = 24;
        final        int relativeOffset;

        private Node(final int relativeOffset) { this.relativeOffset = relativeOffset; }
    }


    static final int findStartingOffset(final byte[] mapFileBytes, final int startIndex)
    {
        return Utils.indexOf(START_PATTERN, mapFileBytes, startIndex);
    }

    static final int getNumNodes(final byte[] mapFileBytes, final int startingOffset)
    {
        return Utils.getBEintFromLEbytes(mapFileBytes, Header.NUM_NODES.relativeOffset + startingOffset);
    }

    static final float getDecalSize(final byte[] mapFileBytes, final int thisNodeOffset)
    {
        return Utils.getBEfloatFromLEbytes(mapFileBytes, Node.DECAL_SIZE.relativeOffset + thisNodeOffset);
    }

    static final void setDecalSize(final byte[] mapFileBytes, final int offset, final float newSize)
    {
        final int temp   = Float.floatToRawIntBits(newSize);
        mapFileBytes[offset    ] = (byte)((temp       ) & 0xFF); mapFileBytes[offset + 1] = (byte)((temp >>>  8) & 0xFF);
        mapFileBytes[offset + 2] = (byte)((temp >>> 16) & 0xFF); mapFileBytes[offset + 3] = (byte)((temp >>> 24) & 0xFF);
    }

    static final float validateSetFloat(final float f)
    {

        if (f > 0.05            ) return (float)0.05;
        if (f > 0.04 && f < 0.05) return (float)0.04;
        if (f > 0.03 && f < 0.04) return (float)0.03;

        return (float)0.02;
    }
}
