package com.harsay.ld31;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class PlayScreen extends GameScreen {
	
	Player player;
	
	public float time = 0;
	
	public boolean gameLive = false;

	public PlayScreen(MyGame game) {
		super(game);
		float c = (float) 32 / (float) 255;
		backgroundColor.set(c, c, c, 1);
		player = new Player(game, MyGame.WIDTH/2, MyGame.HEIGHT/2, 30);
		//addObstacle(600, 0, 300, 400, 1.0f, 1.0f);
		/*for(int i=1; i<=20; i++) {
			addObstacle(400*i, MyGame.HEIGHT-(500-100*i), 300, (500-100*i), 1.0f, new Goal[] {
					new Goal(0, MyGame.HEIGHT-100, -1f, 100, 1.0f*i)
			});
			addObstacle(400*i, 0, 300, 400-100*i, 1.0f, new Goal[] {
					new Goal(0, 0, -1f, 900, 1.0f*i)
			});
		}*/
	}
	
	public void update(float delta) {
		super.update(delta);
		
		if(!gameLive) {
			if(Gdx.input.isKeyPressed(GameKeys.UP) || Gdx.input.isKeyPressed(GameKeys.DOWN) || Gdx.input.isKeyPressed(GameKeys.LEFT) || Gdx.input.isKeyPressed(GameKeys.RIGHT)) {
				gameLive = true;
				Stopwatch.start();
			} else {
				return;
			}
		}
		
		Stopwatch.update(delta);
		player.update(delta);
		if(player.alive) {
			for(int i=0; i < game.obstacles.size(); i++) {
				Obstacle o = game.obstacles.get(i);
				o.update(delta);
				if(!o.spawning && !o.killed && player.collision(player.circle, o.rectangle)) {
					endGame();
				}
			}
		}
	}
	
	public void draw(float delta) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.begin(ShapeType.Filled);
		{
			player.draw(shapeRenderer, spriteBatch);
			for(int i=0; i < game.obstacles.size(); i++) {
				Obstacle o = game.obstacles.get(i);
				o.draw(shapeRenderer, spriteBatch);
			}
		}
		shapeRenderer.end();		
		
		spriteBatch.begin();
		{
			// HUD AND STUFF
			if(!gameLive) {
				game.assets.big.setColor(1, 1, 1, 1);
				game.assets.big.drawMultiLine(spriteBatch, "ULTRA REKTAGON", 130, 800);
				game.assets.medium.draw(spriteBatch, "Press arrows to move.", 500, 400);
				game.assets.small.draw(spriteBatch, "Game made by Harsay for Ludum Dare 31 compo", 460, 36);
			}
			game.assets.medium.draw(spriteBatch, Stopwatch.getString(), 20, MyGame.HEIGHT-20);
		}
		spriteBatch.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	
	public void endGame() {
		game.obstacles.clear();
		game.setScreen(new PlayScreen(game));
		player.kill();
		Stopwatch.stop();
		// TODO: end sequence
	}
	
	public void addObstacle(float x, float y,float width, float height, float tts, float ta) {
		game.obstacles.add(new Obstacle(game, x, y, width, height, tts, ta));
	}
	
	public void addObstacle(float x, float y,float width, float height, float tts, Goal[] goals) {
		game.obstacles.add(new Obstacle(game, x, y, width, height, tts, goals));
	}

}
