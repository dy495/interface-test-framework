package com.haisheng.framework.testng.bigScreen.crm.wm;

public class Test {

    public static void main(String[] args) {
        int money = getMoney(45, 10, 4, 20, 500, 300);
        System.out.println(money);
    }

    public static int getMoney(int realityKm, int realitySecond, int baseMoney, int baseKm, int kmMax, int baseSecond) {
        return realityKm <= baseKm ? baseMoney + getAddMoney(realitySecond, baseSecond) : realityKm <= kmMax ?
                baseMoney + (kmMax - baseKm) * 2 + getAddMoney(realitySecond, baseSecond) :
                baseMoney + (kmMax - baseKm) * 2 + (realityKm - kmMax) * 3 + getAddMoney(realitySecond, baseSecond);
    }

    public static int getAddMoney(int second, int s) {
        return (int) Math.floor((double) second / s);
    }
}
