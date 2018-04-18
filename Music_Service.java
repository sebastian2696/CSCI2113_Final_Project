import jm.audio.*;
import jm.JMC;
import jm.music.*;
import jm.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class GetAudioFile extends JFrame implements ActionListener
{
	JTextField text;
	JTextArea area;
	JButton button;

	public void go()
	{
		button = new JButton("Play audio");
		area = new JTextArea(10,10);
		area.setText("Song info goes here!");
		button.addActionListener(this);
		text = new JTextField(20);
		this.getContentPane().add(BorderLayout.NORTH, text);
		this.getContentPane().add(BorderLayout.SOUTH, button);
		this.getContentPane().add(BorderLayout.CENTER, area);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(300,300);
		this.setResizable(true);
		this.setVisible(true);
	}
	public void actionPerformed(ActionEvent a)
	{
		float[] audio = Read.audio("C:\\Users\\dljames.SEAS14991\\Downloads\\Blessings.wav");
		System.out.println("Song length  is:"+audio.length/44100+"seconds");
		area.setText("Now Playing Blessings by Big Sean" );
		Play.au("C:\\Users\\dljames.SEAS14991\\Downloads\\Blessings.wav");
		this.repaint();
	}
	public static void main(String[] args)
	{
		GetAudioFile newWindow = new GetAudioFile();
		newWindow.go();
	}
}
