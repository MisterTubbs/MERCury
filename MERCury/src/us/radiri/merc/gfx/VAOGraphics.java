package us.radiri.merc.gfx;

import static org.lwjgl.opengl.GL11.glScalef;

import org.lwjgl.opengl.GL11;

import us.radiri.merc.fmwk.Runner;
import us.radiri.merc.font.Font;
import us.radiri.merc.font.TrueTypeFont;
import us.radiri.merc.geo.Circle;
import us.radiri.merc.geo.Ellipse;
import us.radiri.merc.geo.Point;
import us.radiri.merc.geo.Rectangle;
import us.radiri.merc.geo.Triangle;
import us.radiri.merc.geo.Vec2;

/**
 * An object used for graphics. It will draw just about anything for you.
 * 
 * @author wessles, Jeviny
 */

public class VAOGraphics implements Graphics {
    private VAOBatcher batcher;
    
    private Vec2 scale;
    private Font current_font;
    private Color background_color;
    private Color current_color;
    
    @Override
    public void init() {
        batcher = new VAOBatcher();
        scale = new Vec2(1, 1);
        
        current_font = TrueTypeFont.OPENSANS_REGULAR;
        
        background_color = Color.DEFAULT_BACKGROUND;
        current_color = Color.DEFAULT_DRAWING;
    }
    
    @Override
    public void pre() {
        batcher.begin();
        
        GL11.glClearColor(background_color.r, background_color.g, background_color.b, background_color.a);
    }
    
    @Override
    public void post() {
        batcher.end();
    }
    
    @Override
    public void setDrawMode(int mode) {
        if (getDrawMode() == mode)
            return;
        
        batcher.flush();
        batcher.setDrawMode(mode);
    }
    
    @Override
    public int getDrawMode() {
        return batcher.getDrawMode();
    }
    
    @Override
    public void setLineWidth(float width) {
        GL11.glLineWidth(width);
    }
    
    @Override
    public void scale(float factor) {
        scale(factor, factor);
    }
    
    @Override
    public void scale(float x, float y) {
        Camera cam = Runner.getInstance().getCamera();
        
        GL11.glTranslatef(cam.getOrigin().x, cam.getOrigin().y, 0);
        glScalef(x, y, 1);
        GL11.glTranslatef(-cam.getOrigin().x, -cam.getOrigin().y, 0);
        
        scale.x = x;
        scale.y = y;
        
        // Would you believe me if I told you this almost got into beta?
        // Hawhawhaw... The competancy.
        // Logger.debug(x + ", " + y + " | " + scale.toString());
    }
    
    @Override
    public Vec2 getScale() {
        return scale;
    }
    
    @Override
    public void setFont(Font font) {
        current_font = font;
    }
    
    @Override
    public Font getFont() {
        return current_font;
    }
    
    @Override
    public void setBackground(Color color) {
        background_color = color;
    }
    
    @Override
    public Color getBackground() {
        return background_color;
    }
    
    @Override
    public void setColor(Color color) {
        current_color = color;
        
        batcher.setColor(current_color);
    }
    
    Color push_color = null;
    boolean pull = true;
    
    @Override
    public void pushSetColor(Color color) {
        push_color = current_color;
        current_color = color;
        
        batcher.setColor(current_color);
    }
    
    private void pullSetColor() {
        if (push_color == null || !pull)
            return;
        
        current_color = push_color;
        push_color = null;
        
        batcher.setColor(current_color);
    }
    
    @Override
    public Color getColor() {
        return current_color;
    }
    
    @Override
    public void useShader(Shader shad) {
        batcher.setShader(shad);
    }
    
    @Override
    public void releaseShaders() {
        batcher.clearShaders();
    }
    
    @Override
    public Batcher getBatcher() {
        return batcher;
    }
    
    @Override
    public void drawRawVertices(VAOBatcher.VertexData... verts) {
        if (verts.length % 3 != 0)
            throw new IllegalArgumentException("Vertices must be in multiples of 3!");
        
        for (VAOBatcher.VertexData vdo : verts)
            batcher.vertex(vdo);
    }
    
    @Override
    public void flush() {
        batcher.flush();
    }
    
    @Override
    public void drawString(float x, float y, String msg) {
        drawString(current_font, x, y, msg);
    }
    
    @Override
    public void drawString(Font font, float x, float y, String msg) {
        if (font instanceof TrueTypeFont) {
            TrueTypeFont jf = (TrueTypeFont) font;
            
            int current_x = 0;
            
            pull = false;
            
            for (int ci = 0; ci < msg.toCharArray().length; ci++) {
                if (msg.toCharArray()[ci] == '\n') {
                    y += jf.getLineHeight();
                    current_x = 0;
                }
                
                TrueTypeFont.IntObject intobj = jf.chars[msg.toCharArray()[ci]];
                
                drawTexture(jf.font_tex, intobj.x, intobj.y, intobj.x + intobj.w, intobj.y + intobj.h, x + current_x, y, intobj.w, intobj.h);
                current_x += intobj.w;
            }
            
            pull = true;
            pullSetColor();
        }
    }
    
