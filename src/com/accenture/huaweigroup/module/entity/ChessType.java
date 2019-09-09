package com.accenture.huaweigroup.module.entity;

public enum ChessType {
    LAND(0),
    SEA(1),
    AIR(2);

    int value;
    private ChessType(int value){
        this.value=value;
    }
    public int getValue() {
        return this.value;
    }
    /*方法Value2CityTest是为了typeHandler后加的*/
    public static ChessType Value2CityTest(int value) {
        for (ChessType citytest : ChessType.values()) {
            if (citytest.value == value) {
                return citytest;
            }
        }
        throw new IllegalArgumentException("无效的value值: " + value + "!");
    }

}
