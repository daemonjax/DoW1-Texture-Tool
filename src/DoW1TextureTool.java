import java.io.File;
import java.nio.file.Files;

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
 * @author Daemonjax
 */
final class DoW1TextureTool
{
    static final int ARG_FILENAME = 0;
    static final int ARG_TARGET = 1;
    static final int ARG_COMMAND = 2;

    private DoW1TextureTool(){}


    public static final void main(final String[] args)
    {
        final File f = processFile(args);
        final FileType fileType = FileType.get(f);
        final Target target = processTarget(args);
        final Command command = processCommand(args, fileType, target);
        final byte[] fileBytes; try { fileBytes = Files.readAllBytes(f.toPath()); } catch (Exception e) { Error.FILEREAD.exit(); /*UNCREACHABLE*/ return; }

        switch (fileType)
        {
            case MAP_SGB ->
            {
                System.out.println(Strings.MAP_SGB_FILE);
                String message;
                int offset = DataSMap.findStartingOffset(fileBytes, 0);
                int numNodes = DataSMap.getNumNodes(fileBytes, offset);
                offset += DataSMap.Header.TOTAL_SIZE;

                if (target == Target.INFO)
                {
                    System.out.println(Strings.TARGET_INFO);

                    for (int i = 0, thisDecalPathCharsLength; i < numNodes; ++i)
                    {
                        thisDecalPathCharsLength = Utils.getBEintFromLEbytes(fileBytes, offset);
                        System.out.println(DataSMap.getDecalFilename(fileBytes, offset));
                        offset = DataSMap.nextDecalTypesNodeOffset(offset, thisDecalPathCharsLength);
                    }
                }
                else if (command == Command.MULTIPLY || command == Command.INFO)
                {
                    final byte[] uniqueID = new byte[4];
                    final float multiplier;
                    if (command == Command.MULTIPLY) try { multiplier = Float.valueOf(args[ARG_COMMAND].substring(args[ARG_COMMAND].indexOf('=') + 1)); } catch (Exception e) { Error.COMMAND_MULTIPLY.exit(); /*UNCREACHABLE*/ return; }
                    else multiplier = 1.0f;

                    final byte[] targetDecalPathChars = Utils.getBytesFromChars(args[ARG_TARGET].substring(args[ARG_TARGET].indexOf('=') + 1).toCharArray());
                    final int targetDecalPathCharsLength = targetDecalPathChars.length;
                    offset = getUniqueIDOffsetFromPathChars(fileBytes, targetDecalPathChars, targetDecalPathCharsLength, numNodes, offset);
                    uniqueID[0] = fileBytes[offset    ]; uniqueID[1] = fileBytes[offset + 1];
                    uniqueID[2] = fileBytes[offset + 2]; uniqueID[3] = fileBytes[offset + 3];
                    System.out.println(Strings.FOUND_UNIQUE_ID); //+ Utils.getFormattedStringFromBytes(uniqueID, 0));

                    offset = DataEnty.findStartingOffset(fileBytes, offset + DataSMap.Node.NUM_BYTES_UNIQUE_ID);                    
                    numNodes = DataEnty.getNumNodes(fileBytes, offset);                    
                    offset += DataEnty.Header.TOTAL_SIZE + DataEnty.Node.UNIQUE_ID.relativeOffset;

                    int counter = 0;
                    float originalSize;

                    if (command == Command.MULTIPLY) message = Strings.MULTIPLY_MESSAGE_1 + multiplier;
                    else message = Strings.INFO_MESSAGE_1;
                    System.out.println(message);

                    for (int i = 0, decalSizeOffset; i < numNodes; ++i, offset += DataEnty.Node.TOTAL_SIZE)
                    {
                        if (uniqueID[0] == fileBytes[offset    ] && uniqueID[1] == fileBytes[offset + 1] &&
                            uniqueID[2] == fileBytes[offset + 2] && uniqueID[3] == fileBytes[offset + 3])
                        {
                            ++counter;
                            decalSizeOffset = offset + (DataEnty.Node.DECAL_SIZE.relativeOffset - DataEnty.Node.UNIQUE_ID.relativeOffset);
                            originalSize = Utils.getBEfloatFromLEbytes(fileBytes, decalSizeOffset);

                            if (command == Command.MULTIPLY) DataEnty.setDecalSize(fileBytes, decalSizeOffset, originalSize * multiplier);
                            else System.out.println(originalSize);
                        }
                    }

                    if (command == Command.MULTIPLY) message = Strings.MULTIPLY_MESSAGE_2;
                    else message = Strings.INFO_MESSAGE_2;
                    System.out.println(message + counter);
                }
            }
            case MODEL_WHE ->
            {
                System.out.println(Strings.MODEL_WHE_FILE);
            }
        }

        System.out.print(Strings.ALL_DONE);
        if (target != Target.INFO && command != Command.INFO)
        {         
            File savefile = null;

            for (int i = 1; i < 100; ++i, savefile = null)
            {
                savefile = new File(f.getPath() + Strings.EXTENSION + String.format(Strings.SAVE_FORMAT, i));
                if (!savefile.exists()) break;
            }

            if (savefile != null) System.out.println(Strings.SAVING_FILE + savefile.getPath());
            else Error.SAVEFILE.exit();
            try { Files.write(savefile.toPath(), fileBytes); } catch (Exception e) { Error.SAVEFILE.exit(); return; }
        }
        else System.out.println(Strings.INFO_DONE);
    }

