public class Album{

    protected String artistName;
    protected String albumName;
    
    //Creates the album oject that is passed around
    public Album(String albumInfo[]){
        artistName = albumInfo[0];
        albumName = albumInfo[1];
    }

    //converts the the variables into a string 
    public String toString(){
        String albums = artistName + "-" + albumName;
        return albums;
    }

    //Allows comparison of 2 objects 
    public boolean equals(Album object){
        if(object == null)
            return false;
        
        else if(this == object)
            return true;
        
        else if(!(object instanceof Album))
            return false;
        Album album2 = (Album) object;

        if(this.artistName.equals(album2.artistName) && this.albumName.equals(album2.albumName))
            return true;

        return false;
    }
}