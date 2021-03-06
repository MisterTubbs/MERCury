package radirius.merc.graphics.gui;

import radirius.merc.graphics.Color;
import radirius.merc.graphics.Graphics;
import radirius.merc.graphics.Texture;
import radirius.merc.graphics.font.Font;
import radirius.merc.graphics.font.TrueTypeFont;

/**
 * @author Jeviny
 */
public class TextButton extends TextBar implements Button {
	protected boolean wasactive;

	public TextButton(String txt, Texture left, Texture right, Texture body, float x, float y, Color textcolor, Font textfont) {
		super(txt, left, right, body, x, y, textcolor, textfont);
	}

	public TextButton(String txt, float x, float y, Color textcolor, Color backgroundcolor) {
		this(txt, null, null, null, x, y, textcolor, TrueTypeFont.OPENSANS_REGULAR);
	}

	public TextButton(String txt, Texture left, Texture right, Texture body, float x, float y, Color textcolor) {
		this(txt, left, right, body, x, y, textcolor, TrueTypeFont.OPENSANS_REGULAR);
	}

	public TextButton(String txt, Texture left, Texture right, Texture body, float x, float y) {
		this(txt, left, right, body, x, y, Color.black);
	}

	public TextButton(String txt, float x, float y) {
		this(txt, getDefaultTextures().getTexture(3), getDefaultTextures().getTexture(3).convertToTexture(true, false), getDefaultTextures().getTexture(4), x, y, Color.white);
	}

	@Override
	public void update(float delta) {
		super.update(delta);

		if (isClicked(bounds))
			wasactive = true;
	}

	@Override
	public void render(Graphics g) {
		super.render(g);
	}

	@Override
	public boolean wasActive() {
		boolean _waspressed = wasactive;

		wasactive = false;

		return _waspressed;
	}
}
