import java.io.*;

abstract class manusia {
    private double tinggiBadan;

    public manusia(double TB){
        tinggiBadan = TB;
    }

    public double getTB(){
        return tinggiBadan;
    }
    public abstract double htgBBI();
}

class cowo extends manusia {
    public cowo(double TB){
        super(TB);
    }
}
