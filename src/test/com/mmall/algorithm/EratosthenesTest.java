package com.mmall.algorithm;

import org.junit.Test;

import static org.junit.Assert.*;

public class EratosthenesTest {

    public EratosthenesTest(){
        System.out.println("Generator prime numbers using"
                + " Sieve of Eratosthenes algorithm");
    }

    @Test
    public void generatePrimeNumbersUpto() {
        int[] primeUptoZero = Eratosthenes.generatePrimeNumbersUpto(0);
        assertEquals(0, primeUptoZero.length);

        int[] primeUptoTwo = Eratosthenes.generatePrimeNumbersUpto(2);
        assertEquals(1, primeUptoTwo.length);
        assertEquals(2, primeUptoTwo[0]);

        int[] primeUptoThree = Eratosthenes.generatePrimeNumbersUpto(3);
        assertEquals(2, primeUptoThree.length);
        assertEquals(2, primeUptoThree[0]);
        assertEquals(3, primeUptoThree[1]);

        int[] primeUptoHundred = Eratosthenes.generatePrimeNumbersUpto(100);
        assertEquals(25, primeUptoHundred.length);
        assertEquals(97, primeUptoHundred[24]);
    }

    @Test
    public void testExhaustive(){
        for(int i = 2; i<700; i++){
            verifyPrimeList(Eratosthenes.generatePrimeNumbersUpto(i));
        }
    }
    private void verifyPrimeList(int[] listOfPrimes) {
        for(int i = 0; i<listOfPrimes.length; i++){
            verifyPrime(listOfPrimes[i]);
        }
    }

    private void verifyPrime(int number) {
        for (int factor = 2; factor < number; factor++){
            assertTrue(number%factor != 0);
        }
    }
}