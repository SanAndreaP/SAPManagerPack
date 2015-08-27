/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.util;

public class MutableString
        implements java.io.Serializable, Comparable<String>, CharSequence
{
    private static final long serialVersionUID = 518737846171917970L;

    private String value;

    public MutableString(String str) {
        this.value = str;
    }

    public void set(String str) {
        this.value = str;
    }

    @Override
    public int length() {
        return this.value.length();
    }

    @Override
    public char charAt(int index) {
        return this.value.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return this.value.subSequence(start, end);
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public int compareTo(String o) {
        return this.value.compareTo(o);
    }

    @Override
    public boolean equals(Object obj) {
        return this.value.equals(obj);
    }

    public String stringValue() {
        return this.value;
    }
}
