# Software Engineering Final Project

## Prompt

The final project is to design a network connected music player. Each instance of your music player class should have a member variable that's a string that allows you to store a username.


Your music player will allow you to enter the following information for each song:
- Artist Name
- Album Name
- Song Title
- Filename for Song
- Filename for album cover art


You should be able to add a number of songs to a list, and you should have two buttons on your main interface. Play next song and Play random song. The play next song button should play the next song in your list in the order the song was added. Play random song should choose a random song in your list.

You are allowed to use java.util.LinkedList to store the song objects in the order they are added to the music player. You should also provide a way for the user to view the song list (using a JList or JTextArea), in the order that songs have been added.
As your song is playing, your interface should display the Artist,Song Title and Album Name, and should show a picture of the album cover art for that song. Your application should also send your username, Song Title, Artist Name and Album Name over the network as a string to at least one connected music player client. Your application should also be able to connect to at least one other music player, and receive a string sent over the network that captures what song the other user is currently playing. This now playing data should be accumulated in a JTextArea widget.
Extra points will be assigned to teams that incorporate functionality to select songs to be played from the JList, move songs up or down the playlist, and use another data structure so that an album cover file name only needs to be stored once per album.
