package com.yamenai.game;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
// add this import and NOT the one in the standard library
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.yamenai.game.cordinator.Cordinator;
import com.yamenai.game.menu.MenuScreen;

public class YameNai extends Stage implements Screen  {

	final Cordinator game;
	// nex code for showing score and level
	BitmapFont font;



	private OrthographicCamera camera;
	private Rectangle actor;

	public MenuScreen menuScreen;

	//adding background
	private Texture background;
	private Texture background2;

	//GAME LOGIC
	//checking whether game is On or game Over, 0 is active, 1-> game over
	int gameStatus=0;
	// to find out whether the actor has dropped to the ground in the beginning of the game
	boolean hasStarted=false;
	// game speed based on level
	int levelSpeed=1;
	// to find out whether the first background began moving beyond the screen
	boolean isBackground1Gone =true;
	// to find out whether the second background began moving beyond the screen
	boolean isBackground2Gone =true;

	//adding main character kojo
	private Texture[] kojo;

	//adding background musics
	private Music intro;


	private Music[] bgMusic;

	private Sound explosionEffect;
	private Sound levelChange;
	private Sound jumpEffect;
	private Sound diamondEffect;



	//scores and levels
	int scoreInt;
	public int levelInt;

	//making diamond and diamond dimensions
	private Texture diamond;
	float diamondX;
	private int waitingTimeForDiamond=0;
	private int randomTimeForDiamond=0;
	Rectangle diamondFrame= new com.badlogic.gdx.math.Rectangle();


	//bomb
	private Texture bomb;
	float bombX;
	private int waitingTimeForBomb=0;
	private int randomTimeForBomb=0;
	Rectangle bombFrame= new com.badlogic.gdx.math.Rectangle();


	//actor dimensions
	private float actorWidth;
	private float actorHeight;


	//background positioning
	int backgroundX=0;
	int backgroundX2;

	private int pause=0;
	private int countForActorImages=0;

	//the speed that actor drops to the ground
	private float velocity=-15f;

	// screen dimensions
	private float screenWidth;
	private float screenHeight;

	//explosion or similar effects
	private Texture explosion;

	//new codee
	Stage stage;
	Stage stageForScore;


	public void playMusic(){
		if(levelInt<12){

			if(bgMusic[levelInt/3].isPlaying()){
				bgMusic[levelInt/3].stop();
			}
			bgMusic[levelInt/3].setLooping(true);
			bgMusic[levelInt/3].setVolume(0.2f);
			bgMusic[levelInt/3].play();}
		else {
			if(bgMusic[3].isPlaying()){
				bgMusic[3].stop();
			}
			bgMusic[3].setLooping(true);
			bgMusic[3].setVolume(0.2f);
			bgMusic[3].play();
		}
	}


	private int randomDiamond(){
		return MathUtils.random(100, 300);
	}
	private int randomBomb(){
		return MathUtils.random(0, 100);
	}


	Preferences preferences;
	boolean actorAlreadyDown;
	levelScore levelScore  = new levelScore();
	boolean resumable=false;

