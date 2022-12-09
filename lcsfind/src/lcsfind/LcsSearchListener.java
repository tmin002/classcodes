package lcsfind;

import java.io.File;

public interface LcsSearchListener {
    void onFileFound(File foundFile, int foundFileCount);
    void onFileSearching(File searchingFile, int searchedFileCount);
    void onSearchFinished(boolean isKilled);
}
