
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AllThoseTerritorys{

    private World world;
    private Player human;
    private ArtificialIntelligenceStupid ai;
    private Round round;

    public static void main(String[] args) throws Exception{
        AllThoseTerritorys p = new AllThoseTerritorys();
        p.chooseMap();
    }


    // die map.datei wird durch world.readmap eingelesen. anschließend wird startGame() aufgerufen.
    private void chooseMap() throws IOException{

        Scanner sc = new Scanner(System.in);
        System.out.println("Bitte Pfad der Mapdatei eingeben: ");
        while(sc.hasNext()){
            String input = sc.nextLine();
            if(input.equals("Exit")) {
                sc.close();
                break;
            } else{
                if(input.endsWith(".map")){
                    try{
                        world = new World();
                        world.readMap(input);
                        sc.close();
                        startGame();
                        break;
                    } catch(IOException e){
                        System.out.println("Datei nicht gefunden!\nBitte erneut versuchen oder exit eingeben um das Programm zu beenden!");
                    }
                } else{
                    System.out.println("Bitte Pfad der Mapdatei eingeben: ");
                }
            }
        }
    }

    private JFrame frame;
    private JButton turn;
    private Jdraw j = new Jdraw();

    //erzeugt das GUI und startet das spiel mit einem mausklick
    private void startGame(){

        human = new Player("Spieler",world);
        ai = new ArtificialIntelligenceStupid("CPU",world);
        round = new Round(human,ai,world);
        frame = new JFrame();
        frame.setSize(1250,650);

        frame.setLayout(new BorderLayout());

        frame.addMouseListener(new MouseMove());
        frame.add(j,BorderLayout.CENTER);


        turn = new JButton("Zug Beenden");
        turn.setSize(50,30);
        turn.addActionListener(new ZugBeenden());
        turn.setEnabled(false);

        frame.add(turn, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        JOptionPane.showMessageDialog(null, "Spiel beginnt.");

    }

    private class MouseMove implements MouseListener{
        public void mouseEntered(MouseEvent e){

        }

        public void mouseClicked(MouseEvent e){

        }

        public void mouseReleased(MouseEvent e){
            j.repaint();
        }

        public void mouseExited(MouseEvent e){

        }

        public void mousePressed(MouseEvent e){
            int x = e.getX();
            int y = e.getY();

            switch(round.getPhase()){
                case "P" : round.chooseTerritoriesFirsRound(x, y,world.getTerritories().iterator(),world.getTerritories().iterator());
                           //round.checkContinentsOccupied();
                           j.repaint();break;
                case "S" : round.checkContinentsOccupied();
                           round.supply(x, y);
                           j.repaint();break;
                case "C" : turn.setEnabled(true);
                           round.conquer(x,y,e,world.getTerritories().iterator());
                           j.repaint();
            }
        }

    }
    // durch klicken auf zugbeenden wird der zug beendet.
    // sollte der spieler gewonnen/ verloren habenm wird das spiel beendet.
    // andernfalls erhält der spieler neue troops und es wird eine neue runde mit der verstärkungsphase begonnen.
    private class ZugBeenden implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(human.getTerritories().size() == world.getTerritories().size()){
                JOptionPane.showMessageDialog(frame,"Sie haben gewonnen!");             //Auf sieg �berpr�fen
                System.exit(1);
            }

            round.aiTurn();
            j.repaint();

            if(ai.getTerritories().size() == world.getTerritories().size()){
                JOptionPane.showMessageDialog(frame,"Sie haben verloren!");             //Auf Niederlage �berpr�fen
                System.exit(1);
            }

            round = new Round(human,ai,world);
            human.addTroops(human.receiveTroops());
            round.setPhase("S");
            JOptionPane.showMessageDialog(frame,"Verstaerkungsphase beginnt!");
            turn.setEnabled(false);
        }
    }

    class Jdraw extends JPanel {

        public Jdraw(){
            setBorder(BorderFactory.createLineBorder(Color.black));

        }

        public Dimension getPreferredSize(){
            return new Dimension(super.getSize());
        }

        public void paintComponent(Graphics g){
            super.paintComponent(g);
            this.setBackground(new Color(38,155,255));
            Graphics2D g2d = (Graphics2D) g.create();
            Iterator<Territory> m = world.getTerritories().iterator();
            Territory cur;

            while(m.hasNext()) {
                cur = m.next();

                Iterator<Territory> c = cur.getNeighbors().listIterator();
                while(c.hasNext()){
                    Territory curNeighbor = c.next();
                    g2d.setColor(Color.black);
                    g2d.drawLine((int) curNeighbor.getCapital().getX(),(int) curNeighbor.getCapital().getY(),(int) cur.getCapital().getX(), (int) cur.getCapital().getY());
                }
             }

            m = world.getTerritories().iterator();
            while(m.hasNext()){
                cur = m.next();

                if(cur.getOccupiedBy() != null){
                    if(cur.getOccupiedBy().equals(human)){
                        if(round.getChoice() != null && round.getChoice().equals(cur)){
                            g2d.setColor(new Color(35,90,232).brighter());
                        }
                        else{
                            g2d.setColor(new Color(35,90,232));
                        }
                    }
                    if(cur.getOccupiedBy().equals(ai)){
                        g2d.setColor(new Color(212,25,37));
                    }
                }else {
                    g2d.setColor(Color.GREEN);
                }

                g2d.fill((cur.getShape()));
                g2d.setColor(Color.BLACK);
                g2d.draw((cur.getShape()));

                g2d.drawString(cur.getStationaryTroops()+"",(float) cur.getCapital().getX(), (float)cur.getCapital().getY());
                if(round.getPhase().equals("S")){
                    g2d.drawString("Verfügbare Verstärkung : "+human.getTroops(), 550,580);
                }
        }
        }
    }
}




