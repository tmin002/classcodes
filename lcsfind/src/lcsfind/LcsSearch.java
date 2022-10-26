package lcsfind;
import java.io.*;
import java.util.ArrayList;

public class LcsSearch {
   public static ArrayList<File> search(String fromPath, String fileName) {

      File from = new File(fromPath);
      ArrayList<File> result = new ArrayList<File>();
      File[] ls = from.listFiles();

      if (ls == null)
         return result;

      for (File f : ls) {
         if (checkFileNameLcsMatch(f.getName(), fileName)) {
            result.add(f);
         }
         if (f.isDirectory()) {
            result.addAll(LcsSearch.search(f.getAbsolutePath(), fileName));
         } 
      }

      return result;
   }

   public static boolean checkFileNameLcsMatch(String fileName1, String fileName2) {
      int[][] l = new int[fileName1.length()+1][fileName2.length()+1];
      int f1l = fileName1.length();
      int f2l = fileName2.length();
      int i=0, j=0;

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
      return result >= f2l;
   }
}
