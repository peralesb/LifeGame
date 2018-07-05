/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package life.controller;

import javax.swing.JPanel;
import life.controller.*;
import life.view.*;
import life.model.*;
/**
 *
 * @author peral
 */
public class LifeMain
{
    /**
     * The size of the board. NOTE: more than 200 cause view problems
     */
    public static int BOARD_SIZE = 200;
    
     /**
     * The main method
     * @param args 
     */
    public static void main(String[] args)
    {
            LifeController lifeController = new LifeController(BOARD_SIZE);
            LifeView lifeView = new LifeView(lifeController, BOARD_SIZE);
            LifeModel lifeModel = new LifeModel(BOARD_SIZE);
            
            lifeController.SetView(lifeView);
            lifeController.SetModel(lifeModel);          
    }
} // LifeMain
