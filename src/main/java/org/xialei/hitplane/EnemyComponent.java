package org.xialei.hitplane;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

public class EnemyComponent extends Component {

    private final double fireSpeed;
    private final LocalTimer timer;

    public EnemyComponent(double fireSpeed) {
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

    private void fire() {
        FXGL.spawn("EnemyBullet", entity.getX() + entity.getWidth() / 2, entity.getY() + entity.getHeight());
    }

}
