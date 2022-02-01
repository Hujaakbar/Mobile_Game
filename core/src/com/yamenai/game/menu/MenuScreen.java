package com.yamenai.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.yamenai.game.YameNai;
import com.yamenai.game.cordinator.Cordinator;
import com.badlogic.gdx.graphics.GL20;
import com.yamenai.game.levelScore;

public class MenuScreen implements Screen {

    final Cordinator game;

    OrthographicCamera camera;
    private Texture background;

    Stage stage;
    boolean a=true;
    boolean b;


    public MenuScreen(final Cordinator game) {
        this.game = game;
//        a=true;


        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Gdx.input.setCatchKey(Input.Keys.BACK, true);


    }

    levelScore levelScore = new levelScore();

    //public MainMenuScreen(final Drop game)....

    @Override
    public void show() {
        stage.draw();
        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
//        table.setDebug(true);
        stage.addActor(table);

        Skin skin = new Skin(Gdx.files.internal("skin/rainbow-ui.json"));


        TextButton newGame = new TextButton("   New Game   ", skin);
        TextButton resume = new TextButton("   Resume   ", skin);
        TextButton exit = new TextButton("   Exit   ", skin);

        newGame.getLabel().setFontScale(1.5f, 1.5f);
        exit.getLabel().setFontScale(1.5f, 1.5f);

        table.add(newGame).size(700, 150);
        table.row();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("mountainss.png"))));


        if(game.resumable){
            resume.getLabel().setFontScale(1.5f, 1.5f);
            table.add(resume).size(700, 150);
            table.row();
        }

        table.add(exit).size(700, 150);
        table.row();


        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new YameNai(game, "runNewGame"));
            }
        });

        resume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new YameNai(game, "resume"));
            }
        });
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            game.resumable=true;

            Gdx.app.exit();


        }

        stage.draw();
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
//        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//
        game.batch.end();

//
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
        game.batch.dispose();
        stage.dispose();

    }

    //Rest of class still omitted...

}
