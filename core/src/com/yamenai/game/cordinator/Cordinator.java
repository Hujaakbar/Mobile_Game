package com.yamenai.game.cordinator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yamenai.game.YameNai;
import com.yamenai.game.menu.MenuScreen;


public class Cordinator extends Game {
    public SpriteBatch batch;
    public BitmapFont font;


//    private LoadingScreen loadingScreen;
//    private PreferencesScreen preferencesScreen;
    private MenuScreen menuScreen;
    private YameNai yameNai;
//    private EndScreen endScreen;

    public final static int MENU = 0;
    public final static int PREFERENCES = 1;
    public final static int APPLICATION = 2;
    public final static int ENDGAME = 3;

//    public int level=0;
//    public int score=0;
    public boolean resumable;



    public void create() {
        Preferences preferences= Gdx.app.getPreferences("create");
        preferences.clear();
        resumable= preferences.getBoolean("resumAble", false);

        batch = new SpriteBatch();
        //Use LibGDX's default Arial font.
        font = new BitmapFont();
        if(menuScreen==null){
            menuScreen = new MenuScreen(this);
            preferences.putBoolean("resumAble", true).flush();

        }
        else {
            resumable=true;
        }
        this.setScreen(menuScreen);
//        this.setScreen(new MenuScreen(this));
    }

    public void render() {
        super.render(); //important!
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
