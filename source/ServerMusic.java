import java.net.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
public class ServerMusic extends JFrame implements ActionListener, Runnable{
	//JTextField Artist, Album, Song_Title, File_Path, Image_Path;
	//JPanel Options;
	JTextArea textArea;
	InputStreamReader isr;
	OutputStreamWriter outWriter;
	BufferedReader reader;
	PrintWriter writer;
	Socket clientConn;
	ServerSocket serverSock;
	boolean connected = false;

	// Buttons
	JButton nextsongButton;
	JButton playButton;

	// Strings for the buttons
	private static String nextsongString = "Save and New";
	private static String playString = "Play";

    // Labels to identify the fields
    JLabel artistLabel;
    JLabel albumLabel;
    JLabel songtitleLabel;
    JLabel filepathLabel;
	JLabel imagepathLabel;

    // Strings for the labels
    private static String artistString = "Artist: ";
    private static String albumString = "Album: ";
    private static String songtitleString = "Song: ";
    private static String filepathString = "Path: ";
	private static String imagepathString = "Image Path: ";

    // Fields for data entry
    JTextField artistField;
    JTextField albumField;
    JTextField songtitleField;
    JTextField filepathField;
	JTextField imagepathField;

	// Panels
	JPanel fieldPane;
	JPanel labelPane;
	JPanel buttonPane;

	// Linked List
	LLMusic songlistLL;

	// Card Panel
	JPanel cardsPane;
	JPanel nextsongPane;
	JPanel playPane;


	public void go()
	{
		artistLabel = new JLabel(artistString);
		albumLabel = new JLabel(albumString);
		songtitleLabel = new JLabel(songtitleString);
		filepathLabel = new JLabel(filepathString);
		imagepathLabel = new JLabel(imagepathString);

		artistField = new JTextField();
		albumField = new JTextField();
		songtitleField = new JTextField();
		filepathField = new JTextField();
		imagepathField = new JTextField();

        artistLabel.setLabelFor(artistField);
        albumLabel.setLabelFor(albumField);
        songtitleLabel.setLabelFor(songtitleField);
        filepathLabel.setLabelFor(filepathField);
		imagepathLabel.setLabelFor(imagepathField);

		fieldPane = new JPanel(new GridLayout(0,1));

		fieldPane.add(artistField);
		fieldPane.add(albumField);
		fieldPane.add(songtitleField);
		fieldPane.add(filepathField);
		fieldPane.add(imagepathField);

		labelPane = new JPanel(new GridLayout(0,1));

		labelPane.add(artistLabel);
		labelPane.add(albumLabel);
		labelPane.add(songtitleLabel);
		labelPane.add(filepathLabel);
		labelPane.add(imagepathLabel);

		nextsongButton = new JButton(nextsongString);
		playButton = new JButton(playString);

		buttonPane = new JPanel();

		buttonPane.add(nextsongButton);
		buttonPane.add(playButton);

		songlistLL = new LLMusic();

		textArea = new JTextArea(10,20);
		artistField.addActionListener(this);

		// Set ActionListener(this)
		nextsongButton.addActionListener(new nextsongListener());
		playButton.addActionListener(new playListener());

		cardsPane = new JPanel(new CardLayout());
		nextsongPane = new JPanel();
		nextsongPane.setLayout(new BorderLayout());
		playPane = new JPanel();
		playPane.setLayout(new BorderLayout());

		nextsongPane.add(BorderLayout.CENTER, fieldPane);
		nextsongPane.add(BorderLayout.WEST, labelPane);
		nextsongPane.add(BorderLayout.SOUTH, buttonPane);

//		playPane.add(BorderLayout.CENTER, labelPane);
//		playPane.add(BorderLayout.SOUTH, buttonPane);

		cardsPane.add(nextsongPane,"Select");
		cardsPane.add(playPane,"Play");

		this.getContentPane().add(BorderLayout.CENTER, cardsPane);

//		this.getContentPane().add(BorderLayout.CENTER, fieldPane);
//		this.getContentPane().add(BorderLayout.WEST, labelPane);
//		this.getContentPane().add(BorderLayout.SOUTH, buttonPane);
		this.setSize(600, 600);
		this.setTitle("Text Window Server");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		setUpNetworking();
	}

	public void setUpNetworking()
	{
		try
		{
			serverSock = new ServerSocket(8000);
			clientConn =serverSock.accept();
			System.out.println("Connection was successful");
			isr = new InputStreamReader(clientConn.getInputStream());
			reader = new BufferedReader(isr);
			outWriter = new OutputStreamWriter(clientConn.getOutputStream());
			writer = new PrintWriter(outWriter);
			writer.println("This is a message from the server!!");
			connected = true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void run()
	{
		try
		{
			String albumInfo=reader.readLine();
			while(albumInfo!= null)
			{
				textArea.append(albumInfo+"\n");
				albumInfo=reader.readLine();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public class nextsongListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			songlistLL.songAdd(new Song(artistField.getText(),
										 albumField.getText(),
										 songtitleField.getText(),
										 filepathField.getText(),
										 imagepathField.getText()
										));

			artistField.setText("");
			albumField.setText("");
			songtitleField.setText("");
			filepathField.setText("");
			imagepathField.setText("");

			songlistLL.ll_Content();
		}
	}

	public class playListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			CardLayout cardLayout = (CardLayout) cardsPane.getLayout();
			cardLayout.show(cardsPane, "Play");
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		if(connected)
		{
			writer.println(artistField.getText());
			writer.flush();
		}
		else
		{
			System.out.println("The server has not established a connection yet.");
		}
	}
	public static void main(String[] args)
	{
		ServerMusic win = new ServerMusic();
		win.go();
		Thread t = new Thread(win);
		t.start();
	}
}
