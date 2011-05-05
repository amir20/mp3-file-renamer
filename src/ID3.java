import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

public class ID3 {
    File mp3File;
    private final String encoding = "Cp437";
    public String title, artist, album, year, comment;
    public Byte track, genre;

    public ID3(File mp3File) {
        this.mp3File = mp3File;
    }

    public void readID3() throws IOException {
       if (ID3Exists()) {
            RandomAccessFile in = new RandomAccessFile(mp3File, "r");
            in.seek(in.length() - 125);
            byte []buffer = new byte[125];
            if (in.read(buffer, 0, 125) != 125) {
                in.close();
                throw new IOException("ID3 Tag does not exist");
            }
            in.close();
            String tag = new String(buffer, 0, 125, encoding);

            title = tag.substring(0, 30).trim();
            artist = tag.substring(30, 60).trim();
            album = tag.substring(60, 90).trim();
            year = tag.substring(90, 94).trim();
            comment = tag.substring(94, 123).trim();
            track = (byte) tag.charAt(123);
            genre = (byte) tag.charAt(124);

       } else
           throw new IOException("ID3 Tag does not exist");
    }

    public boolean ID3Exists() throws FileNotFoundException {
        RandomAccessFile raf = new RandomAccessFile(mp3File, "r");

        try {
            if (raf.length() < 129) {
                // file to short for an ID3 tag
                raf.close();
                return false;
            } else {
                // go to position where "TAG" must be
                long seekPos = raf.length() - 128;
                raf.seek(seekPos);

                byte buffer[] = new byte[3];

                if (raf.read(buffer, 0, 3) != 3) {
                    raf.close();
                    throw new IOException("Read beyond end of file");
                }

                String testTag = new String(buffer, 0, 3, encoding);
                raf.close();
                return testTag.equals("TAG");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return false;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getYear() {
        return year;
    }

    public String getComment() {
        return comment;
    }

    public Byte getTrack() {
        return track;
    }

    public Byte getGenre() {
        return genre;
    }

    public File getMp3File() {
        return mp3File;
    }
}
