import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author daemonjax
 */
final class Utils
{
    private static final String FORMAT = "%02x ";


    private Utils() {}

    static final int getBEintFromLEbytes(byte[] array, final int startIndex)
    {
        return  (array[startIndex    ] & 0xFF)        |
               ((array[startIndex + 1] & 0xFF) <<  8) |
               ((array[startIndex + 2] & 0xFF) << 16) |
               ((array[startIndex + 3] & 0xFF) << 24) ;
    }

    static final float getBEfloatFromLEbytes(final byte[] array, final int startIndex)
    {
        return ByteBuffer.wrap(array, startIndex, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

    static final byte[] getLEbytesFromBEfloat(final float f)
    {
        final int temp = Float.floatToRawIntBits(f);
        return new byte[] { (byte)((temp       ) & 0xFF), (byte)((temp >>>  8) & 0xFF),
                            (byte)((temp >>> 16) & 0xFF), (byte)((temp >>> 24) & 0xFF) };
    }

    static final byte[] getBytesFromChars(final char[] array)
    {
        final int length = array.length;
        final byte[] result = new byte[length];

        for (int i = 0; i < length; ++i) { result[i] = (byte)array[i]; }
        return result;
    }

    /*static final String getFormattedStringFromBytes(final byte[] array, final int startIndex)
    {
        return  String.format(FORMAT, array[startIndex    ]) +
                String.format(FORMAT, array[startIndex + 1]) +
                String.format(FORMAT, array[startIndex + 2]) +
                String.format(FORMAT, array[startIndex + 3]);
    }*/
    

    static final int indexOf(final byte[] pattern, final byte[] data, final int startIndex)  // naive implementation, but guaranteed to work
    {
        final int patternLength = pattern.length;

        if (patternLength > 0)
        {
            final int endIndex = data.length - patternLength;

            if (endIndex >= startIndex)
            {
                for (int i = startIndex, j; i <= endIndex; ++i)
                {
                    for (j = 0; j < patternLength; ++j)
                    {
                        if (pattern[j] != data[i + j]) break;
                    }
                    if (j != patternLength) continue;
                    return i;
                }
            }
        }

        return -1;
    }
}

/*static final int indexOf_kmp(final byte[] pattern, final byte[] data, final int startIndex) // Uses KMP Algorithm
{
    final int patternLength = pattern.length;
    final int dataLength = data.length;
    if (patternLength == 0 || patternLength > dataLength) return -1;
    final int[] lps = new int[patternLength];

    for (int i = 1, j = 0; i < patternLength; ++i)
    {
        while (j > 0 && pattern[j] != pattern[i]) j = lps[j - 1];
        if (pattern[j] == pattern[i]) ++j;
        lps[i] = j;
    }

    for (int i = startIndex, j = 0; i < dataLength; ++i)
    {
        while (j > 0 && pattern[j] != data[i]) j = lps[j - 1];
        if (pattern[j] == data[i]) ++j;
        if (j == patternLength) return i - patternLength + 1;
    }
    return -1;
}*/