    @Override
    public void drawTexture(Texture texture, float x, float y) {
        drawTexture(texture, x, y, texture.getTextureWidth(), texture.getTextureHeight());
    }
    
    @Override
    public void drawTexture(Texture texture, float x, float y, float w, float h) {
        drawTexture(texture, 0, 0, texture.getTextureWidth(), texture.getTextureHeight(), x, y, w, h);
    }
    
    @Override
    public void drawTexture(Texture texture, float sx1, float sy1, float sx2, float sy2, float x, float y) {
        drawTexture(texture, sx1, sy1, sx2, sy2, x, y, texture.getTextureWidth(), texture.getTextureHeight());
    }
    
    @Override
    public void drawTexture(Texture texture, float sx1, float sy1, float sx2, float sy2, float x, float y, float w, float h) {
        drawTexture(texture, sx1, sy1, sx2, sy2, x, y, x + w, y, x + w, y + h, x, y + h);
    }
    
    @Override
    public void drawTexture(Texture texture, float sx1, float sy1, float sx2, float sy2, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        drawTexture(texture, sx1, sy1, sx2, sy2, new Rectangle(x1, y1, x2, y2, x3, y3, x4, y4));
    }
    
    @Override
    public void drawTexture(Texture texture, Rectangle rect) {
        drawTexture(texture, 0, 0, texture.getTextureWidth(), texture.getTextureHeight(), rect);
    }
    
    @Override
    public void drawTexture(Texture texture, float sx1, float sy1, float sx2, float sy2, Rectangle rectangle) {
        float x1 = rectangle.getVertices()[0].x;
        float y1 = rectangle.getVertices()[0].y;
        float x2 = rectangle.getVertices()[1].x;
        float y2 = rectangle.getVertices()[1].y;
        float x3 = rectangle.getVertices()[2].x;
        float y3 = rectangle.getVertices()[2].y;
        float x4 = rectangle.getVertices()[3].x;
        float y4 = rectangle.getVertices()[3].y;
        
        float w = texture.getTextureWidth();
        float h = texture.getTextureHeight();
        
        sy1 = h - sy1;
        sy2 = h - sy2;
        
        sx1 /= w;
        sy1 /= h;
        sx2 /= w;
        sy2 /= h;
        
        boolean linemode = getDrawMode() == MODE_LINE;
        if (linemode)
            setDrawMode(MODE_FILL);
        
        batcher.setTexture(texture);
        
        batcher.vertex(x1, y1, sx1, sy1);
        batcher.vertex(x2, y2, sx2, sy1);
        batcher.vertex(x4, y4, sx1, sy2);
        
        batcher.vertex(x3, y3, sx2, sy2);
        batcher.vertex(x4, y4, sx1, sy2);
        batcher.vertex(x2, y2, sx2, sy1);
        
        if (linemode)
            setDrawMode(MODE_LINE);
        
        pullSetColor();
    }
    
    @Override
    public void drawTexture(SubTexture texture, float x, float y) {
        drawTexture(texture, x, y, texture.getWidth(), texture.getHeight());
    }
    
    @Override
    public void drawTexture(SubTexture texture, float x, float y, float w, float h) {
        drawTexture(texture, new Rectangle(x, y, w, h));
    }
    
    @Override
    public void drawTexture(SubTexture texture, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        drawTexture(texture, new Rectangle(x1, y1, x2, y2, x3, y3, x4, y4));
    }
    
    @Override
    public void drawTexture(SubTexture texture, Rectangle rect) {
        drawTexture(texture.getParent(), texture.getX(), texture.getY(), texture.getX() + texture.getWidth(), texture.getY() + texture.getHeight(), rect);
    }
    
    @Override
    public void drawRect(Rectangle rectangle) {
        batcher.clearTextures();
        
        float x1 = rectangle.getVertices()[0].x;
        float y1 = rectangle.getVertices()[0].y;
        float x2 = rectangle.getVertices()[1].x;
        float y2 = rectangle.getVertices()[1].y;
        float x3 = rectangle.getVertices()[2].x;
        float y3 = rectangle.getVertices()[2].y;
        float x4 = rectangle.getVertices()[3].x;
        float y4 = rectangle.getVertices()[3].y;
        
        if (getDrawMode() == MODE_FILL) {
            batcher.vertex(x1, y1, 0, 0);
            batcher.vertex(x2, y2, 0, 0);
            batcher.vertex(x4, y4, 0, 0);
            
            batcher.vertex(x3, y3, 0, 0);
            batcher.vertex(x4, y4, 0, 0);
            batcher.vertex(x2, y2, 0, 0);
        } else {
            batcher.vertex(x1, y1, 0, 0);
            batcher.vertex(x2, y2, 0, 0);
            
            batcher.vertex(x2, y2, 0, 0);
            batcher.vertex(x3, y3, 0, 0);
            
            batcher.vertex(x3, y3, 0, 0);
            batcher.vertex(x4, y4, 0, 0);
            
            batcher.vertex(x4, y4, 0, 0);
            batcher.vertex(x1, y1, 0, 0);
        }
        
        pullSetColor();
    }
    
