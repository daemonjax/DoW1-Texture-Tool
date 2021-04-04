import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;

/*  DoW1 Texture Tool
    Copyright (C) 2021  Daemonjax

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/gpl-3.0.html>.
*/

/**
 * @author daemonjax
 */
final class Utils
{
    static final StringBuilder sb = new StringBuilder(8192);

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

    static final File getNotEmptyFile(final String fileName)
    {
        final File result = new File(fileName);

        if (result.canRead() && result.isFile() && result.length() > 0) return result;
        return null;
    }

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

    static final void displayTimer(final Instant start, final Instant stop)
    {
        final Duration duration = Duration.between(start, stop);
        if (duration.getSeconds() == 0) System.out.println("Time elapsed: " + (new DecimalFormat("###,###,###")).format(duration.getNano()) + " nanoseconds.\n");
        else System.out.println("Time elapsed: " + duration.getSeconds() + " seconds, and " + (new DecimalFormat("###,###,###")).format(duration.getNano()) + " nanoseconds.\n");
    }

    static final void save(final File file, final byte[] data, final String extension, final boolean overwrite)
    {
        File saveFile = null;

        if (overwrite) saveFile = file;
        else
        {
            for (int j = 1; j < 100; ++j, saveFile = null)
            {
                saveFile = new File(file.getPath() + extension + String.format(Strings.SAVE_FORMAT, j));
                if (!saveFile.exists()) break;
            }
        }

        if (saveFile != null) Utils.sb.append(Strings.INDENT).append(Strings.SAVING_FILE).append(saveFile.getPath()).append(Strings.NEWLINE);
        else { Error.SAVEFILE.exit(new Exception(), file.getName()); /*UNREACHABLE*/ return; }
        try { Files.write(saveFile.toPath(), data); } catch (Exception e) { Error.SAVEFILE.exit(e, saveFile.getName()); }
    }

    static final void outputAllMessages(boolean logToFile)
    {
        if (logToFile)
        {
            System.out.println(Strings.LOG_OUTPUT);
            save(new File(Strings.LOGFILE), sb.toString().getBytes(StandardCharsets.UTF_8), Strings.EMPTY, true);
        }
        else
        {
            System.out.println(sb.toString());
        }
    }

    /*static final String getFormattedStringFromBytes(final byte[] array, final int startIndex)
    {
        return  String.format(FORMAT, array[startIndex    ]) +
                String.format(FORMAT, array[startIndex + 1]) +
                String.format(FORMAT, array[startIndex + 2]) +
                String.format(FORMAT, array[startIndex + 3]);
    }*/
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
