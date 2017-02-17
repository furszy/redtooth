package iop_sdk.utils;

/**
 * Created by mati on 01/12/16.
 */

public class StringUtils {

    public static String cleanString(String s){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<s.length();i++){
            char c = s.charAt(i);
            if (c != '\"' && c!='[' && c!=']' && c!='\\')
                stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public static String numberToNumberWithDots(long number){
        String numberStr = String.valueOf(number);
        int size = numberStr.length();
        StringBuilder ret = new StringBuilder();
        int num = 0;
        for (int i=size-1;i>-1;i--){
            num++;
            char digit = numberStr.charAt(i);
            ret.append(digit);
            if (num % 3 == 0 && i!=0){
                ret.append(".");
            }
        }
        return ret.reverse().toString();
    }


    public static String numberToK(long number){
        String numberStr = numberToNumberWithDots(number);
        numberStr = numberStr.replace(".000","k");
        return numberStr;
    }

}
