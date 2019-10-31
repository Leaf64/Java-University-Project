import org.mariuszgromada.math.mxparser.*;

public class MyFunction {


    private String name;
    private String expression;


    MyFunction(String name, String expression) {
        this.name = name;
        this.expression = expression;
    }


    public String getExpression() {
        return expression;
    }


    @Override
    public String toString() {  //potrzebny nam toString do wyswietlenia na liscie
        return name;
    }


}