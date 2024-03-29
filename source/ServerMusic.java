import java.net.*;
import jm.util.*;
import jm.music.tools.*;
import jm.music.rt.*;
import jm.music.net.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
import java.util.*;
import jm.util.*;
import jm.midi.MidiSynth;
import jm.music.data.*;
import jm.JMC;
import jm.audio.*;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.sampled.*;
import java.io.File;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.IOException;


class Music_Service extends Play
{
	public Music_Service(){
	    super();
	}

	public static void au(String fileName, boolean autoClose) {
        jm.gui.wave.WaveFileReader afr = new jm.gui.wave.WaveFileReader(fileName);
        jm.music.rt.RTLine[] lineArray = {new jm.util.AudioRTLine(fileName)};
        jm.audio.RTMixer mixer = new jm.audio.RTMixer(lineArray) ;//, 4096, si.getSampleRate(), si.getChannels(), 0.01);
            mixer.begin();
            System.out.println("---------- Playing '" + fileName + "'... Sample rate = "
                               + afr.getSampleRate() + " Channels = " + afr.getChannels() + " ----------");
            if (autoClose) {
                java.io.File audioFile = new java.io.File(fileName);
                try {
                    int byteSize = afr.getBits() - 1;
                    // bytes, sample rate, channels, milliseconds, cautious buffer
                    Thread.sleep((int)((double)audioFile.length() / byteSize /
                                       afr.getSampleRate() / afr.getChannels() * 1000.0));
                } catch (InterruptedException e) {
		    mixer.stop();
                    System.err.println("jMusic play.au error: Thread sleeping interupted");
                }
                System.out.println("-------------------- Completed Audio Playback ----------------------");
     //           System.exit(0); // horrid, but less confusing for beginners
            }
    }

}



public class ServerMusic extends JFrame implements Runnable{
    JTextArea textArea;
    InputStreamReader isr;
    OutputStreamWriter outWriter;
    BufferedReader reader;
    PrintWriter writer;
    Socket clientConn;
    ServerSocket serverSock;
    boolean connected = false;

    private volatile Thread t1;
    private boolean  Trn = false;
    Music_Service music;

    ArrayList<Integer> map;
    int mapIndex = 0;

    // Buttons
    JButton nextsongButton;
    JButton nextsong2Button;
    JButton playButton;
    JButton play2Button;
    JButton moveupButton;
    JButton movedownButton;
    JButton stopButton;
    JButton randomButton;

    // Strings for the buttons
    private static String nextsongString = "Add Song";
    private static String playString = "Play";
    private static String moveupString = "Move Up";
    private static String movedownString = "Move Down";
    private static String stopString = "Stop";
    private static String randomString = "Random";

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
    JPanel button2Pane;
    JPanel movebuttonPane;
    JPanel textPane;

    // Linked List
    LLMusic songlistLL;

    // Card Panel
    JPanel cardsPane;
    JPanel nextsongPane;
    JPanel playPane;

    DefaultListModel<String> songModel;
    JList<String> songList;
    JScrollPane songListScrollPane;

    Song song;

    boolean DEBUG = false;

    public static boolean empty( final String s ) {
    // Null-safe, short-circuit evaluation.
	return s == null || s.trim().isEmpty();
    }


