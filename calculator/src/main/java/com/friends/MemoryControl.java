package com.friends;

public abstract class MemoryControl {
    protected static double memory = 0; // Gör memory delat för alla instanser

    // Gemensam logik
    public double getMemory() {
        return memory;
    }

    public abstract void handleMemoryOperation(double value);

    // Barnklasser definieras i samma fil
    public static class MemoryAdd extends MemoryControl {
        @Override
        public void handleMemoryOperation(double value) {
            memory += value;
        }
    }

    public static class MemorySubtract extends MemoryControl {
        @Override
        public void handleMemoryOperation(double value) {
            memory -= value;
        }
    }

    public static class MemoryClear extends MemoryControl {
        @Override
        public void handleMemoryOperation(double value) {
            memory = 0;
        }
    }

    public static class MemoryRecall extends MemoryControl {
        @Override
        public void handleMemoryOperation(double value) {
            // Ingen logik behövs här, memory är redan tillgängligt via getMemory()
        }
    }
}
