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
enum Error
{
    CONFIG_FILE_BAD("Unable to read config file " + Strings.CONFIG_FILENAME + ".  Using " + Strings.CONFIG_FILENAME_DEFAULT + " instead"),
    CONFIG_FILE_DEFAULT_BAD("Unable to read the default config file " + Strings.CONFIG_FILENAME_DEFAULT + ": Either it doesn't exist, it's not a file, it can't be read, or it's empty"),
    CONFIG_FILEREAD("Unable to read the configuration .ini file.  Unknown reason"),
    CONFIG_BAD("Config file does not contain the expected number of settings (" + Config.Keys.VALUES.length +
               ").\nTake a look at the default config file to help figure out what's wrong, or just\n" +
               "delete both the .ini file and .default file and run again to recreate the .default file"),
    CONFIG_WRITING_DEFAULTS("Unable to create a new " + Strings.CONFIG_FILENAME_DEFAULT +" file.  Unknown reason"),
    ARGSINVALID("While processing arguements"),
    ARG_LIST_DNE("The arguement list file does not exist"),
    ARG_LIST_BAD("The arguement list file structure is bad"),
    PROCESS_FILE("Unable to open the file.  Either it doesn't exist, it's not a file, it can't be accessed, or it contains 0 bytes"),
    PROCESS_FILES("Unable to open a file in the folder.  Either it can't be accessed, or it contains 0 bytes"),
    EMPTY_FOLDER("The folder provided does not contain any map files"),
    FILEREAD("Unable to read the file.  Unknown reason"),
    FILETYPE_GET("While getting filetype"),
    PROCESS_TARGET("Invalid target"),
    PROCESS_COMMAND("While processing command"),
    COMMAND_INVALID("You used an invalid command for the filetype detected.  For example, the -set command can only be used on .WHE model files"),
    PROGRAMMER_NODE_UNIQUE_ID("!!!Programmer Error!!!  You can't get the relative offset of this node's UNIQUE_ID this way because you have to know the value at NUM_PATH_CHAR"),
    PROGRAMMER_ENUM("!!!Programmer Error!!!  Didn't list all enum types in switch statement."),
    COMMAND_GET("While getting command from arguements string"),
    UNIQUE_ID_NOT_FOUND("While looking for the unique ID in the DATASMAP section of the map file.  The target decal name does not exist in this map"),
    PARSE_COMMAND("While parsing the number for this command.  It doesn't seem to be a proper number"),
    BAD_OPTION("Bad options -- The letter is not a valid option or the option string doesn't start with a '-' character"),
    SAVEFILE("While saving file "),
    NOT_IMPLEMENTED("Attempting to use an unimplemented feature"),
    NOT_AN_ERROR(Strings.EMPTY);

    private final String message;

    private Error(String message) { this.message = message; }


    final Object exit()
    {
        Utils.sb.append(Strings.NEWLINE).append(Strings.FATAL_ERROR).append(this.message).append(Strings.NEWLINE).append(Strings.USAGE).append(Strings.NEWLINE).append(Strings.FULL_EXAMPLES);
        System.out.println(Utils.sb.toString());
        System.exit(1);
        /*UNREACHABLE*/ return null;
    }

    final Object exit(Exception e)
    {
        Utils.sb.append(Strings.NEWLINE).append(Strings.FATAL_ERROR).append(e.getStackTrace()[0].getMethodName()).append(Strings.NEWLINE);
        return exit();
    }

    final Object exit(final String message)
    {
        Utils.sb.append(Strings.NEWLINE).append(Strings.FATAL_ERROR).append(message).append(Strings.NEWLINE);
        return exit();
    }

    final Object exit(Exception e, final String message)
    {
        Utils.sb.append(Strings.NEWLINE).append(Strings.FATAL_ERROR).append(e.getStackTrace()[0].getMethodName());
        return exit(message);
    }

    final void warn()
    {
        Utils.sb.append(Strings.NEWLINE).append(Strings.WARNING).append(message).append(Strings.NEWLINE);
    }

    final void cleanExit()
    {
        Utils.sb.append(Strings.NEWLINE).append(this.message).append(Strings.USAGE).append(Strings.NEWLINE).append(Strings.FULL_EXAMPLES);
        System.out.println(Utils.sb.toString());
        System.exit(0);
    }
}
