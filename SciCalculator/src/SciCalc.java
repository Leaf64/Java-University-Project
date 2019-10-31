import javax.swing.*;

public class SciCalc {


    public static void main(String[] args) {


        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }


        Model model = new Model();
        View view = new View(model);
        view.startView();
    }
}
