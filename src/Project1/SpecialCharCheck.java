package Project1;

public class SpecialCharCheck {
    public static boolean check(String Str) {
        //只要包含任意一种，就返回true，否则返回false
        return Str.contains("/") || Str.contains("<") || Str.contains(">") || Str.contains(" ") || Str.contains("|") || Str.contains("'") || Str.contains("\\") || Str.contains(",");
    }
}
