/**
 * @author daemonjax
 */
enum Error
{
    ARGSINVALID("Fatal error while processing arguements."),
    PROCESS_FILE("\nError opening file.  Either it doesn't exist, it's not a file, it can't be accessed, or it contains 0 bytes."),
    FILEREAD("\nError reading file.  Unknown reason."),
    FILENAMELENGTH("\nError inspectigin file.  Filename length too short.  It needs to be at least " + FileType.MIN_FILENAME_LENGTH + " characters long in total."),
    FILETYPE_GET("Fatal Error while getting filetype."),
    PROCESS_TARGET("Fatal Error: Invalid target."),
    PROCESS_COMMAND("Fatal error while processing command."),
    COMMAND_INVALID("You used an invalid command for the filetype detected.  For example, the -set command can only be used on .WHE model files."),
    NODE_UNIQUE_ID("Programmer Error: You can't get the relative offset of this node's UNIQUE_ID this way because you have to know the value at NUM_PATH_CHAR."),
    COMMAND_GET("Fatal error while getting command from arguements string."),
    UNIQUE_ID_NOT_FOUND("Fatal error while looking for the unique ID in the DATASMAP section of the map file.  The target decal name does not exist in this map."),
    COMMAND_MULTIPLY("Fatal error while parsing the number for the multiplier.  It doesn't seem to be a proper number."),
    SAVEFILE("Fatal error while saving file.");

    private final String message;

    private Error(String message) { this.message = message; }


    final void exit()
    {
        System.out.println(this.message + Strings.USAGE + Strings.NEWLINE + Strings.FULL_EXAMPLES);
        System.exit(1);
    }

    final void exit(final String s)
    {
        System.out.println(this.message + Strings.NEWLINE + s + Strings.NEWLINE + Strings.USAGE + Strings.NEWLINE + Strings.FULL_EXAMPLES);
        System.exit(1);
    }
}
