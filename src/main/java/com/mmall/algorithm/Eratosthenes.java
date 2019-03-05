package com.mmall.algorithm;

/**
 * 素数生成算法- Eratosthenes的筛选实例
 */
public class Eratosthenes {

    private enum Marker{
        CROSSED,UNCROSSED;
    }

    private static Marker[] crossedOut;
    private static int[] primes;

    public static int[] generatePrimeNumbersUpto(int limit){
        if(limit < 2){
            return new int[0];
        }
        uncrossIntegerUpto(limit);
        crossOutMultiples();
        putUncrossedIntegersIntoPrimes();
        return primes;
    }


    /**
     * 生成2-N的自然数，并设置成初始标记
     * @param limit 最大自然数
     */
    private static void uncrossIntegerUpto(int limit) {
        crossedOut = new Marker[limit+1];
        for (int i = 2; i < crossedOut.length; i++){
            crossedOut[i] = Marker.UNCROSSED;
        }
    }

    private static void crossOutMultiples() {
        //只用计算小于等于最大值平方根的范围的素数
        int iterationLimit = determineIterationLimit();
        for(int i = 2; i <= iterationLimit; i++){
            //当前未被证明是某个自然数倍数的自然数进行新一轮的判断，免去重复判断
            if(notCrossed(i)){
                crossOutMultipleOf(i);
            }
        }
    }

    private static int determineIterationLimit() {
        //数组中的每个倍数都有一个素数因子
        //小于或等于平方根
        //数组大小，我们不需要越过
        //大于根的倍数。
        double iterationLimit = Math.sqrt(crossedOut.length);
        return (int) iterationLimit;
    }

    private static boolean notCrossed(int i) {
        return crossedOut[i] == Marker.UNCROSSED;
    }

    /**
     * 算法核心
     * @param i 数组下标，也表示当前判断的自然数
     */
    private static void crossOutMultipleOf(int i) {
        //i从2开始的自然数，0和1不认为是素数
        //i的倍数都不是素数，设置为CROSSED
        for(int multiple = 2*i; multiple < crossedOut.length; multiple += i){
            crossedOut[multiple] = Marker.CROSSED;
        }
    }

    /**
     * 把素数放入素数数组
     */
    private static void putUncrossedIntegersIntoPrimes(){
        primes = new int[numberOfUncrossedIntegers()];
        for(int j = 0, i = 2; i<crossedOut.length; i++){
            if(notCrossed(i)){
                primes[j++] = i;
            }
        }
    }

    /**
     * 计算出存在素数的个数
     * @return int
     */
    private static int numberOfUncrossedIntegers() {
        int count = 0;
        for(int i = 2; i<crossedOut.length; i++){
            if(notCrossed(i)){
                count++;
            }
        }
        return count;
    }


}
