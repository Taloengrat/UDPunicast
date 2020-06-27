import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server extends JFrame implements Runnable{
    public JPanel panelEnd, panelStart;
    public JTextArea textArea;
    public JScrollPane scrollPane;
    public JTextField txMsg;
    public JButton btSend;
    private String MsgReceive="";

    private DatagramSocket socket;
    private byte[] buf = new byte[256];

    Server() throws Exception{

        init();
        socket = new DatagramSocket(6000);
//        run();

    }

    public void init(){
        setTitle("UDP Server unicast");
        panelEnd = new JPanel();
        panelStart = new JPanel();
        btSend = new JButton("Send");
        textArea = new JTextArea(20,40);
        txMsg = new JTextField(10);
        scrollPane = new JScrollPane(textArea);
        textArea.setEnabled(false);

        panelEnd.add(txMsg);
        panelEnd.add(btSend);

        add(panelStart, BorderLayout.PAGE_START);
        add(panelEnd, BorderLayout.PAGE_END);

        btSend.addActionListener(SendDataAction);
        add(scrollPane, BorderLayout.CENTER);
        pack();
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void run() {
        while (!MsgReceive.equals("end")){
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                MsgReceive = new String(packet.getData(), 0, packet.getLength());
                textArea.setText(textArea.getText()+"\n"+MsgReceive);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }


    private void SendMessage(String msg) throws Exception {
        DatagramPacket packetSend = new DatagramPacket(msg.getBytes(), msg.getBytes().length, InetAddress.getByName("localhost"), 2000);
        socket.send(packetSend);

    }

    ActionListener SendDataAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (txMsg.getText().isEmpty()){
                JOptionPane.showMessageDialog(panelStart, "Please put your message");
            }else{
                try {
                    SendMessage("Server : "+txMsg.getText());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        }
    };
}
