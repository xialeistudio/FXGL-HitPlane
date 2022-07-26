package org.xialei.hitplane;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.TimerAction;
import javafx.util.Duration;

public class PlayerComponent extends Component {

    private final double fireSpeed;
    private TimerAction fireTimerAction;

    public PlayerComponent(double fireSpeed) {
        this.fireSpeed = fireSpeed;
    }

    @Override
    public void onAdded() {
        fireTimerAction = FXGL.getGameTimer().runAtInterval(this::fire, Duration.seconds(fireSpeed));
    }

    @Override
    public void onRemoved() {
        fireTimerAction.expire();
    }

    /**
     * 玩家开火
     */
    private void fire() {
        FXGL.play("bullet.mp3");
        FXGL.spawn("PlayerBullet", entity.getX() + entity.getWidth() / 2, entity.getY() - 10);
    }
}
