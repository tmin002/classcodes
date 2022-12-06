package lcsfind.gui;

import java.io.File;

public interface SearchProgressListener {
    void onFileFound(File foundFile, int foundFileCount);
    void onFileSearching(File searchingFile, int searchedFileCount);
    void onSearchFinished(boolean killed);
}
