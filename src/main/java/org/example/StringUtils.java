package org.example;

public class StringUtils {
    public static boolean isPalindrome(String text){
        if (text == null || text.isEmpty()){
            return true;
        }

        text = text.toLowerCase();

        int left = 0;
        int right = text.length() - 1;

        while (left < right){
            if (text.charAt(left) != text.charAt(right)){
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

}
