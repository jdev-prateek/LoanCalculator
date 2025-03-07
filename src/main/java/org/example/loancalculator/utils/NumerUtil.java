package org.example.loancalculator.utils;

public class NumerUtil {
    public static String getToWords(double n){
        String out = "";
        double remaining = n;

        if(n >= 1_00_00_000){
            int crs = (int) (remaining / 1_00_00_000);
            remaining = remaining - (crs * 1_00_00_000.0);
            if(crs != 0) {
                out += crs + " cr ";
            }
        }

        if(n >= 1_00_000){
            int lacs = (int) (remaining / 1_00_000);
            remaining = remaining - (lacs * 1_00_000.0);
            if(lacs != 0) {
                out += lacs + " lac ";
            }
        }

        if(n >= 1000){
            int thousands = (int) (remaining / 1000);
            remaining = remaining - (thousands * 1000.0);
            if(thousands != 0) {
                out += thousands + " thousand ";
            }
        }

        if(n >= 100){
            int hundreds = (int) (remaining / 100);
            remaining = remaining - (hundreds * 100.0);
            if(hundreds != 0){
                out += hundreds + " hundred ";
            }
        }

        return out;
    }

    public static void main(String[] args) {
//        System.out.println(NumerUtil.getToWords(42400));
        System.out.println(NumerUtil.getToWords(100000000000.0));
    }
}
