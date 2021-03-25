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
final class DDS // DDS file reference: http://doc.51windows.net/directx9_sdk/graphics/reference/DDSFileReference/ddsfileformat.htm
{
        byte[] FILETYPE_MAGIC_WORD = {0x44, 0x44, 0x53, 0x20};
        final int STARTING_OFFSET = 0;
        
        enum HEADER 
        {
            MAGIC_WORD(0), HEIGHT(12), WIDTH(16), MIPMAPCOUNT(24), DXT_TYPE(84), ARGB_BIT_COUNT(90);
            final static int ALL_SIZES = 4;
            final int relativeOffset;
            
            private HEADER(final int relativeOffset) {this.relativeOffset = relativeOffset;}
        }
        
}