    @Override
    public void drawRect(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        drawRect(new Rectangle(x1, y1, x2, y2, x3, y3, x4, y4));
    }
    
    @Override
    public void drawRects(Rectangle[] rects) {
        for (Rectangle rect : rects)
            drawRect(rect);
    }
    
    @Override
    public void drawTriangle(Triangle triangle) {
        batcher.clearTextures();
        
        float x1 = triangle.getVertices()[0].x;
        float y1 = triangle.getVertices()[0].y;
        float x2 = triangle.getVertices()[1].x;
        float y2 = triangle.getVertices()[1].y;
        float x3 = triangle.getVertices()[2].x;
        float y3 = triangle.getVertices()[2].y;
        
        if (getDrawMode() == MODE_FILL) {
            batcher.vertex(x1, y1, 0, 1);
            batcher.vertex(x3, y3, 1, 1);
            batcher.vertex(x2, y2, 0, 0);
        } else {
            batcher.vertex(x1, y1, 0, 1);
            batcher.vertex(x3, y3, 1, 1);
            
            batcher.vertex(x3, y3, 1, 1);
            batcher.vertex(x2, y2, 0, 0);
            
            batcher.vertex(x2, y2, 0, 0);
            batcher.vertex(x1, y1, 0, 1);
        }
        
        pullSetColor();
    }
    
    @Override
    public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        drawTriangle(new Triangle(x1, y1, x2, y2, x3, y3));
    }
    
    @Override
    public void drawTriangles(Triangle[] triangles) {
        for (Triangle triangle : triangles)
            drawTriangle(triangle);
    }
    
    @Override
    public void drawEllipse(Ellipse ellipse) {
        batcher.clearTextures();
        
        Vec2[] vs = ellipse.getVertices();
        
        for (int c = 0; c < vs.length; c++) {
            if (getDrawMode() == MODE_FILL)
                batcher.vertex(ellipse.getX() + ellipse.getWidth() / 2, ellipse.getY() + ellipse.getHeight() / 2, 0, 0);
            
            if (c >= vs.length - 1)
                batcher.vertex(vs[0].x, vs[0].y, 0, 0);
            else
                batcher.vertex(vs[c].x, vs[c].y, 0, 0);
            
            if (c >= vs.length - 1)
                batcher.vertex(vs[vs.length - 1].x, vs[vs.length - 1].y, 0, 0);
            else
                batcher.vertex(vs[c + 1].x, vs[c + 1].y, 0, 0);
        }
        
        pullSetColor();
    }
    
    @Override
    public void drawEllipse(float x, float y, float radx, float rady) {
        drawEllipse(new Ellipse(x, y, radx, rady));
    }
    
    @Override
    public void drawEllipses(Ellipse[] ellipses) {
        for (Ellipse ellipse : ellipses)
            drawEllipse(ellipse);
    }
    
    @Override
    public void drawCircle(Circle circle) {
        drawEllipse(circle);
    }
    
    @Override
    public void drawCircle(float x, float y, float radius) {
        drawCircle(new Circle(x, y, radius));
    }
    
    @Override
    public void drawCircles(Circle[] circs) {
        for (Circle circ : circs)
            drawCircle(circ);
    }
    
    @Override
    public void drawLine(float x1, float y1, float x2, float y2) {
        drawLine(new Vec2(x1, y1), new Vec2(x2, y2));
    }
    
    @Override
    public void drawLine(Vec2 p1, Vec2 p2) {
        boolean modefill = getDrawMode() == MODE_FILL;
        if (modefill)
            setDrawMode(MODE_LINE);
        
        batcher.clearTextures();
        
        batcher.vertex(p1.x, p1.y, 0, 0);
        batcher.vertex(p2.x, p2.y, 1, 1);
        
        if (modefill)
            setDrawMode(MODE_FILL);
        
        pullSetColor();
    }
    
    @Override
    public void drawPoint(Point point) {
        float x = point.getX();
        float y = point.getY();
        drawRect(new Rectangle(x, y, x + 1, y, x + 1, y + 1, x, y + 1));
    }
    
    @Override
    public void drawPoint(float x, float y) {
        drawPoint(new Point(x, y));
    }
    
    @Override
    public void drawPoints(Point[] points) {
        for (Point point : points)
            drawPoint(point);
    }
}