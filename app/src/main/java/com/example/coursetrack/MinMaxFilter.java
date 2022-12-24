package com.example.coursetrack;

import android.text.InputFilter;
import android.text.Spanned;

public class MinMaxFilter implements InputFilter {
    private int min, max;
    public MinMaxFilter(int min, int max){
        this.min = min;
        this.max = max;
    }
    public MinMaxFilter(String min, String max){
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
    }

    @Override
    public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
        try{
            int input = Integer.parseInt(spanned.toString() + charSequence.toString());
            if(isRange(min, max, input)){
                return null;
            }
        }
        catch (NumberFormatException e){
            e.printStackTrace();
        }
        return "";
    }
    private boolean isRange(int a, int b, int c){
        return b>a? c>=a && c<=b:c<=a && c>=b;
    }
}
