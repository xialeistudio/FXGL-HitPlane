package org.xialei.hitplane;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.TimerAction;
import javafx.util.Duration;

public class EnemyComponent extends Component {

    private final double fireSpeed;

    private TimerAction fireTimerAction;

    public EnemyComponent(double fireSpeed) {
        this.fireSpeed = fireSpeed;
    }

    @Override
    public void onAdded() {
        fireTimerAction = FXGL.getGameTimer().runAtInterval(this::fire, Duration.seconds(fireSpeed));
    }

    private void fire() {
        FXGL.spawn("EnemyBullet", entity.getX() + entity.getWidth() / 2, entity.getY() + entity.getHeight());
    }

    @Override
    public void onRemoved() {
        fireTimerAction.expire();
    }
}
