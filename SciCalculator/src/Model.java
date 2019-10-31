import org.mariuszgromada.math.mxparser.Expression;

import javax.swing.*;
import java.text.MessageFormat;
import java.util.List;

public class Model {

    public ListModel<MyFunction> addFunctions() {

        MyFunction sinus = new MyFunction("Sinus", "sin");
        MyFunction cosinus = new MyFunction("Cosinus", "cos");
        MyFunction tangens = new MyFunction("Tangens", "tg");
        MyFunction sqrt = new MyFunction("Square root", "sqrt");
        MyFunction ln = new MyFunction("Natural logarithm", "ln");


        MyFunction pi = new MyFunction("Pi", "pi");
        MyFunction e = new MyFunction("Euler's number", "e");
        MyFunction phi = new MyFunction("Golden ratio", "[phi]");

        MyFunction add = new MyFunction("+", "+");
        MyFunction sub = new MyFunction("-", "-");
        MyFunction mul = new MyFunction("*", "*");
        MyFunction div = new MyFunction("/", "/");


        MyFunction last = new MyFunction("Last Result", "Last Result");

        DefaultListModel<MyFunction> listModel = new DefaultListModel<>();

        listModel.addElement(last);

        listModel.addElement(add);
        listModel.addElement(sub);
        listModel.addElement(mul);
        listModel.addElement(div);

        listModel.addElement(sinus);
        listModel.addElement(cosinus);
        listModel.addElement(tangens);
        listModel.addElement(sqrt);
        listModel.addElement(ln);

        listModel.addElement(pi);
        listModel.addElement(e);
        listModel.addElement(phi);

        return listModel;
    }


    public String createEntry(String equation) {
        if (equation.isEmpty()) return null;
        equation = equation.replace(',', '.');
        Expression expression = new Expression(equation);
        if (expression.checkLexSyntax()) {

            double result = expression.calculate();
            if (Double.isNaN(result)) {

                JOptionPane.showMessageDialog(null, "Inapropriate equation. Cannot calculate this.", "Warning", JOptionPane.ERROR_MESSAGE);

                return null;
            } else {
                String entry = MessageFormat.format(" {0}\n \t  = {1} \n ------------\n", equation, result);
                return entry;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Inapropriate equation. Insert number or function", "Warning", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }


}
