package Project1;

public class GetSuffix {
    public static String suffix(String fileName){
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
