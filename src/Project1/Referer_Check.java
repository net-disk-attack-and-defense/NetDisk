package Project1;

public class Referer_Check {
    String Referer;
    String Real_Referer;
    public Referer_Check(String Referer, String Real_Referer){
        this.Referer = Referer;
        this.Real_Referer = Real_Referer;
    }
    public boolean check(){
        return !Referer.contains(Real_Referer);
    }
}
