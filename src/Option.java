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
 *
 * @author daemonjax
 */
enum Option
{
    OVERWRITE('o'), LOG('l');
    static final Option[] VALUES = values();
    final char text;

    private Option(final char text) { this.text = text; }


    static final int getOptionMask(final String[] args)
    {
        int result = 0;
        final int ordinal = DoW1TextureTool.Arg.OPTIONS.ordinal();

        if (args[ordinal] != null)
        {
            final String optionString = args[ordinal];
            final int optionStringLength = optionString.length();

            if ((optionStringLength > 1) && optionString.charAt(0) == '-')
            {
                final Option[] values = VALUES;
                final int optionValuesLength = values.length;
                char optionChar;
                int j;

                for (int i = 1; i < optionStringLength; ++i)
                {
                    optionChar = optionString.charAt(i);
                    for (j = 0; j < optionValuesLength; ++j)
                    {
                        if (optionChar == values[j].text)
                        {
                            result |= 1 << values[j].ordinal();
                            break;
                        }
                    }

                    if (j == optionValuesLength) return (int)Error.BAD_OPTION.exit(new Exception());
                }
            }
            else return (int)Error.BAD_OPTION.exit(new Exception());

        }

        return result;

    }

    final boolean isSet(final int optionMask)
    {
        return ((optionMask & (1 << this.ordinal())) != 0);
    }

    final String getExample()
    {
        return Strings.EMPTY;
    }
}
