package demos.gears;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

/**
 * JOGL 2.0 Program Template (GLCanvas) This is a "Component" which can be added
 * into a top-level "Container". It also handles the OpenGL events to render
 * graphics.
 * @link https://www3.ntu.edu.sg/home/ehchua/programming/opengl/JOGL2.0.html
 */
@SuppressWarnings("serial")
public class SimplePaint extends GLCanvas implements GLEventListener {
	// Define constants for the top-level container
	private static String TITLE = "Paint with JOGL 2.0 Setup (GLCanvas)"; // window's title
	private static final int CANVAS_WIDTH = 1366; // width of the drawable
	private static final int CANVAS_HEIGHT = 768; // height of the drawable
	private static final float CANVAS_XSCALE = 5.0f;
	private static final float CANVAS_YSCALE = 3.0f;
	//private static final int MAX_WIDTH = 1280; // width of the drawable
	//private static final int MAX_HEIGHT = 720; // height of the drawable
	
	private static final int FPS = 60; // animator's target frames per second
	public final ArrayList<Point> list;
	public int x=0;
	public int y=0;
	/** The entry main() method to setup the top-level container and animator */
	public static void main(String[] args) {
            // Run the GUI codes in the event-dispatching thread for thread safety
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    // Create the OpenGL rendering canvas
                    GLCanvas canvas = new SimplePaint();
                    canvas.setPreferredSize(new Dimension(CANVAS_WIDTH,
                                    CANVAS_HEIGHT));

                    // Create a animator that drives canvas' display() at the
                    // specified FPS.
                    final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

                    // Create the top-level container
                    final JFrame frame = new JFrame(); // Swing's JFrame or AWT's
                                                                                            // Frame
                    frame.getContentPane().add(canvas);
                    frame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            // Use a dedicate thread to run the stop() to ensure
                            // that the
                            // animator stops before program exits.
                            new Thread() {
                                @Override
                                public void run() {
                                    if (animator.isStarted())
                                        animator.stop();
                                    System.exit(0);
                                }
                            }.start();
                        }
                    });

                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

                    frame.setTitle(TITLE);
                    frame.pack();
                    frame.setVisible(true);
                    animator.start(); // start the animation loop
                }
            });
	}

	// Setup OpenGL Graphics Renderer

	private GLU glu; // for the GL Utility

	/** Constructor to setup the GUI for this Component */
	public SimplePaint() {
            this.addGLEventListener(this);
            list = new ArrayList<>();
            this.addMouseMotionListener(new MouseListener());
	}


        @Override
	public void display(GLAutoDrawable arg0) {
            GLAutoDrawable drawable = arg0;
            GL2 gl = drawable.getGL().getGL2(); // get the OpenGL 2 graphics context
            gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT); // clear color
            gl.glLoadIdentity(); // reset the model-view matrix

            gl.glTranslatef(0.0f, 0.0f, -6.0f); // translate into the screen
            gl.glColor3f(1.0f,0.0f,0.60f);

            gl.glPointSize(5.0f);
	    gl.glBegin(GL2.GL_POINTS);
            for (Point p : list) {
                gl.glVertex2d(convertX(p ),convertY(p));
            } 
	    gl.glEnd();//end drawing of points

	}

        @Override
	public void dispose(GLAutoDrawable arg0) {
            // stub
	}

        @Override
	public void init(GLAutoDrawable arg0) {
            GLAutoDrawable drawable = arg0;
            GL2 gl = drawable.getGL().getGL2(); // get the OpenGL graphics context
            glu = new GLU(); // get GL Utilities
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
            gl.glClearDepth(1.0f); // set clear depth value to farthest
            gl.glEnable(GL2.GL_DEPTH_TEST); // enables depth testing
            gl.glDepthFunc(GL2.GL_LEQUAL); // the type of depth test to do
            gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST); // best
            gl.glShadeModel(GL2.GL_SMOOTH); // blends colors nicely, and smoothes out
	}

        @Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {

            GLAutoDrawable drawable = arg0;
            int width = arg3;
            int height = arg4;
            GL2 gl = drawable.getGL().getGL2(); // get the OpenGL 2 graphics context

            if (height == 0) {
                height = 1; // prevent divide by zero
            }
            float aspect = (float) width / height;

            // Set the view port (display area) to cover the entire window
            gl.glViewport(0, 0, width, height);

            // Setup perspective projection, with aspect ratio matches viewport
            gl.glMatrixMode(GL2.GL_PROJECTION); // choose projection matrix
            gl.glLoadIdentity(); // reset projection matrix
            glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear,
                                                                                                            // zFar

            // Enable the model-view transform
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glLoadIdentity(); // reset
	}

	//Kalibrasi Posisi X
	
	public static double convertX(Point p){
            return (p.getX()-(CANVAS_WIDTH/2.0))*CANVAS_XSCALE/(CANVAS_WIDTH/2.0);
	}

	//Kalibrasi Posisi Y
	
	public static double convertY(Point p){
            return -1*(p.getY()-(CANVAS_HEIGHT/2.0))*CANVAS_YSCALE/(CANVAS_HEIGHT/2.0);
	}
    
    private class MouseListener implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) {
            list.add(e.getLocationOnScreen());
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            //stub
        }
    }
}