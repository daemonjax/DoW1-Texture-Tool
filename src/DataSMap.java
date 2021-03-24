import java.nio.charset.StandardCharsets;

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
final class DataSMap
{
    private static final byte[] START_PATTERN = { (byte)0x44, (byte)0x41, (byte)0x54, (byte)0x41,
                                                  (byte)0x53, (byte)0x4D, (byte)0x41, (byte)0x50 };

    private DataSMap(){}

    enum Header
    {
        SECTION_NAME(0), UNKNOWN_1(8), UNKNOWN_2(12), UNKNOWN_3(16), NUM_NODES(20);
        static final int NUM_BYTES_SECTION_NAME = 8;
        static final int NUM_BYTES_OTHERS = 4;
        static final Header[] VALUES = Header.values();
        static final int TOTAL_SIZE = 24;
        final int relativeOffset;

        private Header(final int relativeOffset){ this.relativeOffset = relativeOffset; }


        static final int getNumNodes(final byte[] mapFileBytes, final int headerOffset)
        {
            return Utils.getBEintFromLEbytes(mapFileBytes, NUM_NODES.relativeOffset + headerOffset);
        }
    }

    enum Node
    {
        DECAL_PATH_CHARS_LENGTH(0), DECAL_PATH_CHARS(4), UNIQUE_ID(Integer.MAX_VALUE); //UNIQUE_ID relativeOfffset must be calculated based on value at NUM_PATH_CHARS//UNIQUE_ID relativeOfffset must be calculated based on value at NUM_PATH_CHARS
        static final int NUM_BYTES_DECAL_PATH_CHARS_LENGTH = 4;
        static final int NUM_BYTES_UNIQUE_ID = 4;
        final int relativeOffset;
        static final Node[] VALUES = Node.values();

        private Node(final int relativeOffset){ this.relativeOffset = relativeOffset; }
    }


    static final int findStartingOffset(final byte[] mapFileBytes, final int startIndex)
    {
        return Utils.indexOf(START_PATTERN, mapFileBytes, startIndex);
    }

    static final int getNumNodes(final byte[] mapFileBytes, final int startingOffset)
    {
        return Utils.getBEintFromLEbytes(mapFileBytes, startingOffset + Header.NUM_NODES.relativeOffset);
    }

    static final int nextDecalTypesNodeOffset(final int thisNodeOffset, final int curPathLength)
    {
        return Node.DECAL_PATH_CHARS.relativeOffset + Header.NUM_BYTES_OTHERS + curPathLength + thisNodeOffset;
    }

    static final int getDecalPathCharsLength(final byte[] mapFileBytes, final int thisNodeOffset)
    {
        return Utils.getBEintFromLEbytes(mapFileBytes, thisNodeOffset);
    }

    static final String getDecalFilename(final byte[] mapFileBytes, final int thisNodeOffset)
    {
        return getDecalPathChars(mapFileBytes, thisNodeOffset, getDecalPathCharsLength(mapFileBytes, thisNodeOffset));
    }

    static final String getDecalPathChars(final byte[] mapFileBytes, final int thisNodeOffset, final int decalPathCharsLength)
    {
        return new String​(mapFileBytes, Node.NUM_BYTES_DECAL_PATH_CHARS_LENGTH + thisNodeOffset, decalPathCharsLength, StandardCharsets.US_ASCII);
    }
}


