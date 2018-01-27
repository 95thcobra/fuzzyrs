package com.rs.utils.net;

public class IsaacKeyPair {

    private final ISAACCipher inKey, outKey;

    public IsaacKeyPair(final int[] seed) {
        inKey = new ISAACCipher(seed);
        for (int i = 0; i < seed.length; i++) {
            seed[i] += 50;
        }
        outKey = new ISAACCipher(seed);
    }

    public ISAACCipher inKey() {
        return inKey;
    }

    public ISAACCipher outKey() {
        return outKey;
    }

}
