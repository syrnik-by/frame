package ru.autotestframework.web_elements.helpers;

import com.codeborne.selenide.DownloadsFolder;
import java.io.File;

/**
 * Selenide class implementation that allow usage of custom driver
 */
public class ExtendedDownloadsFolder extends DownloadsFolder {
    public ExtendedDownloadsFolder(final File folder) {
        super(folder);
    }

    @Override
    public void cleanupBeforeDownload() {
        throw new UnsupportedOperationException("Unsupported Operation");
    }
}
