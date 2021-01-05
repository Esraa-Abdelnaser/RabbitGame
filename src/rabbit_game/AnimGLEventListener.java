package rabbit_game;

import com.sun.opengl.util.FPSAnimator;
import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.media.opengl.*;
import java.util.BitSet;
import javax.media.opengl.glu.GLU;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class AnimGLEventListener extends AnimListener {

    FPSAnimator animator;
    int xPosition = 0;
    int yPosition = 0;
    int score = 0;
    int timer = 100;
    int state = 0;

    int pit;
    boolean howPlay = false;
    boolean dead = false;
    boolean hit = false;
    boolean back = false;
    boolean game = false;
    boolean menu = false;
    boolean level = false;
    boolean gameOver = false;
    boolean easy = false;
    boolean mid = false;
    boolean hard = false;
    boolean exit = false;
    boolean isPause = false;
    boolean resume = false;

    boolean s = false;
    boolean m = false;
    boolean h = false;
    boolean g = false;

    int maxWidth = 100;
    int maxHeight = 100;
    int x, y;
    GLCanvas glc;
    GL gl;

    TextRenderer tr = new TextRenderer(Font.decode("PLAIN"));
    String textureNames[] = {"easy.JPEG", "mid.JPEG", "hard.JPEG", "rabbit.png", "background.jpg", "win.JPEG", "levels.JPEG",
        "gameover.JPEG", "hammer1.png", "hammer2.jpg", "HowToPlay.png", "monster.png", "back.png", "pause-button.png", "PUASE.png"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];

    public void init(GLAutoDrawable gld) {

        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels()
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }

        }
        playSound("musicGame.wav");

    }

    public void playSound(String wav) {

        try {

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(wav));

            Clip clip = AudioSystem.getClip();

            clip.open(audioStream);
            clip.start();
            clip.loop(100);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            JOptionPane.showMessageDialog(new Rabbit_game(), "Cannot Found " + wav);
        }

    }

    public void display(GLAutoDrawable gld) {

        gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        switch (state) {
            case 0:
                StartMenu(gl);
                menu = true;
                break;
            case 1:
                Play();
                timer--;
                break;
            case 2:
                StartLevels();
                break;
            case 3:
                StartEasy();
                timer--;
                break;
            case 4:
                StartMid();
                timer--;
                break;
            case 5:
                StartHard();
                timer--;
                break;
            case 6:
                EndGame();
                break;

            case 7:
                GameOver();
                break;
            case 8:
                Winner();
                break;
            case 9:
                help();
                break;
            case 10:
                pause();
                break;

        }

    }

    public void DrawBackground(GL gl, int index) {

        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawSprite(GL gl, int x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    private void StartMenu(GL gl) {
        DrawBackground(gl, 4);

    }

    private void help() {
        DrawBackground(gl, 10);
        DrawSprite(gl, 87, 3, 12, 1.2f);//back_button
        howPlay = true;

    }

    private void Play() {
        DrawBackground(gl, 0);

        DrawSprite(gl, 86, 87, 13, 1.2f);//pause_button

        if (gameOver) {
            score = 0;
        }
        pit = (int) (Math.random() * 4);
        if (pit == 1) {
            x = 16;
            y = 11;
            DrawSprite(gl, 16, 11, 3, 1.6f);

        }
        if (pit == 2) {
            x = 44;
            y = 26;
            DrawSprite(gl, 44, 26, 3, 1.6f);
        }
        if (pit == 3) {
            x = 70;
            y = 14;
            DrawSprite(gl, 70, 14, 3, 1.6f);
        }

        Score();

        IsDead();
        Win();

        if (hit) {
            DrawSprite(gl, xPosition, yPosition, 8, 2);
            hit = false;
        } else {
            DrawSprite(gl, xPosition, yPosition, 9, 2);
        }

        level = false;
        easy = false;
        game = true;
        isPause = true;
        g = true;
        resume = false;
        back=false;
    }

    private void StartLevels() {

        DrawBackground(gl, 6);
        DrawSprite(gl, 87, 3, 12, 1.2f);
        level = true;

    }

    private void StartEasy() {
        DrawBackground(gl, 0);
        DrawSprite(gl, 86, 87, 13, 1.2f);
        if (gameOver) {
            score = 0;
        }
        pit = (int) (Math.random() * 4);
        if (pit == 1) {
            x = 16;
            y = 11;
            DrawSprite(gl, 16, 11, 3, 1.6f);
        }
        if (pit == 2) {
            x = 44;
            y = 26;
            DrawSprite(gl, 44, 26, 3, 1.6f);
        }
        if (pit == 3) {
            x = 70;
            y = 14;
            DrawSprite(gl, 70, 14, 3, 1.6f);
        }

        Score();

        IsDead();
        Win();

        if (hit) {
            DrawSprite(gl, xPosition, yPosition, 8, 2);
            hit = false;
        } else {
            DrawSprite(gl, xPosition, yPosition, 9, 2);
        }

        level = false;
        s = true;
        easy = false;
        game = true;
        isPause = true;
        resume = false;
        back=false;
    }

    private void StartMid() {
        DrawBackground(gl, 1);
        DrawSprite(gl, 86, 87, 13, 1.2f);//pause_button
        if (gameOver) {
            score = 0;
        }
        pit = (int) (Math.random() * 16);
        if (pit == 1) {
            x = 19;
            y = 32;
            DrawSprite(gl, 19, 32, 3, 1.6f);
        }
        if (pit == 2) {
            x = 21;
            y = 15;
            DrawSprite(gl, 21, 15, 3, 1.6f);
        }
        if (pit == 3) {
            x = 46;
            y = 26;
            DrawSprite(gl, 46, 26, 3, 1.6f);
        }

        if (pit == 4) {
            x = 66;
            y = 13;
            DrawSprite(gl, 66, 13, 3, 1.6f);
        }
        if (pit == 5) {
            x = 85;
            y = 27;
            DrawSprite(gl, 85, 27, 3, 1.6f);
        }
        if (pit == 8) {
            x = 19;
            y = 32;
            DrawSprite(gl, 19, 32, 11, 1.2f);
        }
        if (pit == 9) {
            x = 21;
            y = 15;
            DrawSprite(gl, 21, 15, 11, 1.2f);
        }
        if (pit == 10) {
            x = 46;
            y = 26;
            DrawSprite(gl, 46, 26, 11, 1.2f);
        }

        if (pit == 11) {
            x = 66;
            y = 13;
            DrawSprite(gl, 66, 13, 11, 1.2f);
        }
        if (pit == 12) {
            x = 85;
            y = 27;
            DrawSprite(gl, 85, 27, 11, 1.2f);
        }

        Score();

        IsDead();
        Win();

        if (hit) {
            DrawSprite(gl, xPosition, yPosition, 8, 2);
            hit = false;
        } else {
            DrawSprite(gl, xPosition, yPosition, 9, 2);
        }

        level = false;
        easy = false;
        game = false;
        m = true;
        isPause = true;
        resume = false;
        back=false;
    }

    private void StartHard() {

        DrawBackground(gl, 2);
        DrawSprite(gl, 86, 87, 13, 1.2f);//pause_button
        if (gameOver) {
            score = 0;
        }
        pit = (int) (Math.random() * 16);
        if (pit == 1) {
            x = 13;
            y = 30;
            DrawSprite(gl, 13, 30, 3, 1.6f);
        }
        if (pit == 2) {
            x = 17;
            y = 15;
            DrawSprite(gl, 17, 15, 3, 1.6f);
        }
        if (pit == 3) {
            x = 38;
            y = 26;
            DrawSprite(gl, 38, 26, 3, 1.6f);
        }

        if (pit == 4) {
            x = 41;
            y = 8;
            DrawSprite(gl, 41, 8, 3, 1.6f);
        }
        if (pit == 5) {
            x = 58;
            y = 19;
            DrawSprite(gl, 58, 19, 3, 1.6f);
        }
        if (pit == 6) {
            x = 73;
            y = 30;
            DrawSprite(gl, 73, 30, 3, 1.6f);
        }
        if (pit == 7) {
            x = 82;
            y = 17;
            DrawSprite(gl, 82, 17, 3, 1.6f);
        }

        if (pit == 8) {
            x = 13;
            y = 30;
            DrawSprite(gl, 13, 30, 11, 1.2f);
        }
        if (pit == 9) {
            x = 17;
            y = 15;
            DrawSprite(gl, 17, 15, 11, 1.2f);
        }
        if (pit == 10) {
            x = 38;
            y = 26;
            DrawSprite(gl, 38, 26, 11, 1.2f);
        }

        if (pit == 11) {
            x = 41;
            y = 8;
            DrawSprite(gl, 41, 8, 11, 1.2f);
        }
        if (pit == 12) {
            x = 58;
            y = 19;
            DrawSprite(gl, 58, 19, 11, 1.2f);
        }
        if (pit == 13) {
            x = 73;
            y = 30;
            DrawSprite(gl, 73, 30, 11, 1.2f);
        }
        if (pit == 14) {
            x = 82;
            y = 17;
            DrawSprite(gl, 82, 17, 11, 1.2f);
        }

        Score();

        IsDead();
        Win();

        if (hit) {
            DrawSprite(gl, xPosition, yPosition, 8, 2);
            hit = false;
        } else {
            DrawSprite(gl, xPosition, yPosition, 9, 2);
        }

        level = false;
        easy = false;
        game = true;
        h = true;
        isPause = true;
        resume = false;
        back=false;

    }

    private void EndGame() {
        System.exit(0);
        exit = true;
    }

    private void GameOver() {

        back = true;
        timer = 100;
        DrawBackground(gl, 7);
        DrawSprite(gl, 87, 3, 12, 1.2f);//back_button
        tr.beginRendering(300, 300);
        tr.setColor(Color.white);
        tr.draw("Your score : " + score, 80, 230);
        tr.setColor(Color.WHITE);
        tr.endRendering();

    }

    private void Winner() {
        back = true;
        DrawBackground(gl, 5);
        DrawSprite(gl, 87, 3, 12, 1.2f);//back_button
    }

    private void Score() {
        tr.beginRendering(300, 300);
        tr.setColor(Color.BLACK);
        tr.draw("Score : " + score, 8, 280);
        tr.draw("Timer : " + timer, 8, 260);
        tr.setColor(Color.WHITE);
        tr.endRendering();
    }

    public void Win() {

        if (score >= 10) {
            state = 8;
            score = 0;
            timer = 100;
        }
    }

    public void IsDead() {
        if (Math.abs(x - xPosition) < 40 && Math.abs(y - yPosition) < 40) {
            dead = true;
        }
        if (timer <= 0) {
            state = 7;
        }

    }

    public void pause() {

        DrawBackground(gl, 14);
        resume = true;
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
        Component c = e.getComponent();
        double width = c.getWidth();
        double height = c.getHeight();
        xPosition = (int) ((x / width) * 100);
        yPosition = (int) ((y / height) * 100);
        yPosition = 100 - yPosition;
    }

    @Override

    public void mouseClicked(MouseEvent e) {

        double x1 = e.getX();
        double y1 = e.getY();
        Component c = e.getComponent();
        double width = c.getWidth();
        double height = c.getHeight();
        xPosition = (int) ((x1 / width) * 100);
        yPosition = ((int) ((y1 / height) * 100));

        yPosition = 100 - yPosition;
        System.out.println(xPosition + " " + yPosition);

        if (menu) {
            //********* ///play///***********////////////
            if (xPosition < 27 && xPosition > 6 && yPosition < 77 && yPosition > 65) {
                game = true;
                level = false;
                menu = false;
                score = 0;
                timer = 100;
                state = 1;
            }
            if (xPosition < 27 && xPosition > 6 && yPosition < 62 && yPosition > 50) {
                state = 2;
                game = false;
                level = false;
                menu = false;
            }
            //********* ///exit///***********////////////
            if (xPosition < 27 && xPosition > 6 && yPosition < 46 && yPosition > 35) {
                game = false;
                level = false;
                menu = false;
                state = 6;
            }
            ////***////help/////****/////
            if (xPosition < 99 && xPosition > 81 && yPosition < 64 && yPosition > 42) {
                game = false;
                level = false;
                menu = false;
                state = 9;

            }

        }
        if (level) {
            game = false;
            level = false;
            menu = false;
            exit = false;

            ///****easy***///
            if (xPosition < 70 && xPosition > 19 && yPosition < 89 && yPosition > 71) {
                score = 0;
                timer = 100;
                state = 3;
            }

            ///****mid***///
            if (xPosition < 70 && xPosition > 19 && yPosition < 63 && yPosition > 46) {
                score = 0;
                timer = 100;
                state = 4;

            }
            ///****hard***///
            if (xPosition < 70 && xPosition > 19 && yPosition < 37 && yPosition > 21) {
                score = 0;
                timer = 100;
                state = 5;
            }
            if (xPosition < 96 && xPosition > 86 && yPosition < 13 && yPosition > 4) {
                state = 0;
            }
        }

        if (howPlay) {
            game = false;
            level = false;
            menu = false;
            exit = false;
            if (xPosition < 96 && xPosition > 86 && yPosition < 13 && yPosition > 4) {
                state = 0;
            }
        }

        if (back) {
            game = false;
            level = false;
            menu = false;
            exit = false;
            if (xPosition < 96 && xPosition > 86 && yPosition < 13 && yPosition > 4) {
                state = 0;
            }
        }

        if (ishit(xPosition, x, yPosition, y)) {
            if (pit >= 8) {
                score--;
            } else {
                score++;
            }
            hit = true;

        }

        if (isPause) {
            game = false;
            level = false;
            menu = false;
            exit = false;
            if (xPosition < 96 && xPosition > 85 && yPosition < 99 && yPosition > 87) {
                state = 10;
            }

        }
        /////////////////////////////////////////////////////////////////////////

        if (resume) {
            game = false;
            level = false;
            menu = false;
            exit = false;

            if (xPosition < 68 && xPosition > 30 && yPosition < 66 && yPosition > 55) {//button_resume

                if (g) {
                    timer = timer;
                    score = score;
                    state = 1;
                    g = true;
                    isPause = true;

                }
                if (s) {
                    timer = timer;
                    score = score;
                    state = 3;
                    s = true;
                    isPause = true;
                    resume = false;
                }
                if (m) {
                    timer = timer;
                    score = score;
                    state = 4;
                    m = true;
                    isPause = true;
                    resume = false;
                }
                if (h) {
                    timer = timer;
                    score = score;
                    state = 5;
                    h = true;
                    isPause = true;
                    resume = false;
                }
            }
            ///////////////////////////////////////////////////
            if (xPosition < 68 && xPosition > 30 && yPosition < 50 && yPosition > 37) {//button_restart

                if (g) {
                    timer = 100;
                    score = 0;
                    state = 1;
                    g = true;
                    isPause = true;
                    resume = false;
                }
                if (s) {
                    timer = 100;
                    score = 0;
                    state = 3;
                    s = true;
                    isPause = true;
                    resume = false;
                }
                if (m) {
                    timer = 100;
                    score = 0;
                    state = 4;
                    m = true;
                    isPause = true;
                    resume = false;
                }
                if (h) {
                    timer = 100;
                    score = 0;
                    state = 5;
                    h = true;
                    isPause = true;
                    resume = false;
                }

            }
///////////////////////////////////////////////////////////////////////////////////////
            if (xPosition < 68 && xPosition > 30 && yPosition < 32 && yPosition > 21) {//button_exit

                if (g) {

                    state = 0;
                }
                if (s) {

                    state = 0;

                }
                if (m) {

                    state = 0;
                }
                if (h) {
                    state = 0;
                }
                resume = false;

            }

        }
    }

    public boolean ishit(int x1, int x2, int y1, int y2) {
        if (Math.abs(x1 - x2) < 20 && Math.abs(y1 - y2) < 3) {
            return true;
        }
        return false;
    }

}












/*





******************************************************************************************************
                                                                                                           
*        ==       ==                        ||||||                   |||||||                             * 
       //   \\  //  \\                      ||                       ||   ||
*     //      ||     \\ ******************  |||||| ***************** ||||||| ********************        *
     //               \\                    ||                       ||\\
*                                           ||||||                   ||  \\                              *
                                                                     ||    \\
*                                                                                                        *
    ||\\
*   ||   \\                            //\\                                                              *
    ||    \\                          //  \\
*   ||    \\    *****************    //____\\                                                            *
    ||   \\                         //      \\
*   ||||\\                         //        \\                                                          *
    
    ******************************************************************************************************





    */