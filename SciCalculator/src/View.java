import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;


public class View {


    private JList list;
    private JPanel mainPanel;
    private JButton evaluateButton;
    private JTextField textField;
    private JTextArea textArea;
    private JPanel userPanel;


    private JMenuBar menuBar = new JMenuBar();         // TE  DWA TRZEBA ZROBIĆ W KODZIE BO INTELIJ ICH NIE MA W KREATORZE!!!
    private JMenu options = new JMenu("Options");

    private JMenuItem reset, exit;
    private JFrame frame;
    private Model model;

    public View(Model model) {


        this.model = model;

        //================================CAŁY MENU BAR==========================================
        menuBar.add(options);
        reset = new JMenuItem("Reset");
        exit = new JMenuItem("Exit");
        options.add(reset);
        options.add(exit);
        mainPanel.add(menuBar, BorderLayout.NORTH);
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTextFieldAndArea();
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //==========================================OBSLUGA JLISTY FUNKCJI===============================


        list.setModel(model.addFunctions()); //funkcje matematyczne siedzą w modelu, tutaj dodajemy do JListy
        //pomimo, że dodaliśmy do listy obiekty klasy MyFunction, lista domyślnie wyswietla nazwy uzywajac toString() klasy MyFunction


        list.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String print = ((MyFunction) list.getSelectedValue()).getExpression(); //dałnostwo zwraca Object wiec rzutujemy na MyFunction i dopiero wtedy mozemy uzyc getExpression();
                if (print == "sin" || print == "cos" || print == "tg" || print == "sqrt" || print == "ln") {
                    try {
                        textField.getDocument().insertString(textField.getCaretPosition(), print + "(", null); //insertuje "fun(" w miejscu pozycji pointera
                        textField.requestFocus();  //odpala pointer zeby byl widoczny
                        int saveCaret = textField.getCaretPosition(); //zapisuje pozycje pointera
                        textField.setText(textField.getText() + ")"); //dodaje nawias zamykajacy
                        textField.setCaretPosition(saveCaret);  //ustawia pointer tam gdzie byl, bo po dodaniu nawiasu pointer wywala poza nawias
                    } catch (BadLocationException exception) {
                        System.out.println("BadLocationException");
                    }
                } else if (print == "Last Result") {
                    String s = textArea.getText();  //pobieramy całego tego dużego skurwiela z wynikami do Stringa
                    String[] lines = s.split("\n");  //dzielimy na linie i wrzucamy do tablicy
                    try {
                        s = lines[lines.length - 2];   //jezeli sie da cofnąć o 2 to znaczy, że jest wynik
                    } catch (Exception ex) {
                        System.err.println("No previous results, nothing will happend");
                    }
                    String[] nLines = s.split(" "); //pozbywanie sie tabulatorow i białych znaków z linii
                    s = nLines[nLines.length - 1];  //wyświetlenie dziada

                    try {
                        textField.getDocument().insertString(textField.getCaretPosition(), s, null); //wstawienie tego do paska na dole
                        textField.requestFocus();  //odpala pointer zeby byl widoczny
                    } catch (BadLocationException exception) {
                        System.out.println("BadLocationException");
                    }
                } else {

                    try {
                        textField.getDocument().insertString(textField.getCaretPosition(), print, null); //insertuje fun w miejscu pozycji pointera
                        textField.requestFocus();  //odpala pointer zeby byl widoczny
                    } catch (BadLocationException exception) {
                        System.out.println("BadLocationException");
                    }
                }


            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });


        //======================= EVALUATE BUTTON =======================

        evaluateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String entry = model.createEntry(textField.getText());     //model to u mnie logic, tworzymy Entry do kalkulatora dając mu zawartość tego paska na dole (textfield.gettext())
                //teraz sobie zobacz u mnie Model u ciebie Logic i tę funkcję co ona robi
                if (entry != null) {  //jezeli tamta funkcja robi Entry nullowe to znaczy, że coś sie popsuło
                    textArea.setText(textArea.getText() + entry); //wstawiamy to entry na górę
                    textField.setText(""); // czyscimy pasek na dole
                }
            }
        });


        //==================OBSLUGA STRZALKI W GORE==================
        //mamy zrobić tak żeby po wciśnięciu strzałki w góre będąc w pasku na dole napisało się samo ostatnie działanie

        textField.addKeyListener(new KeyListener() {   //do textFielda dodajemy keyListener i nadpisują nam sie funkcje
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {   //KeyEvent e zapisał w sobie jaki to przycisk, przyciski mają w sobie keyCode'y, dla strzałki w górę jest to VK_Up
                    String str = textArea.getText(); //pobieramy całą zawartość tego na górze
                    if (str != null || str != "") {  //jezeli nie jest puste

                        String[] tab = str.split("\n");  //split(\n) dzieli to na linie, bo \n to znak nowej linii i kazda linia jest zapisana w tab
                        try {
                            str = tab[tab.length - 3]; //jak sie cofniemy 3 linie do tyłu to wyciągniemy dokładnie to wpisane działanie, wynika to z budowy
                            //naszego entry które wygląda tak:
                            // sin(e)
                            // 	  = 0,411
                            // ------------
                            //jak widać gdybyśmy zroili length - 3 to otrzymamy "sin(e)"

                            str = str.substring(1);  //usuwamy pierwszy znak stringa, który jest zawsze spacja bo tak textArea zapisała
                            // bo byśmy mieli " sin(e)" a nie "sin(e)"

                            textField.setText(str);
                            //Ustawiamy tego stringa jako zawartość tego paska na dole
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            System.err.println("Cannot use up arrow function, because textArea is empty. Nothing to worry, nothing will happend");
                        }
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) { //teraz dla ENTERA robimy identyczne rzeczy jak przy przycisku evaluate
                    String entry = model.createEntry(textField.getText());
                    if (entry != null) {
                        textArea.setText(textArea.getText() + entry);
                        textField.setText("");
                    }

                    //11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111
                    // !!!  !!!   !!!   !!   !!!  !!! !!! !!!    !!!  !!!   !!!   !!   !!!  !!! !!! !!!   !!!  !!!   !!!   !!   !!!  !!! !!! !!!


                    //PRZEJDŹ JESZCZE NA GÓRĘ TAM GDZIE ROBILIŚMY LAST RESULT U CIEBIE I ZOSTAWILIŚMY JE DO ZROBIENIA !!!!!!!!!!!!!!!!!!


                    // !!!  !!!   !!!   !!   !!!  !!! !!! !!!    !!!  !!!   !!!   !!   !!!  !!! !!! !!!   !!!  !!!   !!!   !!   !!!  !!! !!! !!!
                    //111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });


        System.out.println(textArea.getText());

    }


    public void startView() {
        frame = new JFrame("SciCalc");
        frame.setContentPane(new View(model).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        frame.setLocation(500, 500);
        frame.setSize(1200, 400);


    }

    public void addFunctionsToList(ListModel<MyFunction> listModel) {
        list.setModel(listModel);
    }


    void clearTextFieldAndArea() {
        System.out.println("Wyczyszczono gui");
        textField.setText("");
        textArea.setText("");
    }


}
