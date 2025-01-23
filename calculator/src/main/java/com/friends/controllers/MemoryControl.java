package com.friends.controllers;
/**
 * Abstrakt basklass för att hantera minnesoperationer (M) på kalkylatorn.
 * Denna klass tillhandahåller ett delat statiskt minne för alla instanser och deklarerar metoder för att hantera minnesrelaterade operationer.
 */
public abstract class MemoryControl {
    /**
     * Delat minnesvärde för alla instanser av MemoryControl.
     */
    protected static double memory = 0;
    /**
     * Hämtar det aktuella värdet av det delade minnet.
     *
     * @return det aktuella minnesvärdet.
     */
    public double getMemory() {
        return memory;
    }
    /**
     * Abstrakt metod för att hantera minnesoperationer.
     * Underklasser måste implementera denna metod för att definiera specifika operationer på minnet.
     *
     * @param value värdet som ska användas i operationen på minnet.
     */
    public abstract void handleMemoryOperation(double value);
    /**
     * Underklass för att lägga till ett värde till minnet.
     */
    public static class MemoryAdd extends MemoryControl {
        /**
         * Lägger till det angivna värdet till minnet.
         *
         * @param value värdet som ska läggas till minnet.
         */
        @Override
        public void handleMemoryOperation(double value) {
            memory += value;
        }
    }
    /**
     * Underklass för att subtrahera ett värde från minnet.
     */
    public static class MemorySubtract extends MemoryControl {
        /**
         * Subtraherar det angivna värdet från minnet.
         *
         * @param value värdet som ska subtraheras från minnet.
         */
        @Override
        public void handleMemoryOperation(double value) {
            memory -= value;
        }
    }
    /**
     * Underklass för att rensa minnet.
     */
    public static class MemoryClear extends MemoryControl {
        /**
         * Rensar minnet genom att sätta det till 0.
         *
         * @param value detta parameter ignoreras eftersom inget värde behövs för att rensa minnet.
         */
        @Override
        public void handleMemoryOperation(double value) {
            memory = 0;
        }
    }
            /**
            * Underklass för att hämta (recalla) minnet.
          */
    public static class MemoryRecall extends MemoryControl {
        /**
         * Ingen operation utförs här eftersom minnesvärdet redan
         * är tillgängligt via {@link #getMemory()}.
         *
         * @param value detta parameter ignoreras eftersom ingen operation behövs för att hämta minnet.
         */
        @Override
        public void handleMemoryOperation(double value) {
        }
    }
}
