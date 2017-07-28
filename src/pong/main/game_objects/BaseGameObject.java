package pong.main.game_objects;

import pong.main.Collision;
import pong.main.util.Rectangle;

public class BaseGameObject extends BaseScreenObject {

	protected static String $name = ">>uninitialized_BGO<<";

	public Rectangle hBox;

	public BaseGameObject() {
		this(new Rectangle(0, 0, 0, 0));
	}

	public BaseGameObject(Rectangle hBox) {
		this.hBox = hBox;
	}

	@Override
	public void render() {
	}

	@Override
	public void update() {
	}

	@Override
	public void destroy() {		
	}
	
	public String getName() {
		return $name;
	}

	public Rectangle getRect() {
		return hBox;
	}

	public void collision(Collision col) {
	}



}
