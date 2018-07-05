/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package life.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

import life.view.*;
import life.model.*;

/**
 *
 * @author peral
 */
public class LifeController implements ActionListener
{   
    // View of MVC pattern
    private LifeView lifeView;
    
    // Model of MVC pattern
    private LifeModel lifeModel;        
    
    // Size of board (limited to 200x200 cells)
    private int BOARD_SIZE;
    
    // Represents the board.  alive[r][c] is true if the cell in row r, column c is alive.
    private boolean[][] cells;
    
    // Timer to update the Life next status
    private Timer timer;
    
    /**
     * Defaul constructor, initialize cells and Timer
     */
    public LifeController (int boardSize)
    {
        BOARD_SIZE = boardSize;
        cells = new boolean[BOARD_SIZE][BOARD_SIZE];
        timer = new Timer(50,this);
    }
    
    public void SetView(LifeView lV)
    {
        lifeView = lV;
        
        // Fill with the users
        lifeView.fillExistsUsers(LifeModel.getUsers());
    }

    public void SetModel(LifeModel lM)
    {
        lifeModel = lM;  
    }
    
/**
     * Compute the next generation of cells.
     */
    public void DoFrame()
    {
        boolean[][] newboard = new boolean[BOARD_SIZE][BOARD_SIZE];
        for ( int row = 0; row < BOARD_SIZE; row++ )
        {
            int above, below; // rows considered above and below row number r
            int left, right;  // columns considered left and right of column c
            above = row > 0 ? row-1 : BOARD_SIZE-1;
            below = row < BOARD_SIZE-1 ? row+1 : 0;
            for ( int col = 0; col < BOARD_SIZE; col++ )
            {
                left =  col > 0 ? col-1 : BOARD_SIZE-1;
                right = col < BOARD_SIZE-1 ? col+1 : 0;
                int nAlive = 0; // number of alive cells in the neighboring cells
                if (cells[above][left])
                    nAlive++;
                if (cells[above][col])
                    nAlive++;
                if (cells[above][right])
                    nAlive++;
                if (cells[row][left])
                    nAlive++;
                if (cells[row][right])
                    nAlive++;
                if (cells[below][left])
                    nAlive++;
                if (cells[below][col])
                    nAlive++;
                if (cells[below][right])
                    nAlive++;
                if (nAlive == 3 || (cells[row][col] && nAlive == 2))
                    newboard[row][col] = true;
                else
                    newboard[row][col] = false;
            }
        }
        cells = newboard;
    }

    public void clear()
    {
        cells = new boolean[BOARD_SIZE][BOARD_SIZE];
    }
    
    public void RandomData ()
    {
        for (int row = 0; row < BOARD_SIZE; row++)
        {
            for (int col = 0; col < BOARD_SIZE; col++)
              cells[row][col] = (Math.random()>0.5);
        }
    }
    
    public boolean[][] getCells ()
    {
        return cells;
    }
    
    public void setCellAlive (int row, int col)
    {
        cells[row][col] = true;
    }
    
    public void setCellDead (int row, int col)
    {
        cells[row][col] = false;
    }
    
     /**
     * Respond to an ActionEvent the timer.
     * @param e
     */
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        if (src == timer)
        {
            DoFrame();
            lifeView.showBoard(cells);
        }
    }
    
    /**
     * If the cells evolution is started return true
     * @return 
     */
    public boolean isRunning ()
    {
        return timer.isRunning();
    }
    
    /**
     * Stop the cell evolution
     */
    public void stop ()
    {
        timer.stop();
    }

    /**
     * Start the cells evolution
     */
    public void start ()
    {
        timer.start();
    }

    /**
     * Save data of user
     * @param user
     * @return 
     */
    public boolean save (String user)
    {
        return lifeModel.Save(cells, user);
    }
    
    /**
     * Load a saved user
     * @param user
     * @return 
     */
    public boolean load (String user)
    {
        cells = lifeModel.Load(user);
        
        if (cells == null)
            return false;
                    
        else
            lifeView.showBoard(cells);

        return true;
    }
    
    
} // LifeController
