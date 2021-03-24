import java.io.File;

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
