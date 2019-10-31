package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import java.awt.Image;
import java.awt.Toolkit;

public class MyForm {
    private JPanel panelBig;
    private JButton startButton2;
    private JPanel panelPrimes;
    private JPanel panelPress;
    private JLabel labelPressTitle;
    private JLabel labelPrime;
    private JLabel labelNumbers;
    private JButton stopButton;
    private JTextField textField;
    private JProgressBar progressBar;
    private JLabel imageLabel;
    private JButton imageButton;


    private static final String SAMPLE_URL = "https://gifimage.net/wp-content/uploads/2017/11/gandalf-nodding-gif-6.gif";
    //https://gifimage.net/wp-content/uploads/2017/11/gandalf-nodding-gif-6.gif
    //https://i.ibb.co/TbGFj9G/zkRbxk3.jpg


    boolean szukaj = false;
    Thread watek;

    void szukajLiczbPierwszych() {

        int liczba = 1;

        while (szukaj) {

            if (czyJestPierwsza(liczba)) {
                //jTextArea1.append("" + liczba + "\n");
                labelNumbers.setText("" + liczba);

            }

            if (liczba > 10000000) {
                szukaj = false;
            }

            liczba += 2;
        }


    }

    boolean czyJestPierwsza(int x) {

        for (int i = 2; i < x; ++i) {
            if (x % i == 0) {
                return false;
            }
        }

        return true;
    }


    public MyForm() {


        stopButton.setEnabled(false);

        //============ BUTTON DLA STARTU LICZB PIERWSZYCH ===============

        startButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                startButton2.setEnabled(false);
                stopButton.setEnabled(true);
                szukaj = true;

                //SwingWorker ma dwa paramety <V,K>
                // V  -  parametr który SwingWorker zwraca pod koniec pracy
                // T  -  parametr który określa typ danych pośrednich, których używają metody publish() i process()

                //Dlaczego tworzę nowy obiekt swingWorkera za każdym razem po kliknięciu przycisku, a nie tworzę go wcześniej raz?
                //Ponieważ nie ma możliwości wykorzystania ponownie obiektu tego typu. Po wykonaniu pracy znika.

                SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {

                    //Abstrakcyjna metoda SwingWorkera którą TRZEBA nadpisać, wykonuje ona określone zadanie
                    @Override
                    protected Void doInBackground() throws Exception {

                        int liczba = 1;

                        while (szukaj) {

                            if (czyJestPierwsza(liczba)) {
                                //labelNumbers.setText("" + liczba);  //Od razu podmienia napis na labelu, jednak ja chcę pokazać publish
                                publish(liczba);   //wysyla dane do funkcji process()

                            }

                            if (liczba > 10000000) {
                                szukaj = false;
                            }

                            liczba += 2;
                        }
                        return null;
                    }

                    //Tej metody nie trzeba przesłaniać
                    //Operuje ona na danych wysłanych przez funkcję publish()
                    //W tej metodzie możemy bezpiecznie operować na komponentach graficznych ponieważ działa ona asynchronicznie w EDT.
                    @Override
                    protected void process(List data) {
                        labelNumbers.setText("" + data.get(data.size() - 1));

                        //data.size() - 1 zwraca index ostatniego elementu listy
                        //data.get - pobiera wartość określonego indexu
                        //setText ustawia na labelu dany tekst
                        //innymi słowy pobieramy ostatni index tablicy i ustawiamy go na labelu
                    }

                    //Nazwa mówi sama za siebie. NIE TRZEBA NADPISYWAĆ, tak tylko zrobiłem żeby pokazać
                    @Override
                    protected void done() {
                        System.out.println("Koniec liczenia liczb pierwszych");
                    }
                };

                worker.execute();
            }
        });


        //============== BUTTON DLA STOPU LICZB PIERWSZYCH ===================

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButton2.setEnabled(true);
                stopButton.setEnabled(false);
                szukaj = false;
            }
        });


        //============================================ ŁADOWANIE OBRAZKA ==================================

        textField.setEditable(true);

        textField.setText(SAMPLE_URL);

        imageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingWorker<Icon, Void> worker = new SwingWorker<Icon, Void>() {

                    @Override
                    protected Icon doInBackground() throws Exception {
                        progressBar.setIndeterminate(true);
                        return loadImage(textField.getText());
                    }

                    @Override
                    protected void done() {
                        try {
                            imageLabel.setIcon(get());
                            progressBar.setIndeterminate(false);

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                };
                worker.execute();
            }
        });


    }


    private Icon loadImage(String url) {
        URL imageUrl = null;
        Image image = null;

        try {
            System.out.println(url);
            imageUrl = new URL(url);

            try {
                image = Toolkit.getDefaultToolkit().createImage(imageUrl);
                //image = ImageIO.read(imageUrl);
            } finally {
                return new ImageIcon(image);
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public static void startGui() {





             /*    W Java wątek odpowiadający za obsługę GUI zwie się Event Dispatch Thread (jakby ktoś nie wiedział;)),
        czyli po prostu EDT. Jak sama nazwa mówi zajmuję się on obsługą kolejki zdarzeń i informowaniem o nich
        obiektów nasłuchujących (czyt. z reguły ActionListener-ów), dodatkowo zarządza rozłożeniem komponentów,
         ich wyświetleniem, zmianą właściwości komponentów (np. dezaktywacja przycisku) i obsługą zadań.
         Zadaniami tymi powinny być tylko i wyłącznie krótkotrwałe procesy.   */

        //EventQueue.invokeLater(new Runnable(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("W sumie dziwny program");
                frame.setContentPane(new MyForm().panelBig);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.pack();
                frame.setLocation(500, 500);
                frame.setSize(1200, 400);
            }
        });


    }


}