	public YameNai(final Cordinator game, String choice) {
		this.game = game;
		preferences = Gdx.app.getPreferences("options");
		resumable=true;
		preferences.putBoolean("resumable", true).flush();
		game.resumable=preferences.getBoolean("resumable");

//
		//showing score and level

//font.setUseIntegerPositions(false);(Optional)
		font = new BitmapFont();
		font.setColor(Color.RED);
		font.getData().setScale(4);






		camera = new OrthographicCamera();
		//adding new code
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		//new code
		stage = new Stage(new ScreenViewport());

		menuScreen= new MenuScreen(game);
		Gdx.input.setCatchKey(Input.Keys.BACK, true);

		// getting screen dimensions
		screenWidth= Gdx.graphics.getWidth();
		screenHeight= Gdx.graphics.getHeight();

		camera.setToOrtho(false, screenWidth, screenHeight);

		// adding background
		background=new Texture("background1.png");
		background2=new Texture("background2.png");
		backgroundX2=Gdx.graphics.getWidth()-1; //the starting x position of background2


		//adding characters
		//adding onCreate character kojo
		kojo= new Texture[8];
		kojo[0]=new Texture("kojo1.png");
		kojo[1]=new Texture("kojo2.png");
		kojo[2]=new Texture("kojo3.png");
		kojo[3]=new Texture("kojo4.png");
		kojo[4]=new Texture("kojo5.png");
		kojo[5]=new Texture("kojo6.png");
		kojo[6]=new Texture("kojo_fall.png");
		kojo[7]=new Texture("kojo_up.png");



		//adding background musics
		bgMusic= new Music[4];

		intro = Gdx.audio.newMusic(Gdx.files.internal("intro.ogg"));

		bgMusic[0] = Gdx.audio.newMusic(Gdx.files.internal("music1.ogg"));
		bgMusic[1] = Gdx.audio.newMusic(Gdx.files.internal("music2.ogg"));
		bgMusic[2] = Gdx.audio.newMusic(Gdx.files.internal("music3.ogg"));
		bgMusic[3] = Gdx.audio.newMusic(Gdx.files.internal("music4.ogg"));
		explosionEffect=Gdx.audio.newSound(Gdx.files.internal("Chunky Explosion.mp3"));


		levelChange=Gdx.audio.newSound(Gdx.files.internal("levelChange.ogg"));
		jumpEffect=Gdx.audio.newSound(Gdx.files.internal("jumping5.ogg"));
		diamondEffect=Gdx.audio.newSound(Gdx.files.internal("gettingDiamond1.ogg"));
		// start the playback of the background music immediately

//		bgMusic4.setLooping(true);


		//putting actor on rectangle and setting its dimensions
		actor = new Rectangle();
		actor.x = Gdx.graphics.getWidth()/5.5f;
		actor.y = Gdx.graphics.getHeight();
		if(actorAlreadyDown){
			actor.y = Gdx.graphics.getHeight()/8f;
		}
		actor.width = kojo[0].getWidth()/2f-30;
		actor.height = 372-40;
		actorWidth=kojo[0].getWidth()/2f;
		actorHeight=372;

		//bomb
		bomb= new Texture("bomb.png");
		bombX=( Gdx .graphics.getWidth());

		//diamond dimensions
		diamond = new Texture("diamond5.png");
		diamondX=Gdx .graphics.getWidth();

		//resume function

		if (choice.equals("runNewGame")) {
			scoreInt = 0;
			levelInt = 1;
			preferences.clear();
			preferences.putInteger("level", levelInt).flush();
			preferences.putInteger("gameStat", 0).flush();
//			preferences.putInteger("highscore", 0);
//			preferences.putInteger("level", 0);
		}
		else{
			if(preferences.getInteger("gameStat")==0) {
				scoreInt = preferences.getInteger("highscore");
				levelInt = preferences.getInteger("level", 1);
				actor.y = preferences.getFloat("actorY");
				diamondX = preferences.getFloat("diamondX");
				bombX = preferences.getFloat("bombX");
				hasStarted = true;
				actorAlreadyDown = true;
//			if(levelInt<12){
//
//				bgMusic[levelInt/3].play();
//			}
//			else{
//				bgMusic[3].play();
//			}}
			}
			else{
				preferences.putBoolean("resumable", true).flush();
				game.resumable=preferences.getBoolean("resumable");

				scoreInt=0;
				actorAlreadyDown=false;
				bombX=-200;
				diamondX=-200;
				waitingTimeForBomb=0;
				waitingTimeForDiamond=0;
				hasStarted=false;
				levelInt=preferences.getInteger("level");
				actor.y=Gdx.graphics.getHeight();
				gameStatus=0;
			}
		}

		//explosion and similar effects
		explosion=new Texture("explosion.png");


	}

