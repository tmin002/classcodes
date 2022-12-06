package lcsfind;
import lcsfind.gui.SearchProgressListener;

import java.io.*;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;

public class LcsSearch {

   // Infinite
   public static final int INFINITE = -1;

   // Member variables
   private final String searchPath;
   private final String searchFileName;
   private final int searchMaxDepth;
   private final SearchProgressListener listener;

   // Thread related
   private Thread searchThread;
   private boolean die = false; // kill thread if true

   // File counters
   private int foundFilesCount = 0;
   private int searchedFilesCount = 0;

   // Constructor
   public LcsSearch(String searchPath, String searchFileName, int searchMaxDepth, SearchProgressListener listener) {
      this.searchPath = searchPath;
      this.searchFileName = searchFileName;
      this.searchMaxDepth = searchMaxDepth;
      this.listener = listener;
   }

   // Wrapper of searchRecursive().
   public void startSearchAsync() {
      searchThread = new Thread(() -> {
         searchRecursive(searchPath, searchFileName, 0);
         listener.onSearchFinished(this.die);
      });
      searchThread.start();
   }

   public void stopSearch() {
      try {
         die = true;
         searchThread.join();
      } catch (InterruptedException ignored) {}
   }

   private void searchRecursive(String fromPath, String fileName, int depth) {

      File from = new File(fromPath);
      File[] ls = from.listFiles();

      if (ls == null)
         return;

      for (File f : ls) {
         listener.onFileSearching(f, ++searchedFilesCount);
         if (checkFileNameLcsMatch(f.getName(), fileName)) {
            listener.onFileFound(f, ++foundFilesCount);
         }
         if (f.isDirectory() && depth != searchMaxDepth) {
            searchRecursive(f.getAbsolutePath(), fileName, depth+1);
         }
         if (die) {
            return;
         }
      }
   }


   public static boolean checkFileNameLcsMatch(String fileName1, String fileName2) {
      int[][] l = new int[fileName1.length()+1][fileName2.length()+1];
      int f1l = fileName1.length();
      int f2l = fileName2.length();
      int i=0, j=0;

	  // Change to be case-insensitive
	  fileName1 = fileName1.toLowerCase();
	  fileName2 = fileName2.toLowerCase();

      // Don't check file extensions
      //fileName1 = fileName1.substring(0, fileName1.lastIndexOf('.'));

      // Fill first col
      for (i=0; i<=f1l; i++)
         l[i][0] = 0;

      // Fill first row
      for (i=0; i<=f2l; i++)
         l[0][i] = 0;

      // Fill rest of the table
      for (i=1; i<=f1l; i++) {
         for (j=1; j<=f2l; j++) {
            l[i][j] = fileName1.charAt(i-1) == fileName2.charAt(j-1) ?
               l[i-1][j-1] + 1 : 
               Math.max(l[i-1][j], l[i][j-1]);
         }
      }

      // Return result
      int result = l[i-1][j-1];
      return result == f2l;
   }
}
