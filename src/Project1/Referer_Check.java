package Project1;

public class Referer_Check {
    String Referer;
    String[] Real_Referer;
    public Referer_Check(String Referer, String...Real_Referer){
        this.Referer = Referer;
        this.Real_Referer = Real_Referer;
    }
    public boolean check()
    {
        boolean result = false;
        for (int i = 0; i < Real_Referer.length; i++) {
            result = Referer.contains(Real_Referer[i]) || result;
        }
        return !result;
    }
}
