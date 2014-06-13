package us.radiri.merc.gfx;

import org.lwjgl.opengl.GL11;

import us.radiri.merc.font.Font;
import us.radiri.merc.geo.Circle;
import us.radiri.merc.geo.Ellipse;
import us.radiri.merc.geo.Point;
import us.radiri.merc.geo.Rectangle;
import us.radiri.merc.geo.Triangle;
import us.radiri.merc.geo.Vec2;

/**
 * An abstraction for all graphics.
 * 
 * @author wessles, Jeviny
 */

public interface Graphics {
    // Do you get the pun? 0 looks a bit more like a closed figure, symbolizing
    // that it 'fills,' while 1 is more of a line!
    //
    // The more you know.
    // Da DAA da DAAAAAAAAA!
    public final int MODE_FILL = GL11.GL_TRIANGLES, MODE_LINE = GL11.GL_LINES;
    
    /** Initializes the graphics object. */
    public void init();
    
    /** Prepares the graphics object for rendering. */
    public void pre();
    
    /** Cleans up the graphics object at the end of rendering. */
    public void post();
    
    /** Sets the draw mode */
    public void setDrawMode(int mode);
    
    /** Returns the drawing mode that is currently in use. */
    public int getDrawMode();
    
    /** Sets the line width in case of MODE_LINE being the drawmode */
    public void setLineWidth(float width);
    
    /** Scales the matrix. */
    public void scale(float factor);
    
    /**
     * Scales the matrix.
     * 
     * @param x
     *            The scale factor of x
     * @param y
     *            The scale factor of y
     */
    public void scale(float x, float y);
    
    /** @return The last scale that was set in the graphics object. */
    public Vec2 getScale();
    
    /** Sets the font. */
    public void setFont(Font font);
    
    /** @return The last set font. */
    public Font getFont();
    
    /** Sets the background color. */
    public void setBackground(Color col);
    
    /** @return The background color. */
    public Color getBackground();
    
    /** Sets the color. */
    public void setColor(Color color);
    
    /** Sets the color for one drawing, and then sets it to the previous color. */
    public void pushSetColor(Color color);
    
    /** @return The last set color. */
    public Color getColor();
    
    /** Uses a shader. */
    public void useShader(Shader shader);
    
    /** Releases shaders. */
    public void releaseShaders();
    
    /** @return The batcher. */
    public Batcher getBatcher();
    
    /** Pushes raw vertices to the batcher. */
    public void drawRawVertices(VAOBatcher.VertexData... verts);
    
    /** Pushes data in batcher to OGL. */
    public void flush();
    
    /** Draws msg at x and y. */
    public void drawString(float x, float y, String msg);
    
    /** Draws msg at x and y with font. */
    public void drawString(Font font, float x, float y, String msg);
    
    /** Draws texture at x and y. */
    public void drawTexture(Texture texture, float x, float y);
    
    /** Draws texture at x and y, at a width and height. */
    public void drawTexture(Texture texture, float x, float y, float w, float h);
    
    /** Draws a portion of the texture at x and y. */
    public void drawTexture(Texture texture, float sx1, float sy1, float sx2, float sy2, float x, float y);
    
    /** Draws a portion of the texture at x1 and y1, to x2 and y2. */
    public void drawTexture(Texture texture, float sx1, float sy1, float sx2, float sy2, float x1, float y1, float x2, float y2);
    
    /** Draws a portion of the texture to coordinates. */
    public void drawTexture(Texture texture, float sx1, float sy1, float sx2, float sy2, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4);
    
    /** Draws a texture to a rectangle. */
    public void drawTexture(Texture texture, Rectangle rect);
    
    /** Draws a portion of a texture to a rectangle. */
    public void drawTexture(Texture texture, float sx1, float sy1, float sx2, float sy2, Rectangle rect);
    
    /** Draws a subtexture at x and y. */
    public void drawTexture(SubTexture texture, float x, float y);
    
    /** Draws a subtexture at x and y with a width and height. */
    public void drawTexture(SubTexture texture, float x, float y, float w, float h);
    
    /** Draws a subtexture to a set of coordinates. */
    public void drawTexture(SubTexture texture, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4);
    
    /** Draws a subtexture to a rectangle. */
    public void drawTexture(SubTexture texture, Rectangle rect);
    
    /** Draws a rectangle. */
    public void drawRect(Rectangle rectangle);
    
    /** Makes and draws a rectangle. */
    public void drawRect(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4);
    
    /** Draws multiple rectangles. */
    public void drawRects(Rectangle[] rects);
    
    /** Draws a triangle. */
    public void drawTriangle(Triangle triangle);
    
    /** Makes and draws a triangle. */
    public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3);
    
    /** Draws multiple triangles. */
    public void drawTriangles(Triangle[] triangles);
    
    /** Draws an ellipse. */
    public void drawEllipse(Ellipse ellipse);
    
    /** Makes and draws an ellipse. */
    public void drawEllipse(float x, float y, float radx, float rady);
    
    /** Draws multiple ellipses. */
    public void drawEllipses(Ellipse[] ellipses);
    
    /** Draws a circle. */
    public void drawCircle(Circle circle);
    
    /** Makes and draws a circle. */
    public void drawCircle(float x, float y, float radius);
    
    /** Draws multiple circles. */
    public void drawCircles(Circle[] circles);
    
    // Geometry nerds beware! I know it is actually a line SEGMENT, but for
    // simplicity's sake, I will just call it a line, like a normal human being.
    
    /** Draws a line from x1, y1 to x2, y2 */
    public void drawLine(float x1, float y1, float x2, float y2);
    
    /** Draws a line from p1 to p2 */
    public void drawLine(Vec2 p1, Vec2 p2);
    
    /** Draws a point. */
    public void drawPoint(Point point);
    
    /** Makes and draws a point. */
    public void drawPoint(float x, float y);
    
    /** Draws multiple points. */
    public void drawPoints(Point[] points);
}