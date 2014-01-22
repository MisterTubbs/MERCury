package com.teama.merc.gui;

import java.util.ArrayList;

import com.teama.merc.env.Renderable;
import com.teama.merc.gfx.Graphics;

/**
 * @from merc in com.teama.merc.gui
 * @authors wessles
 * @website www.wessles.com
 * @license (C) Dec 23, 2013 www.wessles.com This file, and all others of the project 'MERCury' are licensed under WTFPL license. You can find the license itself at http://www.wtfpl.net/about/.
 */

public class GUISystem implements Renderable
{
    public final ArrayList<Component> components = new ArrayList<Component>();
    
    public GUISystem addComponent(Component c)
    {
        components.add(c);
        return this;
    }
    
    public void update()
    {
        for (Component c : components)
            c.update();
    }
    
    @Override
    public void render(Graphics g)
    {
        for (Component c : components)
            c.render(g);
    }
}
