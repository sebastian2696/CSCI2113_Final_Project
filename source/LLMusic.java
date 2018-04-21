import java.util.*;

class Song{
    String Artist;
    String Album;
    String Song_Title;
    private String File_Path;
    private int Image_Path;

    public Song(String Artist, String Album, String Song_Title, String File_Path){
           // String Image_Path){
        this.Artist = Artist;
        this.Album = Album;
        this.Song_Title = Song_Title;
        this.File_Path = File_Path;
//        this.Image_Path = Image_Path;
    }

    public void setImage_Path(int index){
        Image_Path = index;
    }

    public String getFile_Path(){
        return File_Path;
    }

    public int getImage_Path(){
        return Image_Path;
    }

}

public class LLMusic{
    LinkedList<Song> songLL;
    ArrayList<String> imageList;


    public LLMusic(){
        songLL = new LinkedList<Song>();
        imageList = new ArrayList<String>();
    }

    public boolean songAdd(Song song, String ImagePath){
        if(imageList.size() == 0){
            imageList.add(ImagePath);
            song.setImage_Path(imageList.size()-1);
        }
        else{
            for (String content:imageList){
                if(!content.equals(ImagePath)){
                    imageList.add(ImagePath);
                    song.setImage_Path(imageList.size()-1);
                    break;
                }
            }
        }
        return songLL.add(song);
    }

    public String getimageList(int index){
        return imageList.get(index);
    }

    public Song ll_Index(int index){
        return songLL.get(index);
    }

    public void ll_Content(){
        for (Song content:songLL){
            System.out.println(content.Song_Title + " " + getimageList(content.getImage_Path()));
        }
    }
}
