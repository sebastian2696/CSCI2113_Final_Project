import jm.audio.*;
import jm.JMC;
import jm.music.tools.*;
import jm.music.rt.*;
import jm.music.net.*;
import jm.music.data.*;
import jm.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Music_Service extends JFrame implements ActionListener
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
		float[] audio = Read.audio("/Users/goku2696/Downloads/J_Balvin_Ft__Daddy_Yankee_Don_Omar_Arcangel_Farruko_Yandel_Nicky_Jam_De_La_Ghetto___Zion_-_Ginza__Remix_.wav");
		System.out.println("Song length  is:"+audio.length/44100+"seconds");
		area.setText("Now Playing Blessings by Big Sean" );
		Play.au("/Users/goku2696/Downloads/J_Balvin_Ft__Daddy_Yankee_Don_Omar_Arcangel_Farruko_Yandel_Nicky_Jam_De_La_Ghetto___Zion_-_Ginza__Remix_.wav");
		this.repaint();
	}
	public static void main(String[] args)
	{
		Music_Service newWindow = new Music_Service();
		newWindow.go();
	}
}
