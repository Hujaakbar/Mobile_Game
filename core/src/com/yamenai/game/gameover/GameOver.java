package com.yamenai.game.gameover;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.yamenai.game.YameNai;
import com.yamenai.game.cordinator.Cordinator;

import sun.misc.Resource;

import static com.badlogic.gdx.Input.Keys.R;

public class GameOver extends Stage implements Screen {

    Texture background;

    final Cordinator game;

    OrthographicCamera camera;

    Stage stage;
    public GameOver(final Cordinator game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Gdx.input.setCatchKey(Input.Keys.BACK, true);


    }
    //new code
    @Override
    public void draw() {
        act(Gdx.graphics.getDeltaTime());

            super.draw();

    }
    //until here


    @Override
    public void show() {
        stage.draw();
        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);

        table.center();

        stage.addActor(table);

        Skin skin = new Skin(Gdx.files.internal("skin/rainbow-ui.json"));


        TextButton reStart = new TextButton("Restart", skin);
        TextButton exit = new TextButton("Exit", skin);
        background = new Texture("background1.png");

        reStart.getLabel();
        //.setFontScale(1.5f, 1.5f)
        exit.getLabel();
        Image image = new Image(background);


        table.add(reStart);
        //.size(700, 150)
        table.row();
        table.add(exit);
        table.row();
//        table.add(image);
//        table.pack();
//        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("background1.png"))));
//        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("kojo1.png"))));
//        table.setBackground( skin.getDrawable("background1.png"));
//        table.pack();


        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        reStart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new YameNai(game, "fuck"));
            }
        });


    }

    @Override
    public void render(float delta) {
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
//        stage.getBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());





//
//
        game.batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

//        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

//        game.font.draw(game.batch, "Welcome to Drop!!! ", 100, 150);
//        game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
//
//        stage.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
