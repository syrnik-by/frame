package ru.autotestframework.core;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import lombok.SneakyThrows;

/**
 * File utils.
 */
public class FileUtils {
    private static final int BUFFER_SIZE = 4096;
    private static final int THRESHOLD_ENTRIES = 10000;
    private static final int THRESHOLD_SIZE = 1000000000; // 1 GB
    private static final double THRESHOLD_RATIO = 10;

    /**
     * Unzip.
     *
     * @param zipFilePath   the zip file path
     * @param destDirectory the dest directory
     */
    @SneakyThrows
    public static void unzip(String zipFilePath, String destDirectory) {
        var totalEntryArchive = 0;
        try (var zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {

            var destDir = new File(destDirectory);
            if (!destDir.exists()) {
                destDir.mkdir();
            }
            var entry = zipIn.getNextEntry();
            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                long compressedSize = entry.getCompressedSize();
                if (!entry.isDirectory()) {
                    extractFile(zipIn, filePath, compressedSize);
                    totalEntryArchive++;

                } else {
                    var dir = new File(filePath);
                    dir.mkdirs();
                }
                zipIn.closeEntry();
                if (totalEntryArchive > THRESHOLD_ENTRIES) {
                    // too many entries in this archive, can lead to inodes exhaustion of the system
                    break;
                }
                entry = zipIn.getNextEntry();
            }
        }
    }

    @SneakyThrows
    private static void extractFile(ZipInputStream zipIn, String filePath, double compressedSize) throws IOException {
        var totalSizeArchive = 0;
        try (var bos = new BufferedOutputStream(new FileOutputStream(filePath))) {

            var totalSizeEntry = 0;
            var bytesIn = new byte[BUFFER_SIZE];
            var read = 0;
            while ((read = zipIn.read(bytesIn)) != -1) {
                totalSizeEntry += read;
                totalSizeArchive += read;
                double compressionRatio = totalSizeEntry / compressedSize;
                if ((compressionRatio > THRESHOLD_RATIO) || (totalSizeArchive > THRESHOLD_SIZE)) {
                    // ratio between compressed and uncompressed data is highly suspicious, looks like a Zip Bomb Attack
                    break;
                }
                bos.write(bytesIn, 0, read);
            }
        }
    }

    /**
     * Create zip and get zip path string.
     *
     * @param sourceFile the source file
     * @param zipPath    the zip path
     * @return the string
     */
    @SneakyThrows
    public static String createZipAndGetZipPath(String sourceFile, String zipPath) {
        var fos = new FileOutputStream(zipPath);
        var zipOut = new ZipOutputStream(fos);

        var fileToZip = new File(sourceFile);
        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
        return zipPath;
    }

    /**
     * Zip file.
     *
     * @param fileToZip the file to zip
     * @param fileName  the file name
     * @param zipOut    the zip out
     */
    @SneakyThrows
    public static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        try (var fis = new FileInputStream(fileToZip)) {
            var zipEntry = new ZipEntry(fileName);
            zipOut.putNextEntry(zipEntry);
            var bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
        }
    }
}
