/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package life.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


/**
 *
 * @author peral
 */
public class LifeModel
{
    private int BOARD_SIZE;
    
    public LifeModel(int boardSize)
    {
        BOARD_SIZE  = boardSize;
    }
    
    public static ArrayList<String> getUsers()
    {
        ArrayList<String> users = new ArrayList<>();
        
        File [] files = new File(".").listFiles(new FilenameFilter()
                                                    {
                                                        public boolean accept(File dir, String name)
                                                        { 
                                                            return name.endsWith(".life");
                                                        }
                                                    });
        
        if (files != null)
            for (File file : files)
            {
                try
                {
                    users.add(file.getName().split(".life")[0]);
                }
                catch (Exception e)
                {
                    continue;
                }
            }
        return users;
    }
    
    public boolean Save(boolean [][]cells, String user)
    {
        Path file = Paths.get(user.toLowerCase()+".life");
        try (BufferedWriter writer = Files.newBufferedWriter(file, Charset.forName("US-ASCII"))) {
            for (int row = 0; row < BOARD_SIZE; row++)
                for (int col = 0; col < BOARD_SIZE; col++)
                {
                    if (cells[row][col])
                    {
                        String cell = Integer.toString(row) + ":" + Integer.toString(col) + "\n";
                        writer.write(cell, 0, cell.length());
                    }
                }
        }
        catch (Exception x)
        {
            return false;
        }
        return true;
    }
    
    public boolean [][] Load (String user)
    {
        boolean [][] loadedBoard = new boolean[BOARD_SIZE][BOARD_SIZE];
        Path file = Paths.get(user.toLowerCase()+".life");
        try (InputStream in = Files.newInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in))){
            String line;
            // Read each line as integers pairs (row, col)
            while ((line = reader.readLine()) != null)
            {
                String [] liveCell = line.split(":");
                if (liveCell[0] != null && liveCell[0] != "" && liveCell[1] != null && liveCell[1] != "")
                {
                    loadedBoard[Integer.parseInt(liveCell[0])][Integer.parseInt(liveCell[1])] = true;
                }    
                else
                    return null;
                
            }
        }
        catch (Exception x)
        {
            return null;
        }
        
        return loadedBoard;
    }
}
