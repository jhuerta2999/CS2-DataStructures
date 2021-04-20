import java.util.Arrays;

public class MyHashMap extends AbstractHashMap{
    protected static Entry[] entryList;
    
    public MyHashMap(){
        super(.75);
        entryList = new Entry[capacity];
    }
    
    public void put(Object key, Object value){
        Entry addEntry = new Entry(key, value);
        //Depending on the key it will determine where it will be placed w/ the hash function
        int location = hash(key);
        
        //If key exist already it will search the list and enter is key and value
        if(containsKey(key)){ //O(n^2) because contains key loops through the length of the dictioanry to find the key then looks for the key if the key exists to update
            for(int i = 0; i < entryList.length; i++){
                if(entryList[i] != null && key.equals(entryList[i].key)){
                    entryList[i] = addEntry;
                }
            }
        }
        //Will add the key to this location if it is null
        else if(entryList[location] == null){
            entryList[location] = addEntry;
            numKeys++;
        }
        //will search for an available spot if the original one is taken
        else{
            location = (location + 1) % capacity;
            while(entryList[location] != null){
                location = (location + 1) % capacity;
            }

            if(entryList[location] == null){
                entryList[location] = addEntry;
            }

            entryList[location] = addEntry;
            numKeys++;
        }

        //Depending on the ratio of space it will update the length of the list if necessary
        if(lambda() >= maxLoad){
            resize();
        }
    }

    private double lambda(){
        //Updates ratio of space
        double l = (double)numKeys/(double)capacity;
        return l;
    }

    //will get the value of the key provided if existant
    public Object get(Object key){
        Object target = null;
        for(int i = 0; i < entryList.length; i++){//O(n) looks for the key it wants and returns the value of teh found key
            if(entryList[i] != null){
                if(key.equals(entryList[i].key)){
                    target = entryList[i].value;
                }
            }
        }
        return target;  
    }

    protected void resize(){
        Entry[] copy = new Entry[capacity];
        //Copies data from old list ot new one
        for(int i = 0; i < entryList.length; i++){//O(n) copies the keys from the list to the new list
            copy[i] = entryList[i];
        }
        numKeys = 0;
        capacity =(int)(capacity * 2.5);
        entryList = new Entry[capacity];

        //places the keys with the new hash value/ location
        for(int i = 0; i < copy.length; i++){//O(n) places each key with its new hash location
            if(copy[i] != null){
                put(copy[i].key, copy[i].value);
             }
         }
    }

    //Searches through the list ot check if the key exists
    public boolean containsKey(Object key){        
        for(int i = 0; i < entryList.length; i++){//O(n) looks for the key in the list
            if(entryList[i] != null){
                if(key.equals(entryList[i].key)){
                    return true;
                }
            }
        }
        return false;
    }

    //Calls insertion sort and returns the ordered list
    public Entry[] getEntries(){
        insertionSort(entryList);
        return entryList;
    }

    private void insertionSort(Entry[] arr){
        Entry[] newList = new Entry[numKeys];
        int posInNew = 0;
        //Copies keys from old to new and avoids the nulls
        for(int i = 0; i < entryList.length; i++){// O(n) creates a new list with no nulls in it
            if(entryList[i] != null){
                newList[posInNew] = entryList[i];
                posInNew++;
            }
        }
        
        int acc = 1;
            //will order the list as long as the acc does not exceed the length
            for(int i = acc; i < newList.length; i++){//O(n^2) loops through the list at every position and then compares that position with the rest ot see if it is greter than or less than  
                int occur = (int)newList[acc].value;
                int nextEntry = (int)newList[acc-1].value;
                //As long as the position is greater than 0 and value is greater it will slide over the key
                if(occur > nextEntry){
                    for(int j = i - 1; j >= 0; j--){
                        Entry tmp = newList[j];
                        if((int)tmp.value < occur){
                            tmp = newList[j];
                            newList[j] = newList[j+1];
                            newList[j+1] = tmp;
                        }
                    }

                }
                acc++;
            }
        //names the new list as the old one
        entryList = newList;
    }
}