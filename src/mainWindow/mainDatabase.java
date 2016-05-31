package mainWindow;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by tengumis on 31.05.2016.
 */
public class MainDatabase {
    private HashMap<String,String> kategorie;
    MainDatabase(){
        kategorie=new HashMap<>();
    }
    public void insert(String a,String b) {
        kategorie.put(a,b);
    }

    public Set<String> getCategories() {
        return kategorie.keySet();
    }

    public ArrayList<Pair<String,String>> getSetOfElements(String category,int count) throws Exception {
        if(kategorie.containsKey(category)){
            ArrayList<Pair<String,String>> result=new ArrayList<>();
            if(count<1) return result;
            BufferedReader br = new BufferedReader(new FileReader(kategorie.get(category)));
            try {
                br.readLine(); //czytam tytuł;
                String line = br.readLine();
                String[] parts;
                while (line != null) {
                    parts=line.split(":");
                    result.add(new Pair<>(parts[0],parts[1]));
                    line = br.readLine();
                }
                shuffle(result);
                while(result.size()>count) result.remove(result.size()-1);
                for(int i=0;result.size()<count;i++) result.add(result.get(i));
                shuffle(result);
                return result;
            } finally {
                br.close();
            }
        }
        else throw new Exception("Brak kategorii");
    }
    private static void shuffle(ArrayList<Pair<String,String>> array){
        int noOfElements =array.size();
        Random rnd = ThreadLocalRandom.current();
        for(int i=0;i<noOfElements;i++){
            int s= rnd.nextInt(i + 1);
            Pair<String,String> tmp=array.get(s);
            array.set(s,array.get(i));
            array.set(i,tmp);
        }
    }

}
