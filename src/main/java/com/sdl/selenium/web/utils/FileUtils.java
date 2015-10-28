package com.sdl.selenium.web.utils;

import com.sdl.selenium.utils.config.WebDriverConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class FileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private static final Pattern NEW_LINE_PATTERN = Pattern.compile("\\r\\n|\\r|\\n");

    public static String getValidFileName(String fileName) {
        String regex = "\\\\|:|/|\\*|\\?|\\<|\\>|\\|"; // matches special characters: ,(comma) (space)&><@?\/'"
        fileName = fileName.replaceAll(regex, "_");
        fileName = fileName.replaceAll(";", "_"); // replace semicolon only after replacing characters like &amp, &gt etc
        return fileName;
    }

    public static boolean waitFileIfIsEmpty(File file, long millis) {
        boolean isNotEmpty;
        do {
            isNotEmpty = file.length() > 0;
            if (!isNotEmpty) {
                LOGGER.debug("File exist: '" + file.exists() + "' and content file is empty in: " + millis);
                Utils.sleep(100);
            }
            millis -= 100;
        } while (!isNotEmpty && millis > 0);
        return isNotEmpty;
    }

    public static String getTextFromFile(String pathFile) {
        String strLine = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(pathFile), "UTF8"));
            String tmp;
            while ((tmp = br.readLine()) != null) {
                strLine += tmp;
            }
            LOGGER.debug("length {}", strLine.length());
            br.close();
        } catch (Exception e) {
            LOGGER.debug("Error: {}", e.getMessage());
        }
        return strLine;
    }

    public static final void copyInputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;

        while ((len = in.read(buffer)) >= 0)
            out.write(buffer, 0, len);

        in.close();
        out.close();
    }

    /**
     * @param zipFilePath      zipFilePath
     * @param outputFolderPath if null of empty will extract in same folder as zipFilePath
     * @return True | False
     */
    public static boolean unZip(String zipFilePath, String outputFolderPath) {
        byte[] buffer = new byte[1024];
        try {
            long startMs = System.currentTimeMillis();
            //create output directory if doesn't exists
            if (outputFolderPath == null || "".equals(outputFolderPath)) {
                // unzip in same folder as zip file
                outputFolderPath = new File(zipFilePath).getParent();
            }
            File folder = new File(outputFolderPath);
            if (!folder.exists()) {
                folder.mkdir();
            }

            //get the zip file content
            ZipInputStream zis =
                    new ZipInputStream(new FileInputStream(zipFilePath));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {

                String fileName = ze.getName();
                File newFile = new File(outputFolderPath + File.separator + fileName);

                LOGGER.info("file unzip : " + newFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            LOGGER.info("Unzip Done: " + zipFilePath);
            long endMs = System.currentTimeMillis();
            LOGGER.debug(String.format("unzip took %s ms", endMs - startMs));
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return (file.delete());

    }

    public static boolean compareFiles(String file1Path, String file2Path) {
        try {
            File file1 = new File(file1Path);
            File file2 = new File(file2Path);
            FileUtils.waitFileIfIsEmpty(file1, 10000);
            FileUtils.waitFileIfIsEmpty(file2, 10000);
            String str1 = convertStreamToString(new FileInputStream(file1));
            String str2 = convertStreamToString(new FileInputStream(file2));
            assertThat("Strings are not same", formatToSystemLineSeparator(str1), equalTo(formatToSystemLineSeparator(str2)));
            return formatToSystemLineSeparator(str1).equals(formatToSystemLineSeparator(str2));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String formatToSystemLineSeparator(String input) {
        return NEW_LINE_PATTERN.matcher(input).replaceAll(Matcher.quoteReplacement(System.getProperty("line.separator")));
    }

    public static String convertStreamToString(InputStream is) {
        if (is != null) {
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
                reader.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
            return writer.toString();
        } else {
            return "";
        }
    }

    public static boolean compareFileSize(String filePath1, String filePath2) {
        boolean equal = false;
        File file1 = new File(filePath1);
        File file2 = new File(filePath2);
        LOGGER.info("file1.length = " + file1.length());
        LOGGER.info("file2.length = " + file2.length());
        if (file1.length() == file2.length()) {
            equal = true;
        }
        return equal;
    }

    public static String obtainFileName(String fileName, String insertedText) {
        return obtainFileName(fileName, insertedText, null);
    }

    public static String obtainFileName(String fileName, String insertedText, String extension) {

        String[] pathSplit = fileName.split("\\\\");
        String[] splitted = pathSplit[pathSplit.length - 1].split("\\.");
        String result = "";
        for (int i = 0; i < (splitted.length - 1); i++) {
            result += splitted[i];
        }
        result += insertedText + "." + (extension == null ? splitted[splitted.length - 1] : extension);
        return result;
    }

    public static String getFileNameFromPath(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }

    public static void cleanDownloadDir() {
        try {
            org.apache.commons.io.FileUtils.cleanDirectory(new File(WebDriverConfig.getDownloadPath()));
            LOGGER.debug("Clean download directory with success.");
        } catch (IOException e) {
            LOGGER.debug("Clean Download Dir with error {}", e.getMessage());
        }
    }
}