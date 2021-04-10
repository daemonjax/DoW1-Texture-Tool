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
enum Target
{
    DECAL(Strings.TARGET_DECAL_TEXT), LIST(Strings.TARGET_LIST_TEXT), INFO(Strings.TARGET_INFO_TEXT);
    static final int MIN_TARGET_LENGTH = 5;
    static final Target[] VALUES = Target.values();
    final String text;

    private Target(final String text) { this.text = text; }


    private static final Target get(final String s)
    {
        final int length = s.length();

        if (length >= MIN_TARGET_LENGTH)
        {
            if (s.startsWith(DECAL.text + Strings.EQUAL_SIGN) && (length > DECAL.text.length() + 2)) return DECAL;
            if (s.startsWith(LIST.text + Strings.EQUAL_SIGN) && (length > LIST.text.length() + 2)) return LIST;
            if (s.equals(INFO.text)) return INFO;
        }

        return (Target)Error.PROCESS_TARGET.exit(new Exception());
    }

    static final Target process(final String[] args)
    {
        final String s;
        final int ordinal = DoW1TextureTool.Arg.TARGET.ordinal();

        if (args.length > ordinal)
        {
            s = args[ordinal] = args[ordinal].replace('\\', '/');
            Utils.sb.append(Strings.TARGET).append(s).append(Strings.NEWLINE);
            return Target.get(s.toLowerCase());
        }

        return (Target)Error.PROCESS_TARGET.exit(new Exception());
    }

    static final String getStringValueFromArg(final String[] args)
    {
        final int ordinal = DoW1TextureTool.Arg.TARGET.ordinal();
        return args[ordinal].substring(args[ordinal].indexOf('=') + 1);
    }

    final String getExample()
    {
        if (this ==  DECAL) return Strings.TARGET_EXAMPLE_DECAL;
        if (this == LIST) return Strings.TARGET_EXAMPLE_LIST;
        return Strings.EMPTY;
    }
}
