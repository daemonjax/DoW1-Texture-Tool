import java.io.File;

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
enum FileType
{
    MAP_SGB   (new Command[] { Command.INFO, Command.MULTIPLY }),
    MODEL_WHE (new Command[] { Command.INFO, Command.SET      });
    static final int MIN_FILENAME_LENGTH = 4;
    final Command[] validCommands;

    private FileType(final Command[] validCommands) { this.validCommands = validCommands; }


    static final FileType get(final File f)
    {
        final String s = f.getName();
        final int length = s.length();

        if (length >= MIN_FILENAME_LENGTH)
        {
            final String s1 = s.substring(length - MIN_FILENAME_LENGTH, length);

            if (s1.equalsIgnoreCase(".sgb")) return FileType.MAP_SGB;
            if (s1.equalsIgnoreCase(".whw")) return FileType.MODEL_WHE;
        }

        Error.FILETYPE_GET.exit(); return null;
    }
}
