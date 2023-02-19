package fr.bretzel.minestom.utils.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.PrintStream;

public class TracePrintStream extends PrintStream {

    private final Logger logger = LoggerFactory.getLogger(TracePrintStream.class);
    private final Level level;

    public TracePrintStream(PrintStream original, Level level) {
        super(original);
        this.level = level;
    }

    @Override
    public void println(String line) {
        if (line != null)
            log(line.replaceAll("\\n", ""));
    }

    protected void log(String log) {
        switch (level) {
            case WARN -> logger.warn(log);
            case ERROR -> logger.error(log);
            case DEBUG -> logger.debug(log);
            case TRACE -> logger.trace(log);
            default -> logger.info(log);
        }
    }
}
