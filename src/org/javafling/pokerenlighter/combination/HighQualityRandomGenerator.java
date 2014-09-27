package org.javafling.pokerenlighter.combination;

import java.nio.ByteBuffer;
import java.security.SecureRandom;

/** A much better alternative than the java.util.Random class for generating random numbers.
 * It was taken from the book "Numerical Recipes: The Art of Scientific Computing" by Teukolsky,
 * Vetterling and Flannery.
 * 
 * it combines:
 * - 2 XORShift Generators
 * - 1 LCG (linear congruential generator) and
 * - 1 multiply with carry generator.
 * 
 * The algorithm is fast and provides good quality randomness.
 * 
 * In comparison, the java.util.Random class consists of a single LCG, which is not so good when
 * you need millions and millions of random numbers.
 * 
 * A better solution would be Java's SecureRandom class. Unfortunately, in this case, better randomness
 * would be payed with some speed penalty. On the plus side, the SecureRandom class is used to
 * seed this generator.
 * 
 * @author Radu Murzea
 */
public final class HighQualityRandomGenerator
{
    private long u;
    private long v = 4101842887655102017L;
    private long w = 1;
    
    /**
     * Constructor.
     */
    public HighQualityRandomGenerator()
    {
        //used to seed this generator
        //8 bytes because that's the size of a long in 99.9 % of implementations
        long seed = ByteBuffer.wrap(new SecureRandom().generateSeed(8)).getLong();
        
        u = seed ^ v;
        nextLong();
        v = u;
        nextLong();
        w = v;
        nextLong();
    }

    /**
     * Provides a random long value.
     * @return a random long value
     */
    public long nextLong()
    {
        u = u * 2862933555777941757L + 7046029254386353087L;
        v ^= v >>> 17;
        v ^= v << 31;
        v ^= v >>> 8;
        w = 4294957665L * (w & 0xffffffff) + (w >>> 32);
        long x = u ^ (u << 21);
        x ^= x >>> 35;
        x ^= x << 4;
        long ret = (x + v) ^ w;
      
        return ret;
    }

    /**
     * Provides a random integer that is at most 2^bits large.
     * @param bits how many bits the integer must have
     * @return a random integer with value at most 2^bits.
     */
    public int nextInt(int bits)
    {
        return (int) (nextLong() >>> (64 - bits));
    }
}