    public void go()
    {
	map = new ArrayList<Integer>();

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
			artistLabel.setFont(new Font("Arial",Font.BOLD,25));
      albumLabel.setLabelFor(albumField);
			albumLabel.setFont(new Font("Arial",Font.BOLD,25));
		  songtitleLabel.setLabelFor(songtitleField);
			songtitleLabel.setFont(new Font("Arial",Font.BOLD,25));
      filepathLabel.setLabelFor(filepathField);
			filepathLabel.setFont(new Font("Arial",Font.BOLD,25));
			imagepathLabel.setLabelFor(imagepathField);
			imagepathLabel.setFont(new Font("Arial",Font.BOLD,25));

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

    	nextsong2Button = new JButton(nextsongString);
    	play2Button = new JButton(playString);

	moveupButton = new JButton(moveupString);
	movedownButton = new JButton(movedownString);

	stopButton = new JButton(stopString);
	randomButton = new JButton(randomString);

	buttonPane = new JPanel();
	button2Pane = new JPanel();
	movebuttonPane = new JPanel(new GridLayout(0,1));

    	buttonPane.add(nextsongButton);
    	buttonPane.add(playButton);
    	button2Pane.add(nextsong2Button);
    	button2Pane.add(play2Button);
	button2Pane.add(randomButton);
	button2Pane.add(stopButton);
	movebuttonPane.add(moveupButton);
	movebuttonPane.add(movedownButton);

    	songlistLL = new LLMusic();

    	textArea = new JTextArea(10,20);
	textArea.setFont(new Font("Arial", Font.HANGING_BASELINE, 20));

	textPane = new JPanel(new GridLayout(0,1));

    	// Set ActionListener(this)
    	nextsongButton.addActionListener(new nextsongListener());
    	nextsong2Button.addActionListener(new nextsong2Listener());
    	playButton.addActionListener(new playListener());
    	play2Button.addActionListener(new play2Listener());
	moveupButton.addActionListener(new moveupListener());
	movedownButton.addActionListener(new movedownListener());
	stopButton.addActionListener(new stopListener());
	randomButton.addActionListener(new randomListener());

    	cardsPane = new JPanel(new CardLayout());
    	nextsongPane = new JPanel();
    	nextsongPane.setLayout(new BorderLayout());
    	playPane = new JPanel();
    	playPane.setLayout(new BorderLayout());

    	nextsongPane.add(BorderLayout.CENTER, fieldPane);
    	nextsongPane.add(BorderLayout.WEST, labelPane);
    	nextsongPane.add(BorderLayout.SOUTH, buttonPane);

	playPane.add(BorderLayout.WEST, movebuttonPane);
	playPane.add(BorderLayout.SOUTH, button2Pane);
	playPane.add(BorderLayout.CENTER, textArea);

        songModel = new DefaultListModel<String>();

      	songList = new JList<String>(songModel);
      	songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      	songList.setSelectedIndex(0);
      	songList.setVisibleRowCount(10);
	songList.setFont(new Font("Arial", Font.HANGING_BASELINE, 20));

      	songListScrollPane = new JScrollPane(songList);

	textPane.add(textArea);
	textPane.add(songListScrollPane);

	playPane.add(BorderLayout.CENTER,textPane);

	cardsPane.add(nextsongPane,"Select");
    	cardsPane.add(playPane,"Play");

    	this.getContentPane().add(BorderLayout.CENTER, cardsPane);

    	this.setSize(600, 600);
    	this.setTitle("Sad Face");
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	this.setVisible(true);
	music = new Music_Service();
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
		textArea.setText(": " + albumInfo);
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

	    if(empty(artistField.getText())){
		artistField.setText("Not Valid");
		return;
	    }
	    if(empty(albumField.getText())){
		albumField.setText("Not Valid");
		return;
	    }
	    if(empty(songtitleField.getText())){
		songtitleField.setText("Not Valid");
		return;
	    }
	    if(empty(filepathField.getText())){
		filepathField.setText("Not Valid");
		return;
	    }
	    if(empty(imagepathField.getText())){
		imagepathField.setText("Not Valid");
		return;
	    }
	    Song song = new Song(artistField.getText(),
	        			albumField.getText(),
    					songtitleField.getText(),
    					filepathField.getText()
    					);

    	    songlistLL.songAdd(song, imagepathField.getText());
	    artistField.setText("");
	    albumField.setText("");
	    songtitleField.setText("");
	    filepathField.setText("");
	    imagepathField.setText("");

	    songModel.addElement(song.Song_Title + " by " + song.Artist);
	    songlistLL.ll_Content();
	    map.add(mapIndex++);
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

    public class nextsong2Listener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    	    CardLayout cardLayout = (CardLayout) cardsPane.getLayout();
    	    cardLayout.show(cardsPane, "Select");
	}
    }

    public class stopListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
	    jStop();
	}
    }

    public class randomListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
	    // TBD
	    Random rand = new Random();
	    int rand_int = rand.nextInt(mapIndex);

	    song =  songlistLL.ll_Index(map.get(rand_int));

	    if(Trn)
		jStop();
	    else
		Trn = true;

	    jMusicInter();

	    System.out.println(song.Song_Title + song.Artist);
	    if(connected)
	    {
		writer.println(song.Song_Title + " " + song.Artist);
		writer.flush();
	    }
	    else
	    {
		System.out.println("The server has not established a connection yet.");
	    }

	}
    }

    public class play2Listener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
	    song =  songlistLL.ll_Index(map.get(songList.getLeadSelectionIndex()));

	    if(Trn)
				jStop();
	    else
				Trn = true;

	    jMusicInter();


	    System.out.println(song.Song_Title + song.Artist);
	    if(connected)
	    {
		writer.println("Now Playing: " + song.Song_Title + " by " + song.Artist);
		writer.flush();
	    }
	    else
	    {
		System.out.println("The server has not established a connection yet.");
	    }
    	}
    }

    public class moveupListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
	    int location = songList.getLeadSelectionIndex();
	    if(location > 0){
		map.add(location - 1, map.remove(location));
		songModel.add(location - 1, songModel.remove(location));
	    }
    	}
    }

    public class movedownListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
	    int location = songList.getLeadSelectionIndex();
	    System.out.println(location + " " + mapIndex + " " + map.size());
	    if(mapIndex - 1 > location && location >= 0){
		map.add(location + 1, map.remove(location));
		songModel.add(location + 1, songModel.remove(location));
	    }
    	}
    }
    public void jMusicInter(){
	t1 = new Thread(new Runnable() {
	    public void run(){
		jPlay();
	    }
	});
	Trn = true;
	t1.start();

    }

    public void jStop(){ // FIX ASAP
	System.out.println(" " + Trn);
	if(DEBUG)
	    tHD();
	if(Trn){
	    Thread moribound = t1;
	    t1 = null;
	    moribound.interrupt();
	    Trn = false;
	}
    }

    public void jPlay(){
	music.au(song.getFile_Path(),true);
    }

    public void tHD(){
	Set<Thread> threads = Thread.getAllStackTraces().keySet();

	for (Thread t : threads) {
    	String name = t.getName();
    	Thread.State state = t.getState();
    	int priority = t.getPriority();
    	String type = t.isDaemon() ? "Daemon" : "Normal";
    	System.out.printf("%-20s \t %s \t %d \t %s\n", name, state, priority, type);
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
