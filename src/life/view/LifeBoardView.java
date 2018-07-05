/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package life.view;

/**
 *
 * @author peral
 */
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 *  Class as view of "Life" game a Jpanel with a grid of reactangles as cells
 */
public class LifeBoardView extends JPanel
{
   private int rows;
   private int columns;
   private final Color deadColor = Color.BLACK;
   private final Color aliveColor = Color.GREEN;
   private Color[][] cells;
   private BufferedImage bImage;
    
  
   /**
    *  Construct the cells grid.
    *  @param sizeGrid the grid size (limited to be square).
    */
   public LifeBoardView(int sizeGrid)
   {
      this.rows = sizeGrid;
      this.columns = sizeGrid;
      cells = new Color[sizeGrid][sizeGrid];
      setBorder(BorderFactory.createLineBorder(Color.BLACK,0));
      
      if (sizeGrid > 0)
         setPreferredSize(new Dimension((600/sizeGrid)*sizeGrid, (600/sizeGrid)*sizeGrid));
   }
   
   
   /**
    *  Return the number of rows of rectangles in the cells.
    */
   public int getRowCount()
   {
      return rows;
   }
   
   
   /**
    *  Return the number of columns of rectangles in the grid.
    */
   public int getColumnCount()
   {
      return columns;
   }   
   
  /**
   * Set a cell as alive
   * @param row
   * @param col 
   */
   public void setCellAlive(int row, int col)
   {
      if (row >=0 && row < rows && col >= 0 && col < columns)
      {
         cells[row][col] = aliveColor;
         drawSquare(row,col);
      }
   }   
   
   /**
    * Set a status cell as dead
    * @param row
    * @param col 
    */
   public void setCellDead(int row, int col)
   {
      if (row >=0 && row < rows && col >= 0 && col < columns)
      {
         cells[row][col] = deadColor;
         drawSquare(row,col);
      }
   }
   
   /**
    *   Clear the mosaic by setting all the colors to null.
    */
   public void clear()
   {
      for (int i = 0; i < rows; i++)
         for (int j = 0; j < columns; j++)
            cells[i][j] = deadColor;
      repaint();   
   }
   

/**
 * Pixels coords on grid to row
 * @param y
 * @return 
 */
   public int yToRow(int y)
   {
      double height = (double)(getHeight())/rows;
      int row = (int)(y/height);
      if (row >= rows)
         return rows;
      else
         return row;
   }

   
/**
 * Pixels coords on grid to column
 * @param x
 * @return 
 */
   public int xToCol(int x)
   {
      double width = (double)(getWidth())/columns;
      int col = (int)(x/width);
      if (col >= columns)
         return columns;
      else
         return col;
   }   
  
   /**
    * Draw the grid from the override method and call the super (above the buffered image)
    * @param g 
    */
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      bImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
      Graphics graphics = bImage.getGraphics();
      for (int r = 0; r < rows; r++)
         for (int c = 0; c < columns; c++)
            drawSquare(graphics,r,c);
      graphics.dispose();
      g.drawImage(bImage,0,0,null);
   }
   
   /**
    * Draw the square and fill it
    * @param g
    * @param row
    * @param col 
    */
   private void drawSquare(Graphics g, int row, int col)
   {
      double rowHeight = (double)(getHeight())/rows;
      double colWidth = (double)(getWidth())/columns;
      int x = (int)Math.round(colWidth*col);
      int y = (int)Math.round(rowHeight*row);
      int height = Math.max(1, (int)Math.round(rowHeight*(row+1))-y);
      int width = Math.max(1, (int)Math.round(colWidth*(col+1))-x);
      Color statusColor = cells[row][col];
      g.setColor((statusColor == null)?deadColor:statusColor);
      g.fillRect(x+1,y+1,width-2,height-2);
      g.setColor(deadColor);
      g.drawRect(x,y,width-1,height-1);
      repaint(x,y,width,height);
   }
   /**
    * Pait a specified rectangle
    * @param row
    * @param col 
    */
   private void drawSquare(int row, int col)
   {
      if (bImage == null)
         repaint();
      else {
         Graphics g = bImage.getGraphics();
         drawSquare(g,row,col);
         g.dispose();
      }
   }
     
} // LifeBoardView