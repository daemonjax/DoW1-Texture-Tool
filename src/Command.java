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
enum Command
{
    MULTIPLY("-mul"), SET("-set"), INFO(Target.INFO.text);
    private static final int COMMAND_MIN_LENGTH = 5;
    final String text;

    private Command(String text) { this.text = text; }

    final boolean isValidFor(final FileType f)
    {
        final Command[] validCommands = f.validCommands;
        final int length = validCommands.length;

        for (int i = 0; i < length; ++i) { if (this == validCommands[i]) return true; }
        return false;
    }

    static final Command[] VALUES = Command.values();

    static final Command get(final String s)
    {
        if (s.length() >= COMMAND_MIN_LENGTH)
        {
            final String s1 = s.substring(0, COMMAND_MIN_LENGTH);

            if      (s1.equalsIgnoreCase(MULTIPLY.text + Strings.EQUAL_SIGN)) { return MULTIPLY; }
            else if (s1.equalsIgnoreCase(SET.text      + Strings.EQUAL_SIGN)) { return SET;      }
            else if (s1.equalsIgnoreCase(INFO.text                         )) { return INFO;     }
        }
        return (Command)Error.COMMAND_GET.exit(new Exception());
    }

    final String getExample()
    {
        switch (this)
        {
            case MULTIPLY: return Strings.COMMAND_EXAMPLE_MULTIPLY;
            case SET     : return Strings.COMMAND_EXAMPLE_SET;
            case INFO    : return Strings.EMPTY;
        }
        return (String)Error.PROGRAMMER_ENUM.exit(new Exception());
    }

    final float getValueFromArg(String[] args)
    {
        switch (this)
        {
            case MULTIPLY:
            case SET:
            {
                final String arg = args[DoW1TextureTool.Arg.COMMAND.ordinal()];
                try
                {
                    return Float.valueOf(arg.substring(arg.indexOf('=') + 1).strip());
                } catch (Exception e) { return (float)Error.PARSE_COMMAND.exit(e, arg); }
            }
            case INFO: return 1.0f;
        }
        return (float)Error.PROGRAMMER_ENUM.exit(new Exception());
    }
}
