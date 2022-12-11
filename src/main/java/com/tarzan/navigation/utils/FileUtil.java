package com.tarzan.navigation.utils;

import com.tarzan.navigation.common.exception.ForbiddenException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Slf4j
public class FileUtil {

    /**
     * Copies folder.
     *
     * @param source source path must not be null
     * @param target target path must not be null
     */
    public static void copyFolder(@NonNull Path source, @NonNull Path target) throws IOException {
        Assert.notNull(source, "Source path must not be null");
        Assert.notNull(target, "Target path must not be null");

        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                Path current = target.resolve(source.relativize(dir).toString());
                Files.createDirectories(current);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                Files.copy(file, target.resolve(source.relativize(file).toString()),
                        StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Deletes folder recursively.
     *
     * @param deletingPath deleting path must not be null
     */
    public static void deleteFolder(@NonNull Path deletingPath) throws IOException {
        Assert.notNull(deletingPath, "Deleting path must not be null");

        if (Files.notExists(deletingPath)) {
            return;
        }
        log.info("Deleting [{}]", deletingPath);
        delete(deletingPath.toFile());
        log.info("Deleted [{}] successfully", deletingPath);
    }

    private static void delete(File file) {
        if(file.isDirectory()){
            Arrays.asList(file.listFiles()).forEach(e->{
                delete(e);
            });
        }
        file.delete();
    }

    /**
     * Renames file or folder.
     *
     * @param pathToRename file path to rename must not be null
     * @param newName new name must not be null
     */
    public static void rename(@NonNull Path pathToRename, @NonNull String newName)
            throws IOException {
        Assert.notNull(pathToRename, "File path to rename must not be null");
        Assert.notNull(newName, "New name must not be null");

        Path newPath = pathToRename.resolveSibling(newName);
        log.info("Rename [{}] to [{}]", pathToRename, newPath);

        Files.move(pathToRename, newPath);

        log.info("Rename [{}] successfully", pathToRename);
    }



    /**
     * Unzips content to the target path.
     *
     * @param zis zip input stream must not be null
     * @param targetPath target path must not be null and not empty
     * @throws IOException throws when failed to access file to be unzipped
     */
    public static void unzip(@NonNull ZipInputStream zis, @NonNull Path targetPath)
            throws IOException {
        // 1. unzip file to folder
        // 2. return the folder path
        Assert.notNull(zis, "Zip input stream must not be null");
        Assert.notNull(targetPath, "Target path must not be null");

        // Create path if absent
        createIfAbsent(targetPath);

        // Folder must be empty
       ensureEmpty(targetPath);

        ZipEntry zipEntry = zis.getNextEntry();

        while (zipEntry != null) {
            // Resolve the entry path
            Path entryPath = targetPath.resolve(zipEntry.getName());

            // Check directory
            checkDirectoryTraversal(targetPath, entryPath);

            if (zipEntry.isDirectory()) {
                // Create directories
                Files.createDirectories(entryPath);
            } else {
                // Copy file
                Files.copy(zis, entryPath);
            }

            zipEntry = zis.getNextEntry();
        }
    }

    /**
     * Unzips content to the target path.
     *
     * @param bytes zip bytes array must not be null
     * @param targetPath target path must not be null and not empty
     * @throws IOException io exception
     */
    public static void unzip(@NonNull byte[] bytes, @NonNull Path targetPath) throws IOException {
        Assert.notNull(bytes, "Zip bytes must not be null");

        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(bytes));
        unzip(zis, targetPath);
    }

    /**
     * Zips folder or file.
     *
     * @param pathToZip file path to zip must not be null
     * @param pathOfArchive zip file path to archive must not be null
     * @throws IOException throws when failed to access file to be zipped
     */
    public static void zip(@NonNull Path pathToZip, @NonNull Path pathOfArchive)
            throws IOException {
        try (OutputStream outputStream = Files.newOutputStream(pathOfArchive)) {
            try (ZipOutputStream zipOut = new ZipOutputStream(outputStream)) {
                zip(pathToZip, zipOut);
            }
        }
    }

    /**
     * Zips folder or file.
     *
     * @param pathToZip file path to zip must not be null
     * @param zipOut zip output stream must not be null
     * @throws IOException throws when failed to access file to be zipped
     */
    public static void zip(@NonNull Path pathToZip, @NonNull ZipOutputStream zipOut)
            throws IOException {
        // Zip file
        zip(pathToZip, pathToZip.getFileName().toString(), zipOut);
    }

    /**
     * Zips folder or file.
     *
     * @param fileToZip file path to zip must not be null
     * @param fileName file name must not be blank
     * @param zipOut zip output stream must not be null
     * @throws IOException throws when failed to access file to be zipped
     */
    private static void zip(@NonNull Path fileToZip, @NonNull String fileName,
                            @NonNull ZipOutputStream zipOut) throws IOException {
        if (Files.isDirectory(fileToZip)) {
            log.debug("Try to zip folder: [{}]", fileToZip);
            // Append with '/' if missing
            String folderName =
                    StringUtils.appendIfMissing(fileName, File.separator, File.separator);
            // Create zip entry and put into zip output stream
            zipOut.putNextEntry(new ZipEntry(folderName));
            // Close entry for writing the next entry
            zipOut.closeEntry();

            // Iterate the sub files recursively
            try (Stream<Path> subPathStream = Files.list(fileToZip)) {
                // There should not use foreach for stream as internal zip method will throw
                // IOException
                List<Path> subFiles = subPathStream.collect(Collectors.toList());
                for (Path subFileToZip : subFiles) {
                    // Zip children
                    zip(subFileToZip, folderName + subFileToZip.getFileName(), zipOut);
                }
            }
        } else {
            // Open file to be zipped
            // Create zip entry for target file
            ZipEntry zipEntry = new ZipEntry(fileName);
            // Put the entry into zip output stream
            zipOut.putNextEntry(zipEntry);
            // Copy file to zip output stream
            Files.copy(fileToZip, zipOut);
            // Close entry
            zipOut.closeEntry();
        }
    }

    /**
     * Creates directories if absent.
     *
     * @param path path must not be null
     * @throws IOException io exception
     */
    public static void createIfAbsent(@NonNull Path path) throws IOException {
        Assert.notNull(path, "Path must not be null");

        if (Files.notExists(path)) {
            // Create directories
            Files.createDirectories(path);

            log.debug("Created directory: [{}]", path);
        }
    }

    /**
     * The given path must be empty.
     *
     * @param path path must not be null
     * @throws IOException io exception
     */
    public static void ensureEmpty(@NonNull Path path) throws IOException {
        if (!isEmpty(path)) {
            throw new DirectoryNotEmptyException("Target directory: " + path + " was not empty");
        }
    }

    /**
     * Checks directory traversal vulnerability.
     *
     * @param parentPath parent path must not be null.
     * @param pathToCheck path to check must not be null
     */
    public static void checkDirectoryTraversal(@NonNull String parentPath,
                                               @NonNull String pathToCheck) {
        checkDirectoryTraversal(Paths.get(parentPath), Paths.get(pathToCheck));
    }

    /**
     * Checks directory traversal vulnerability.
     *
     * @param parentPath parent path must not be null.
     * @param pathToCheck path to check must not be null
     */
    public static void checkDirectoryTraversal(@NonNull Path parentPath,
                                               @NonNull String pathToCheck) {
        checkDirectoryTraversal(parentPath, Paths.get(pathToCheck));
    }

    /**
     * Checks directory traversal vulnerability.
     *
     * @param parentPath parent path must not be null.
     * @param pathToCheck path to check must not be null
     */
    public static void checkDirectoryTraversal(@NonNull Path parentPath,
                                               @NonNull Path pathToCheck) {
        Assert.notNull(parentPath, "Parent path must not be null");
        Assert.notNull(pathToCheck, "Path to check must not be null");

        if (pathToCheck.normalize().startsWith(parentPath)) {
            return;
        }

        throw new ForbiddenException("你没有权限访问 " + pathToCheck);
    }

    /**
     * Checks if the given path is empty.
     *
     * @param path path must not be null
     * @return true if the given path is empty; false otherwise
     * @throws IOException io exception
     */
    public static boolean isEmpty(@NonNull Path path) throws IOException {
        Assert.notNull(path, "Path must not be null");

        if (!Files.isDirectory(path) || Files.notExists(path)) {
            return true;
        }

        try (Stream<Path> pathStream = Files.list(path)) {
            return pathStream.count() == 0;
        }
    }
    /**
     * Copy the contents of the given InputStream to the given OutputStream.
     * Closes both streams when done.
     * @param in the stream to copy from
     * @param out the stream to copy to
     * @return the number of bytes copied
     * @throws IOException in case of I/O errors
     */
    public static int copy(InputStream in, OutputStream out) throws IOException {
        Assert.notNull(in, "No InputStream specified");
        Assert.notNull(out, "No OutputStream specified");

        try {
            return StreamUtils.copy(in, out);
        }
        finally {
            try {
                in.close();
            }
            catch (IOException ex) {
            }
            try {
                out.close();
            }
            catch (IOException ex) {
            }
        }
    }
    /**
     * Copy the contents of the given byte array to the given OutputStream.
     * Closes the stream when done.
     * @param in the byte array to copy from
     * @param out the OutputStream to copy to
     * @throws IOException in case of I/O errors
     */
    public static void copy(byte[] in, OutputStream out) throws IOException {
        Assert.notNull(in, "No input byte array specified");
        Assert.notNull(out, "No OutputStream specified");

        try {
            out.write(in);
        }
        finally {
            try {
                out.close();
            }
            catch (IOException ex) {
            }
        }
    }


}
