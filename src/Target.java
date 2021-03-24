/**
 * @author daemonjax
 */
enum Target
{
    INFO("-info"), DECAL("-decal");
    static final int MIN_TARGET_LENGTH = 5;
    static final Target[] VALUES = Target.values();
    final String text;

    private Target(String text) { this.text = text; }


    static final Target get(final String s)
    {
        final int length = s.length();

        if (length >= MIN_TARGET_LENGTH)
        {
            if (s.equalsIgnoreCase(INFO.text))  { return INFO;  }
            if (s.startsWith(DECAL.text + Strings.EQUAL_SIGN) && (length > DECAL.text.length() + 1)) { return DECAL; }
        }

        Error.PROCESS_TARGET.exit(); return null;
    }

    final String getExample()
    {
        switch (this)
        {
            case DECAL -> { return Strings.TARGET_EXAMPLE_DECAL; }
            default ->    { return Strings.EMPTY; }
        }
    }
}
