import java.util.*;

class Song{
            String Artist;
            String Album;
            String Song_Title;
            private String File_Path;
            private String Image_Path;

            public Song(String Artist, String Album, String Song_Title, String File_Path, String Image_Path){
                this.Artist = Artist;
                this.Album = Album;
                this.Song_Title = Song_Title;
                this.File_Path = File_Path;
                this.Image_Path = Image_Path;
            }

            public String getFile_Path(){
                return File_Path;
            }

            public String getImage_Path(){
                return Image_Path;
            }

}

public class LLMusic{
        LinkedList<Song> songLL;

        public LLMusic(){
            songLL = new LinkedList<Song>();
        }

        public boolean songAdd(Song song){
            return songLL.add(song);
        }

        public void ll_Content(){
            for (Song content:songLL)
                System.out.println(content.Song_Title);
        }

//        public static void main(String[] args){
//            LLMusic list = new LLMusic();
//            Song song = new Song("A", "B", "C", "D", "E");
//            list.Song_Add(song);

//            list.LL_Content();
//        }
}
