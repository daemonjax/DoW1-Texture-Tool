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
    MULTIPLY(Strings.COMMAND_MULTIPLY_TEXT), SET(Strings.COMMAND_SET_TEXT), REPLACE_ALL(Strings.COMMAND_REPLACE_TEXT), INFO(Strings.TARGET_INFO_TEXT);
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

    private static final Command get(final String s)
    {
        final int length = s.length();

        if (length >= COMMAND_MIN_LENGTH)
        {
            if (Command.MULTIPLY.compare(s, length)) return MULTIPLY;
            if (Command.SET.compare(s, length)) return SET;
            if (Command.REPLACE_ALL.compare(s, length)) return REPLACE_ALL;
            if (s.equals(INFO.text)) return INFO;
        }
        return (Command)Error.COMMAND_NOT_RECOGNIZED.exit(new Exception());
    }

    private final boolean compare(final String s, final int length)
    {
        return (s.startsWith(this.text + Strings.EQUAL_SIGN) && (length > this.text.length() + 2));
    }

    static final Command process(final String[] args, final FileType fileType, final Target target, final boolean doOutput)
    {
        final Command result;
        final String s;
        final int ordinal = DoW1TextureTool.Arg.COMMAND.ordinal();

        if (target == Target.INFO) return Command.INFO;

        if (args[ordinal] != null)
        {
            s = args[ordinal] = args[ordinal].replace('\\', '/');
            if (doOutput) Utils.sb.append(Strings.COMMAND).append(s).append(Strings.NEWLINE);
            result = Command.get(s.toLowerCase());
            if (result.isValidFor(fileType)) return result;
            Error.COMMAND_INVALID.exit(new Exception(), fileType.name());
        }

        return (Command)Error.PROCESS_COMMAND.exit(new Exception());
    }

    final String getExample()
    {
        switch (this)
        {
            case MULTIPLY   : return Strings.COMMAND_EXAMPLE_MULTIPLY;
            case SET        : return Strings.COMMAND_EXAMPLE_SET;
            case REPLACE_ALL: return Strings.COMMAND_EXAMPLE_REPLACE_ALL;
            case INFO       : return Strings.EMPTY;
        }
        return (String)Error.PROGRAMMER_ENUM.exit(new Exception());
    }

    final float getFloatValueFromArg(final String[] args)
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

    static final String getStringValueFromArg(final String[] args)
    {
        final int ordinal = DoW1TextureTool.Arg.COMMAND.ordinal();
        return args[ordinal].substring(args[ordinal].indexOf('=') + 1);
    }

}
