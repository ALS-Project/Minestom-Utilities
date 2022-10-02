package fr.bretzel.minestom.utils.file;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class FileUtils {

    public static boolean createNewFile(@NotNull File file) {
        if (file.getParentFile() != null && !file.getParentFile().exists())
            file.getParentFile().mkdirs();

        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean writeFile(@NotNull File file, @NotNull String data) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Checks if an input stream is gzipped.
     *
     * @param in the input stream
     * @return true if is zipped
     */
    public static boolean isGZipped(InputStream in) {
        if (!in.markSupported()) {
            in = new BufferedInputStream(in);
        }
        in.mark(2);
        int magic = 0;
        try {
            magic = in.read() & 0xff | ((in.read() << 8) & 0xff00);
            in.reset();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return false;
        }
        return magic == GZIPInputStream.GZIP_MAGIC;
    }

    /**
     * Checks if a file is gzipped.
     *
     * @param f the file
     * @return true if is zipped
     */
    public static boolean isGZipped(File f) {
        int magic = 0;
        try {
            RandomAccessFile raf = new RandomAccessFile(f, "r");
            magic = raf.read() & 0xff | ((raf.read() << 8) & 0xff00);
            raf.close();
        } catch (Throwable e) {
            e.printStackTrace(System.err);
        }
        return magic == GZIPInputStream.GZIP_MAGIC;
    }

    public static File getFilePath() {
        return new File("/");
    }
}
