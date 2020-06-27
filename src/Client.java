import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;

public class Client extends JFrame implements Runnable {
       public JPanel panelEnd, panelStart;
       public JTextArea textArea;
       public JScrollPane scrollPane;
       public JTextField txMsg, txName;
       public JButton btSend;
       public JLabel Lname;

       private DatagramSocket socket;
       private InetAddress address;
       private byte[] buf = new byte[256];
       private byte[] bufSend = new byte[256];
       public String MsgIn="";

    Client() throws Exception {
        init();
        socket = new DatagramSocket(2000);
        address = InetAddress.getByName("localhost");
        run();

    }


    private void init(){
        setTitle("UDP Client unicast");
        Lname = new JLabel("Name");
        panelEnd = new JPanel();
        panelStart = new JPanel();
        btSend = new JButton("Send");
        txName = new JTextField(10);
        textArea = new JTextArea(20,40);
        txMsg = new JTextField(10);
        scrollPane = new JScrollPane(textArea);
        textArea.setEnabled(false);

        panelStart.add(Lname);
        panelStart.add(txName);
        panelEnd.add(txMsg);
        panelEnd.add(btSend);

        add(panelStart, BorderLayout.PAGE_START);
        add(panelEnd, BorderLayout.PAGE_END);

        add(scrollPane, BorderLayout.CENTER);

        btSend.addActionListener(SendDataAction);
        pack();
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void run() {

           while (!MsgIn.equals("end")){


               try {
                   DatagramPacket packet = new DatagramPacket(buf, buf.length);
                   socket.receive(packet);
                   MsgIn = new String(packet.getData(), 0, packet.getLength());
                   textArea.setText(textArea.getText()+"\n"+MsgIn);
               } catch (IOException e) {
                   e.printStackTrace();
               }

           }

           socket.close();
    }

    ActionListener SendDataAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (txName.getText().isEmpty()){
                JOptionPane.showMessageDialog(panelStart, "Please put your name!!!");
            }else if (txMsg.getText().isEmpty()){
                JOptionPane.showMessageDialog(panelEnd, "Please put your name!!!");
            }else {
                try {
                    SendData(txName.getText() + " : " +txMsg.getText());
                } catch (Exception unknownHostException) {
                    unknownHostException.printStackTrace();
                }
            }

        }
    };

    public void SendData(String msg) throws Exception {
        DatagramPacket msgSend = new DatagramPacket(msg.getBytes(), msg.getBytes().length, InetAddress.getByName("localhost"), 6000);
        System.out.println("MSG : "  + msg);
        socket.send(msgSend);
    }
}
