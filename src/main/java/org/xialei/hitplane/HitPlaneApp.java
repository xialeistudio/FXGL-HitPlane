package org.xialei.hitplane;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.ui.FontType;
import java.util.Map;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class HitPlaneApp extends GameApplication {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(480);
        settings.setHeight(800);
        settings.setTitle("HitPlane");
        settings.setVersion("1.0");
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
    }

    @Override
    protected void initPhysics() {
        FXGL.onCollisionBegin(Type.PLAYER, Type.ENEMY_BULLET, (player, enemyBullet) -> {
            enemyBullet.removeFromWorld();
            spawnFloatingText("-100", player.getPosition());
            FXGL.inc("score", -100);
        });
        FXGL.onCollisionBegin(Type.ENEMY, Type.PLAYER_BULLET, (enemy, playerBullet) -> {
            spawnFloatingText("+10", enemy.getPosition());
            enemy.removeFromWorld();
            playerBullet.removeFromWorld();
            FXGL.inc("score", 10);
            FXGL.play("enemy_down.mp3");
        });
    }

    /**
     * todo 文字浮动效果
     *
     * @param content
     * @param position
     */
    private void spawnFloatingText(String content, Point2D position) {
//        Text node = FXGL.getUIFactoryService().newText(content, Color.BLACK, 16);
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new HitPlaneEntityFactory());
        FXGL.spawn("BG");
        spawnPlayer();

        FXGL.getGameTimer().runAtInterval(this::spawnEnemy, Duration.seconds(0.5));

        FXGL.loopBGM("bgm.mp3");
    }

    /**
     * 生成敌人
     */
    private void spawnEnemy() {
        FXGL.spawn("Enemy", FXGLMath.random(0, FXGL.getAppWidth() - 57), 0);
    }

    /**
     * 生成玩家
     */
    private void spawnPlayer() {
        Entity player = FXGL.spawn("Player");
        player.xProperty().bind(FXGL.getInput().mouseXWorldProperty());
        player.yProperty().bind(FXGL.getInput().mouseYWorldProperty());
    }

    @Override
    protected void initUI() {
        Text scoreText = FXGL.getUIFactoryService().newText("", Color.BLACK, FontType.UI, 24);
        scoreText.textProperty().bind(FXGL.getip("score").asString("Score: [%d]"));

        FXGL.addUINode(scoreText, 20, 40);
    }
}
