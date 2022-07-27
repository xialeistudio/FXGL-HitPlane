package org.xialei.hitplane;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

public class PlayerComponent extends Component {

    private final double fireSpeed;
    private final LocalTimer timer;

    public PlayerComponent(double fireSpeed) {
        this.fireSpeed = fireSpeed;
        this.timer = FXGL.newLocalTimer();
    }

    @Override
    public void onUpdate(double tpf) {
        if (timer.elapsed(Duration.seconds(fireSpeed))) {
            fire();
            timer.capture();
        }
    }

    /**
     * 玩家开火
     */
    private void fire() {
        FXGL.play("bullet.mp3");
        FXGL.spawn("PlayerBullet", entity.getX() + entity.getWidth() / 2, entity.getY() - 10);
    }
}
