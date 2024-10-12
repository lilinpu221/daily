package com.linzi.daily.template.gp.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author Lil
 */
@Getter
public enum Speed {
    /**
     * 打印速度
     */
    FASTEST(5),FAST(4),NORMAL(3),SLOW(2);

    private final Integer speed;

    Speed(Integer speed){
        this.speed=speed;
    }

    public static Speed explain(Integer speed){
        return Arrays.stream(Speed.values()).filter(s->s.getSpeed().equals(speed)).findFirst().orElse(NORMAL);
    }
}
