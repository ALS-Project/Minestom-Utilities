package fr.bretzel.minestom.utils.file;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public abstract class FileWatcher extends TimerTask {
    private File file;
    private long lastModifiedFile;

    protected FileWatcher(long period) {
        new Timer().schedule(this, new Date(), period);
    }

    public FileWatcher(File file, long period) {
        this.file = file;
        this.lastModifiedFile = file.lastModified();
        new Timer().schedule(this, new Date(), period);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        this.lastModifiedFile = file.lastModified();
    }

    public long getLastModifiedFile() {
        return lastModifiedFile;
    }

    @Override
    public void run() {
        if (getFile() == null)
            return;

        long lastModified = file.lastModified();

        if (this.lastModifiedFile < lastModified) {
            this.lastModifiedFile = lastModified;
            onFileChange();
        }
    }

    protected abstract void onFileChange();
}