    static final File processFile(final String[] args)
    {
        final File result;
        final String filename;

        if (args.length > ARG_FILENAME)
        {
            filename = args[ARG_FILENAME];
            System.out.println(Strings.FILE + filename);

            result = new File(filename);
            if (result.exists() && result.isFile() && result.canRead() && result.length() > 0) return result;
            Error.PROCESS_FILE.exit();
        }
        if (args.length == 0) Error.NOT_AN_ERROR.cleanExit();
        Error.ARGSINVALID.exit(); /*UNREACHABLE*/ return null;
    }

    static final Target processTarget(final String[] args)
    {
        final String s;

        if (args.length > ARG_TARGET)
        {
            s = args[ARG_TARGET];
            System.out.println(Strings.TARGET + s);
            return Target.get(s);
        }

        Error.PROCESS_TARGET.exit(); /*UNREACHABLE*/ return null;
    }

    static final Command processCommand(final String[] args, final FileType fileType, final Target target)
    {
        final Command result;
        final String s;

        if (args.length > ARG_COMMAND)
        {
            s = args[ARG_COMMAND];
            System.out.println(Strings.COMMAND + s);
            result = Command.get(s);
            if (result.isValidFor(fileType)) return result;
            Error.COMMAND_INVALID.exit();
        }

        if (target == Target.INFO) return Command.INFO;

        Error.PROCESS_COMMAND.exit(); /*UNCREACHABLE*/ return null;
    }

    static final int getUniqueIDOffsetFromPathChars(final byte[] fileBytes, final byte[] targetDecalPathChars, final int targetDecalPathCharsLength, final int numDecalTypes, int dataSMapNodeOffset)
    {
        for (int i = 0, thisDecalPathCharsLength; i < numDecalTypes; ++i, dataSMapNodeOffset = DataSMap.nextDecalTypesNodeOffset(dataSMapNodeOffset, thisDecalPathCharsLength))
        {
            thisDecalPathCharsLength = Utils.getBEintFromLEbytes(fileBytes, dataSMapNodeOffset + DataSMap.Node.DECAL_PATH_CHARS_LENGTH.relativeOffset);

            if (targetDecalPathCharsLength != thisDecalPathCharsLength) continue;
            if (!areDecalPathCharsEqual(targetDecalPathChars, targetDecalPathCharsLength, fileBytes, dataSMapNodeOffset + DataSMap.Node.DECAL_PATH_CHARS.relativeOffset)) continue;

            return dataSMapNodeOffset + DataSMap.Node.DECAL_PATH_CHARS.relativeOffset + targetDecalPathCharsLength;
        }
        Error.UNIQUE_ID_NOT_FOUND.exit(); /*UNCREACHABLE*/ return -1;
    }

    static final boolean areDecalPathCharsEqual(final byte[] pattern, final int patternlength, final byte[] mapFileBytes, final int mapFileBytesOffset)
    {
        for (int i = 0; i < patternlength; ++i) { if (pattern[i] != mapFileBytes[mapFileBytesOffset + i]) return false; }
        return true;
    }

}
