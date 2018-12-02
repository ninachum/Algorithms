/******************************************************************************
 *  Compilation:  javac LSD.java
 *  Execution:    java LSD < input.txt
 *  Dependencies: StdIn.java StdOut.java 
 *  Data files:   https://algs4.cs.princeton.edu/51radix/words3.txt
 *
 *  LSD radix sort
 *
 *    - Sort a String[] array of n extended ASCII strings (R = 256), each of length w.
 *
 *    - Sort an int[] array of n 32-bit integers, treating each integer as 
 *      a sequence of w = 4 bytes (R = 256).
 *
 *  Uses extra space proportional to n + R.
 *
 *
 *  % java LSD < words3.txt
 *  all
 *  bad
 *  bed
 *  bug
 *  dad
 *  ...
 *  yes
 *  yet
 *  zoo
 *
 ******************************************************************************/


import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
/**
 *  The {@code LSD} class provides static methods for sorting an
 *  array of <em>w</em>-character strings or 32-bit integers using LSD radix sort.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/51radix">Section 5.1</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class MyLSD {
    private static final int BITS_PER_BYTE = 8;

    // do not instantiate
    private MyLSD() { }

   /**  
     * Rearranges the array of W-character strings in ascending order.
     *
     * @param a the array to be sorted
     * @param w the number of characters per string
     */
    public static void sort(char[] a) {
        int n = a.length;
        int R = 256;   // extend ASCII alphabet size
        char[] aux = new char[n];


            // compute frequency counts
            int[] count = new int[R+1];
            for (int i = 0; i < n; i++)
                count[a[i] + 1]++;

            // compute cumulates
            for (int r = 0; r < R; r++)
                count[r+1] += count[r];

            // move data
            for (int i = 0; i < n; i++)
                aux[count[a[i]]++] = a[i];

            // copy back
            for (int i = 0; i < n; i++)
                a[i] = aux[i];
    
    }

}

/******************************************************************************
 *  Copyright 2002-2018, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
