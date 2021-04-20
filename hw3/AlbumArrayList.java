 class AlbumArrayList extends AlbumList{
    //Initiates value of acc and length of array
    private int numAlbums = 0;
    private Album[] albumStock;
    public AlbumArrayList(){
        albumStock = new Album[5];
    }

    //method that expands if the array is full by creating a new array
    private void expand(){
        Album[] newArray = new Album[(3/2) * (albumStock.length) + 1];

        for(int i = 0; i < albumStock.length; i++){
            newArray[i] = albumStock[i];
        }
        albumStock = newArray;
    }   
    //will call expand if full and then update the array and acc
	public void add(Album newA){
        if(numAlbums == albumStock.length){
            this.expand();
        }
        albumStock[numAlbums] = newA;
        numAlbums++;
    }
    //will call expand if full and then update the array and acc
    // and also compare the objects to search and remove the duplicate objects
	public Album remove(Album targetA){
        if(numAlbums == albumStock.length){
            this.expand();
        }
        for(int i = 0; i < numAlbums; i++){
            if(albumStock[i].equals(targetA)){
                albumStock[i] = albumStock[i+1];
            }
        }  
        numAlbums--;
        return targetA;   
    }   
    //will call expand if full and then update the array and acc 
    //will do most on the based on the position provided 
	public Album remove(int idx){
        Album removeAlbum = albumStock[idx];
        if(numAlbums == albumStock.length){
            expand();
        }
        for(int i = idx - 1; i < numAlbums; i++){
            albumStock[i] = albumStock[i+1];
        }
        numAlbums--;
        return removeAlbum;
    }
    //allows to access number in acc
	public int size(){
		return numAlbums;
    }   
    //access an object at the position with number provided
	public Album get(int idx){
        return albumStock[idx];
    }
    //replace an object with another object that the lcation provided
	public void set(int idx, Album newA){
        if(numAlbums == albumStock.length){
            expand();
        }
        albumStock[idx] = newA;
    }
}