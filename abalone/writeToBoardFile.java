package abalone;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class writeToBoardFile {

    static ArrayList<String> list = new ArrayList<>();
    static File fnew;
    static FileWriter writer;

    public static void addLineToList(String boardConfig) {
        list.add(boardConfig);
    }
    
    public static void openFile() {
        int number = FileInput.getFNum();
        fnew = new File("board" + number + ".board");
        try {
            fnew.createNewFile();
            writer = new FileWriter(fnew);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void writeToFile(List<String> listOfRow) {
            try {
                int size  = listOfRow.size();
                int i = 0;
                for(String a: listOfRow) {
                    if(i < size -1) {
                        writer.write(a + ",");   
                    }
                    else
                        writer.write(a);
                    ++i;
                }
                writer.write("\n");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        
    }
    
    public static void closeFile() {
        try {
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void writeToFile() {
        int number = FileInput.getFNum();
        File fnew = new File("board" + number + ".board");
        FileWriter writer = null;
        try {
            fnew.createNewFile();
            writer = new FileWriter(fnew);
            for(String a : list) {
                writer.write(a);
            }
            
            writer.close();
            list.clear();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

       
        
        
    }
}
