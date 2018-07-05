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
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import life.controller.*;


/**
* JPanel with the app items: buttons, fields, cells grid
*/
public class LifeView  extends JPanel implements MouseMotionListener, MouseListener, ActionListener, ListSelectionListener
{

    private LifeController lifeController;
    
    // View of Life game (grid of cells)
    private final LifeBoardView board;
    // Size of board (limited to 200x200 cells)
    private int BOARD_SIZE;
    // Buttons to receive commands
    private final JButton  pauseRunButton;
    private final JButton  randomDataButton;
    private final JButton  clearButton;
    private final JButton  exitButton;
    private final JButton  loadButton;
    private final JButton  saveButton;
    
    private final JPanel usersPanel;
    
    private JTextField usersField;
    DefaultListModel<String> listModel;
    private JList<String> usersList;
    
    /**
     * Create a life game board, initially empty.  The number of cells on each side of the grid is GRID_SIZE.
     */
    public LifeView( LifeController lC, int boardSize )
    {   
        lifeController = lC;
        
        BOARD_SIZE = boardSize;
        
        setLayout(new BorderLayout(3,3));
        
        board = new LifeBoardView(BOARD_SIZE);
        add(board,BorderLayout.CENTER);
        JPanel commandsPanel = new JPanel();
        add(commandsPanel,BorderLayout.WEST);
        usersPanel = new JPanel();
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.PAGE_AXIS));
        commandsPanel.setLayout(new GridLayout(3,2));
        
        clearButton = new JButton("Clear");
        pauseRunButton = new JButton("Run");
        exitButton = new JButton("Quit");
        randomDataButton = new JButton("Random Data");
        loadButton = new JButton("Load");
        saveButton = new JButton("Save");
        
        
        usersField = new JTextField ();
        
        listModel = new DefaultListModel<>();
        usersList = new JList(listModel);
        usersList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        usersList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        usersList.setVisibleRowCount(-1);
        
        commandsPanel.add(pauseRunButton);
        commandsPanel.add(randomDataButton);
        commandsPanel.add(clearButton);
        commandsPanel.add(exitButton);
        commandsPanel.add(usersPanel);
        
        usersPanel.add(loadButton);
        usersPanel.add(usersField);
        usersPanel.add(saveButton);
        
        JScrollPane scrollPane = new JScrollPane(usersList);
        scrollPane.setPreferredSize(new Dimension(150, 200));
        usersList.setLayoutOrientation(JList.VERTICAL_WRAP);
        usersList.setVisibleRowCount(0);
        commandsPanel.add(scrollPane);
        

        // Listener to each command
        clearButton.addActionListener(this);
        exitButton.addActionListener(this);
        pauseRunButton.addActionListener(this);
        randomDataButton.addActionListener(this);
        loadButton.addActionListener(this);
        saveButton.addActionListener(this);
        usersList.addListSelectionListener(this);
        board.addMouseListener(this);
        board.addMouseMotionListener(this);
        
        JFrame frame = new JFrame("Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(this);
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * Fill the JList with the name exists users
     * @param users 
     */
    public void fillExistsUsers (ArrayList<String> users)
    {
        for (String user : users)
            listModel.addElement(user);
    }

    /**
     *  Show the cells status
     */
    public void showBoard(boolean[][] cells)
    {
        for (int row = 0; row < BOARD_SIZE; row++)
        {
            for (int col = 0; col < BOARD_SIZE; col++)
            {
                if (cells[row][col])
                    board.setCellAlive(row,col);
                else
                    board.setCellDead(row,col);
            }
        }
    }


    /**
     * Respond to an ActionEvent from one of the control buttons.
     * @param e
     */
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        if (src == exitButton)
            System.exit(0);

        else if (src == clearButton)
        {
            lifeController.clear();
            board.clear();
        }
        else if (src == pauseRunButton)
        {
            if (lifeController.isRunning())
            {
                lifeController.stop();
                clearButton.setEnabled(true);
                randomDataButton.setEnabled(true);
                loadButton.setEnabled(true);
                saveButton.setEnabled(true);
                usersField.setEnabled(true);
                usersList.setEnabled(true);
                pauseRunButton.setText("Run");
            }
            else
            {
                lifeController.start();
                clearButton.setEnabled(false);
                randomDataButton.setEnabled(false);
                loadButton.setEnabled(false);
                saveButton.setEnabled(false);
                usersField.setEnabled(false);
                usersList.setEnabled(false);
                pauseRunButton.setText("Pause");
            }
        }
        else if (src == randomDataButton)
        {
            lifeController.RandomData();
            showBoard(lifeController.getCells());
        }
        else if (src == saveButton)
        {
            String user = usersField.getText();
            if (user.equals(""))
                JOptionPane.showMessageDialog(this, "You need provide or select a user name","Warning", JOptionPane.WARNING_MESSAGE);
            else if (lifeController.save(user))
            {
                int i;
                for (i=0; i<listModel.getSize(); i++)
                    if (listModel.getElementAt(i).equals(user))
                        break;
                if (i == listModel.getSize())
                    listModel.addElement(user);
                       
                JOptionPane.showMessageDialog(this, "The data was saved", "", JOptionPane.INFORMATION_MESSAGE);
            }
            else
                JOptionPane.showMessageDialog(this, "The data was NOT saved","Warning", JOptionPane.WARNING_MESSAGE);
        }

        else if (src == loadButton)
        {
            String user = usersField.getText();
            if (user.equals(""))
                JOptionPane.showMessageDialog(this, "You need select a user name","Warning", JOptionPane.WARNING_MESSAGE);
            if (lifeController.load(user))
                JOptionPane.showMessageDialog(this, "The data was loaded", "", JOptionPane.INFORMATION_MESSAGE);
        }
        
        else if (src == usersList)
        {
            usersField.setText(usersList.getSelectedValue());
        }
    }


    /**
     * Click over a cell, then change status cell .
     * @param e
     */
    public void mousePressed(MouseEvent e)
    {
        if (lifeController.isRunning())
            return;
        int row = board.yToRow(e.getY());
        int col = board.xToCol(e.getX());
        if (row >= 0 && row < board.getRowCount() && col >= 0 && col < board.getColumnCount())
        {
            if (e.isMetaDown() || e.isControlDown()) 
            {
                board.setCellDead(row,col);
                lifeController.setCellAlive(row, col);
            }
            else
            {
                board.setCellAlive(row,col);
                lifeController.setCellAlive(row, col);
            }
        }
    }


    /**
     * Mouse is dragged, then changue the cells affected status .
     */
    public void mouseDragged(MouseEvent e)
    {
        mousePressed(e);
    }

    /**
     * To be a complete implemented interface
     * @param e mouse event
     */
    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseMoved(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) { }

    public void valueChanged(ListSelectionEvent e)
    {
        usersField.setText(usersList.getSelectedValue().toString());
    }
    
} // LifeView
