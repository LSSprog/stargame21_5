package com.star.app.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;

public class GameController {
    private Background background;
    private AsteroidController asteroidController;
    private BulletController bulletController;
    private ParticleController particleController;
    private PowerUpsController powerUpsController;
    private Hero hero;
    private Vector2 tmpVec;

    public PowerUpsController getPowerUpsController() {
        return powerUpsController;
    }

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public Hero getHero() {
        return hero;
    }

    public Background getBackground() {
        return background;
    }

    public GameController() {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.asteroidController = new AsteroidController(this);
        this.bulletController = new BulletController(this);
        this.particleController = new ParticleController();
        this.powerUpsController = new PowerUpsController(this);
        this.tmpVec = new Vector2(0.0f, 0.0f);
        for (int i = 0; i < 3; i++) {
            asteroidController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-200, 200), MathUtils.random(-200, 200), 1.0f);
        }
    }

    public void update(float dt) {
        background.update(dt);
        hero.update(dt);
        asteroidController.update(dt);
        bulletController.update(dt);
        powerUpsController.update(dt);
        particleController.update(dt);
        checkCollisions();
    }

    public void checkCollisions() {
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);
            if (a.getHitArea().overlaps(hero.getHitArea())) {
                float dst = a.getPosition().dst(hero.getPosition());
                float halfOverLen = (a.getHitArea().radius + hero.getHitArea().radius - dst) / 2.0f;
                tmpVec.set(hero.getPosition()).sub(a.getPosition()).nor();
                hero.getPosition().mulAdd(tmpVec, halfOverLen);
                a.getPosition().mulAdd(tmpVec, -halfOverLen);

                float sumScl = hero.getHitArea().radius * 2 + a.getHitArea().radius;

                hero.getVelocity().mulAdd(tmpVec, 200.0f * a.getHitArea().radius / sumScl);
                a.getVelocity().mulAdd(tmpVec, -200.0f * hero.getHitArea().radius / sumScl);

                if (a.takeDamage(2)) {
                    hero.addScore(a.getHpMax() * 20);
                }
                hero.takeDamage(2);
            }
        }


        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {

                    particleController.setup(
                            b.getPosition().x + MathUtils.random(-4, 4),
                            b.getPosition().y + MathUtils.random(-4, 4),
                            b.getVelocity().x * -0.3f + MathUtils.random(-30, 30),
                            b.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                            0.2f,
                            2.3f, 1.7f,
                            1.0f, 1.0f, 1.0f, 1.0f,
                            0.0f, 0.0f, 1.0f, 0.0f
                    );

                    b.deactivate();
                    if (a.takeDamage(1)) {
                        hero.addScore(a.getHpMax() * 100);
                        for (int k = 0; k < 3; k++) {
                            powerUpsController.setup(a.getPosition().x, a.getPosition().y, a.getScale() / 4.0f);
                        }
                    }
                    break;
                }
            }
        }

        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUp p = powerUpsController.getActiveList().get(i);
            if (hero.getHitArea().contains(p.getPosition())){
                hero.consume(p);
                particleController.getEffectBuilder().takePowerUpEffect(
                        p.getPosition().x, p.getPosition().y);
                p.deactivate();
            }
        }

    }
}
