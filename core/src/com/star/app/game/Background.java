package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.StarGame;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Background {
    private class Star {
        private Vector2 position;
        private Vector2 velocity;
        private float scale;


        public Star() {
            this.position = new Vector2(MathUtils.random(-200, ScreenManager.SCREEN_WIDTH + 200),
                    MathUtils.random(-200, ScreenManager.SCREEN_HEIGHT + 200));
            this.velocity = new Vector2(MathUtils.random(-40, -5), 0);
            this.scale = Math.abs(velocity.x) / 40f * 0.8f;
        }

        public void update(float dt) {
            position.x += (velocity.x - gc.getHero().getVelocity().x * 0.1) * dt;
            position.y += (velocity.y - gc.getHero().getVelocity().y * 0.1) * dt;

            if (position.x < -200) {
                position.x = ScreenManager.SCREEN_WIDTH + 200;
                position.y = MathUtils.random(-200, ScreenManager.SCREEN_HEIGHT + 200);
                scale = Math.abs(velocity.x) / 40f * 0.8f;
            }
        }
    }

    private final int STAR_COUNT = 1000;
    private GameController gc;
    private Texture textureCosmos;
    private TextureRegion textureStar;
    private Star[] stars;
    private StringBuilder stringBuilder;
    private boolean pause;

    public Background(GameController gc) {
        this.textureCosmos = new Texture("images/bg.png");
        this.textureStar = Assets.getInstance().getAtlas().findRegion("star16");
        this.gc = gc;
        this.stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star();
        }
        this.stringBuilder = new StringBuilder();
        this.pause = false;

    }

    public void render(SpriteBatch batch) {
        batch.draw(textureCosmos, 0, 0);

        for (int i = 0; i < stars.length; i++) {
            batch.draw(textureStar, stars[i].position.x - 8, stars[i].position.y - 8, 8, 8, 16, 16,
                    stars[i].scale, stars[i].scale, 0);

            if (MathUtils.random(0, 300) < 1) {
                batch.draw(textureStar, stars[i].position.x - 8, stars[i].position.y - 8, 8, 8, 16, 16,
                        stars[i].scale * 2, stars[i].scale * 2, 0);
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(dt);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.L)) {
            pause = true;
            while (pause) {
                if ((Gdx.input.isKeyPressed(Input.Keys.L))){ /// снятие паузы не работает
                    pause = false;
                }
            }
//            do {
//                if (Gdx.input.isKeyPressed(Input.Keys.L)) {  break; }
//            }
//            while (true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
        }
    }

//    public boolean update2(float dt) {
////        for (int i = 0; i < stars.length; i++) {
////            stars[i].update(dt);
////        }
//        if(Gdx.input.isKeyPressed(Input.Keys.L)) {
//            pause = true;
//        }
//        if ((Gdx.input.isKeyPressed(Input.Keys.K))){ /// снятие паузы не работает
//            pause = false;
//            }
////            do {
////                if (Gdx.input.isKeyPressed(Input.Keys.L)) {
////                    break;
////                }
////            }
////            while (true);
//        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
//            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
//        }
//        return pause;
//    }


    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        stringBuilder.clear();
        stringBuilder.append("L: PAUSE ").append("\n");
        stringBuilder.append("Q: QUIT ").append("\n");
        font.draw(batch, stringBuilder, 1120, 700);
    }

}
