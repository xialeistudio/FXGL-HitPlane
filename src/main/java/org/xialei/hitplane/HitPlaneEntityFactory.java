package org.xialei.hitplane;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.geometry.Point2D;

public class HitPlaneEntityFactory implements EntityFactory {

    @Spawns("BG")
    public Entity spawnBG(SpawnData data) {

        return FXGL.entityBuilder(data)
                .view("background.png")
                .build();
    }

    @Spawns("Player")
    public Entity spawnPlayer(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(Type.PLAYER)
                .viewWithBBox("hero.png")
                .with(new PlayerComponent(0.1))
                .collidable()
                .build();
    }

    @Spawns("Enemy")
    public Entity spawnEnemy(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(Type.ENEMY)
                .viewWithBBox("enemy.png")
                .with(new ProjectileComponent(new Point2D(0, 1), 150).allowRotation(false))
                .with(new OffscreenCleanComponent())
                .with(new EnemyComponent(1))
                .collidable()
                .build();
    }

    @Spawns("EnemyBullet")
    public Entity spawnEnemyBullet(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(Type.ENEMY_BULLET)
                .viewWithBBox("bullet2.png")
                .with(new OffscreenCleanComponent())
                .with(new ProjectileComponent(new Point2D(0, 1), 300).allowRotation(false))
                .collidable()
                .build();
    }

    @Spawns("PlayerBullet")
    public Entity spawnPlayerBullet(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(Type.PLAYER_BULLET)
                .viewWithBBox("bullet1.png")
                .with(new OffscreenCleanComponent())
                .with(new ProjectileComponent(new Point2D(0, -1), 600).allowRotation(false))
                .collidable()
                .build();
    }
}