	@Override
	public void draw() {
		act(Gdx.graphics.getDeltaTime());

		super.draw();

	}


	@Override
	public void show() {
		/* start the playback of the background music
		 when the screen is shown */
			playMusic();

		// make buttons
		Gdx.input.setInputProcessor(stage);
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


		reStart.getLabel().setFontScale(1.5f, 1.5f);
		exit.getLabel().setFontScale(1.5f, 1.5f);
		table.add(reStart).size(700, 150);
		//.size(700, 150)
		table.row();
		table.add(exit).size(700, 150);
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
			public void changed(ChangeEvent event, Actor bactor) {
				preferences.putBoolean("resumable", true).flush();
				game.resumable=preferences.getBoolean("resumable");

				scoreInt=0;
				actorAlreadyDown=false;
				bombX=-200;
				diamondX=-200;
				waitingTimeForBomb=0;
				waitingTimeForDiamond=0;
				hasStarted=false;
				actor.y=Gdx.graphics.getHeight();
				gameStatus=0;
				playMusic();
				preferences.putInteger("gameStat", gameStatus).flush();
//				game.setScreen(new MenuScreen(game));

			}
		});

	}

	@Override
	public void render (float delta) {

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();

		// new code checking whether the back key is pressed
		Gdx.input.setCatchKey(Input.Keys.BACK, true);
		if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
			levelScore.setLevel(levelInt);
			game.setScreen(new MenuScreen(game));
			if(levelInt<12){
			bgMusic[levelInt/3].pause();}
			dispose();

		}

		//drawing background
		game.batch.draw(background, backgroundX, 0, Gdx.graphics.getWidth()+2, Gdx.graphics.getHeight());
		game.batch.draw(background2, backgroundX2, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		game.batch.end();
		if (gameStatus == 0) {
			game.batch.begin();
			//drawing actor


			//actor is dropping to the ground
			actor.y += velocity;
			if (actor.y < Gdx.graphics.getHeight() / 8f) {
				actor.y = Gdx.graphics.getHeight() / 8f; //puts actor to certain place
				hasStarted = true; //actor is on the ground so background can start moving

				velocity = 0;}



			if (hasStarted) {
				backgroundX -= levelSpeed;
				backgroundX2 -= levelSpeed;

			}
			if (backgroundX2 <= 0 && isBackground1Gone) {
				isBackground2Gone = true;
				backgroundX = Gdx.graphics.getWidth();
				isBackground1Gone = false;
			}
			if (backgroundX <= 0 && isBackground2Gone) {
				isBackground1Gone = true;
				backgroundX2 = Gdx.graphics.getWidth();
				isBackground2Gone = false;
			}
			//when touched any place on the screen actor jumps
			if (Gdx.input.justTouched()) {
				if (actor.y == Gdx.graphics.getHeight() / 8f) {
					velocity = 12f; //how fast actor jumps
					jumpEffect.play();
				}
			}
			//setting the limit how high the actor can jump
			if (actor.y > Gdx.graphics.getHeight() / 3.5) {
				velocity = -10f; //how fast actor falls
			}

			//setting images for jumping and falling
			if (velocity < 0) {
				//show falling image
				game.batch.draw(kojo[6], Gdx.graphics.getWidth() / 5.5f, actor.y, kojo[6].getWidth()/2f, actorHeight);
			} else if (velocity > 0) {
				//show jumping image

				game.batch.draw(kojo[7], Gdx.graphics.getWidth() / 5.5f, actor.y, kojo[7].getWidth()/2f, actorHeight);
			} else {
				game.batch.draw(kojo[countForActorImages], Gdx.graphics.getWidth() / 5.5f, actor.y, actorWidth, actorHeight);
			}


			//adjusting the change of images
			if (pause < 10 - levelSpeed) {
				pause++;
			} else {
				pause = 0;

				if (countForActorImages < 5) {
					countForActorImages++;
				} else {
					countForActorImages = 0;
				}
			}


			if (waitingTimeForDiamond < randomTimeForDiamond + 350) {
				waitingTimeForDiamond++;
			} else {

				if (!(diamondX < -120 /*when the diamond disappears from the screen completely*/)) {
					diamondX -= 15; /*speed of diamond*/
				} else {
					diamondX = Gdx.graphics.getWidth();
					waitingTimeForDiamond = 0;
					randomTimeForDiamond = randomDiamond();
				}


			}
			game.batch.draw(diamond,diamondX, (float) (Gdx.graphics.getHeight()/6.5), 120, 112 );

			if(waitingTimeForBomb < randomTimeForBomb +100 ){
				waitingTimeForBomb++;
			} else{
				if (!(bombX <-148)){
					bombX-=15; /*speed of bomb*/}
				else{
					bombX=Gdx .graphics.getWidth();
					waitingTimeForBomb =0;
					randomTimeForBomb =randomBomb();
				}

				if (hasStarted){
					game.batch.draw(bomb,   bombX, (float) (Gdx.graphics.getHeight()/6.8), 148, 248);}
			}
			//putting diamond and bomb into rectangle
			diamondFrame.set(diamondX, (float) (Gdx.graphics.getHeight()/6.5), 120, 112);
			bombFrame.set(bombX, (float) (Gdx.graphics.getHeight()/6.8), 60, 150);

			if (Intersector.overlaps(actor, diamondFrame)){
				scoreInt++;
				diamondEffect.play();
				preferences.putInteger("highscore", scoreInt).flush();




				diamondX= -200;


			}
			if (Intersector.overlaps(actor, bombFrame)){
				bombX=-200;
				explosionEffect.play();
				gameStatus=1;
			}

			if (scoreInt==15){
				levelInt++;
				levelChange.play();

				preferences.putInteger("level", levelInt).flush();


//				game.level=levelInt;

				scoreInt=0;
			}

			if (levelInt%3==0){
				playMusic();
			}
			font.draw(game.batch, "lev: " + levelInt,Gdx.graphics.getWidth()-200,Gdx.graphics.getHeight()-10);
			font.draw(game.batch, "score: " + scoreInt,30,Gdx.graphics.getHeight()-10);
			game.batch.end();

			preferences.putFloat("diamondX", diamondX).flush();
			preferences.putFloat("bombX", bombX).flush();
			preferences.putFloat("actorY", actor.y).flush();

		}

		if (gameStatus==1){
			game.batch.begin();
			game.batch.draw(explosion,   Gdx.graphics.getWidth() / 5f, actor.y, 3*actor.width, 2* actor.height);
			game.batch.end();
			if(levelInt<12){
				bgMusic[levelInt/3].stop();}
			else{
				bgMusic[3].stop();
			}

			preferences.putInteger("gameStat", gameStatus).flush();
			preferences.putBoolean("resumable", false).flush();
			game.resumable=preferences.getBoolean("resumable");

			camera.update();
			stage.act(Gdx.graphics.getDeltaTime());
			stage.draw();


//			game.setScreen(new GameOver(game));


		}








	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {
//		preferences.putInteger("highscore", scoreInt);
//		preferences.putInteger("level", levelInt);
//
//		preferences.flush();
	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose () {

		explosionEffect.dispose();
		intro.dispose();
		diamond.dispose();
		bomb.dispose();
		kojo[0].dispose();
		kojo[1].dispose();
		kojo[2].dispose();
		kojo[3].dispose();
		kojo[4].dispose();
		kojo[5].dispose();
		kojo[6].dispose();
		kojo[7].dispose();
		diamondEffect.dispose();
		explosionEffect.dispose();
		jumpEffect.dispose();
		levelChange.dispose();
		bgMusic[0].dispose();
		bgMusic[1].dispose();
		bgMusic[2].dispose();
		bgMusic[3].dispose();



//		game.batch.dispose();

	}
}
