package Project1;

public class User_Agent_Check {
    String uA;
    public User_Agent_Check(String uA){
        this.uA = uA;
    }
    public boolean check(){
        String[] fake_uA ={"python","Python","java","Java","curl"};
        boolean a = false;
        for (String fuA:fake_uA){
            if (uA.contains(fuA)) {
                a = true;
                break;
            }
        }
        return a;
    }
}
