package org.xialei.hitplane;

import com.almasb.fxgl.achievement.Achievement;
import com.almasb.fxgl.achievement.AchievementEvent;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.ui.FontType;
import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class HitPlaneApp extends GameApplication {

    public static void main(String[] args) {
        launch(args);
    }

    private StringProperty achievementTextProperty;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(800);
        settings.setTitle("HitPlane");
        settings.setVersion("1.0");
        settings.setFontText("lcd.ttf");
        settings.setFontUI("lcd.ttf");
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);

        settings.getAchievements().add(new Achievement("Kill Enemy 1", "Kill 3 Enemies", "enemiesKilled", 3));
        settings.getAchievements().add(new Achievement("Kill Enemy 2", "Kill 10 Enemies", "enemiesKilled", 10));
        settings.getAchievements().add(new Achievement("Kill Enemy 3", "Kill 20 Enemies", "enemiesKilled", 20));
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
        vars.put("enemiesKilled", 0);
        achievementTextProperty = new SimpleStringProperty("Achievement: 0/" + FXGL.getSettings().getAchievements().size());
    }

    @Override
    protected void initPhysics() {
        FXGL.onCollisionBegin(Type.PLAYER, Type.ENEMY_BULLET, (player, enemyBullet) -> {
            enemyBullet.removeFromWorld();
            spawnFloatingText("-100", player.getPosition(), Color.RED);
            FXGL.inc("score", -100);
        });
        FXGL.onCollisionBegin(Type.ENEMY, Type.PLAYER_BULLET, (enemy, playerBullet) -> {
            spawnFloatingText("+10", enemy.getPosition(), Color.GREEN);
            enemy.removeFromWorld();
            playerBullet.removeFromWorld();
            FXGL.inc("score", 10);
            FXGL.inc("enemiesKilled", 1);
            FXGL.play("enemy_down.mp3");
        });
    }

    /**
     * todo 文字浮动效果
     *
     * @param content
     * @param position
     */
    private void spawnFloatingText(String content, Point2D position, Color color) {
        Text node = FXGL.addText(content, position.getX(), position.getY());
        node.setFill(color);
        FXGL.animationBuilder()
                .onFinished(() -> FXGL.removeUINode(node))
                .duration(Duration.seconds(0.5))
                .translate(node)
                .from(position)
                .to(new Point2D(position.getX(), position.getY() - 40))
                .buildAndPlay();
    }

    @Override
    protected void initGame() {
        listenAchievementsEvents();
        FXGL.getGameWorld().addEntityFactory(new HitPlaneEntityFactory());
        FXGL.spawn("BG");
        spawnPlayer();
        FXGL.getGameTimer().runAtInterval(this::spawnEnemy, Duration.seconds(0.5));
        FXGL.loopBGM("bgm.mp3");
    }

    private void listenAchievementsEvents() {
        FXGL.getEventBus().addEventHandler(AchievementEvent.ACHIEVED, e -> {
            Achievement achievement = e.getAchievement();
            var achievedCount = FXGL.getSettings().getAchievements().stream().filter(Achievement::isAchieved).count();
            achievementTextProperty.setValue("Achievement: " + achievedCount + "/" + FXGL.getSettings().getAchievements().size());
            FXGL.getNotificationService().pushNotification("<" + achievement.getName() + "> achieved!");
            FXGL.inc("score", 1000);
        });
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
        Text scoreText = FXGL.getUIFactoryService().newText("", Color.BLACK, FontType.UI, 20);
        scoreText.textProperty().bind(FXGL.getip("score").asString("Score: [%d]"));

        Text enemiesKilledText = FXGL.getUIFactoryService().newText("", Color.BLACK, FontType.UI, 20);
        enemiesKilledText.textProperty().bind(FXGL.getip("enemiesKilled").asString("Enemies Killed: [%d]"));

        Text achievementText = FXGL.getUIFactoryService().newText("", Color.BLACK, FontType.UI, 20);
        achievementText.textProperty().bind(achievementTextProperty);

        FXGL.addUINode(scoreText, 20, 40);
        FXGL.addUINode(enemiesKilledText, 20, 60);
        FXGL.addUINode(achievementText, 20, 80);
    }
}
