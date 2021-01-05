/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rabbit_game;

/**
 *
 * @author Esraa
 */import com.sun.opengl.util.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.media.opengl.*;
import javax.swing.*;

public class Rabbit_game extends JFrame {

    public static void main(String[] args) {
        new Rabbit_game();
    }


    public Rabbit_game() {
        GLCanvas glcanvas;
        Animator animator;
        
        AnimListener listener = new AnimGLEventListener();
        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        animator = new FPSAnimator(2);
        animator.add(glcanvas);
        animator.start();
        glcanvas.addMouseListener((MouseListener) listener);
        glcanvas.addMouseMotionListener((MouseMotionListener) listener);
       

        setTitle("Rabbit Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        glcanvas.requestFocus();
    }
}


