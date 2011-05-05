import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Amir
 * Date: Aug 27, 2005
 * Time: 3:31:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class RunApplication {
    public static void main(String[] args) throws IOException {
        File dir = new File("D:\\Media\\Music\\Music Albums");
        RenameFiles(dir);

    }

    private static void RenameFiles(File dir) throws IOException {

        System.out.println("\n\nDirectory:: " + dir);
        File[] MP3Files;

        int i = 0;
        int changed = 0;

        if (dir.isDirectory()) {
            MP3Files = dir.listFiles(new MP3FileFilter());
            for (File f : MP3Files) {
                i++;

                if (f.isDirectory()) {
                    RenameFiles(f);
                    continue;
                }

                ID3 ID3Tag = new ID3(f);
                if (f.isFile() && ID3Tag.ID3Exists()) {
                    ID3Tag.readID3();


                    if (!ID3Tag.getArtist().equals("") && !ID3Tag.getTitle().equals("")) {
                        File newFile = new File(dir, ID3Tag.getArtist() + " - " + ID3Tag.getTitle() + ".mp3");

                        if (!f.renameTo(newFile)) {
                            System.out.println("(" + i + " of " + MP3Files.length + ") Problem renaming " + f + " TO " + newFile);
                        } else {
                            changed++;
                            System.out.println("(" + i + " of " + MP3Files.length + ") Renamed " + f + " TO " + newFile);
                        }

                    } else {
                        System.out.println("(" + i + " of " + MP3Files.length + ") ID3 Tag is empty for " + f);
                    }
                } else {
                    System.out.println("(" + i + " of " + MP3Files.length + ") ID3 Tag does not exist for " + f);
                }
            }
            System.out.println("(" + changed + " of " + MP3Files.length + ") were changed in " + dir + " directory.\n");
        }
    }
}

class MP3FileFilter implements FileFilter {
    public boolean accept(File pathname) {
        return pathname.getName().toLowerCase().endsWith(".mp3") || pathname.isDirectory();
    }
}
