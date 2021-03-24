
/**
 * @author daemonjax
 */
enum Command
{
    INFO(Target.INFO.text), MULTIPLY("-mul"), SET("-set");
    private static final int COMMAND_MIN_LENGTH = 5;
    final String text;

    private Command(String text) { this.text = text; }

    final boolean isValidFor(final FileType f)
    {
        final Command[] validCommands = f.validCommands;
        final int length = validCommands.length;

        for (int i = 0; i < length; ++i) if (this == validCommands[i]) return true;
        return false;
    }

    static final Command[] VALUES = Command.values();

    static final Command get(final String s)
    {
        final int length = s.length();
        if (length >= COMMAND_MIN_LENGTH)
        {
            final String s1 = s.substring(0, COMMAND_MIN_LENGTH);

            if      (s1.equalsIgnoreCase(INFO.text                         )) { return INFO;     }
            else if (s1.equalsIgnoreCase(MULTIPLY.text + Strings.EQUAL_SIGN)) { return MULTIPLY; }
            else if (s1.equalsIgnoreCase(SET.text      + Strings.EQUAL_SIGN)) { return SET;      }
        }

        Error.COMMAND_GET.exit(); return null;
    }

    final String getExample()
    {
        switch (this)
        {
            case MULTIPLY -> { return Strings.COMMAND_EXAMPLE_MULTIPLY; }
            case SET      -> { return Strings.COMMAND_EXAMPLE_SET; }
            default ->    { return Strings.EMPTY; }
        }
    }
}
