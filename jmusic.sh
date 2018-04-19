!/bin/bash

# Sebastian Foubert

cd $HOME

mkdir jMusic

cd $HOME/jMusic

wget https://cytranet.dl.sourceforge.net/project/jmusic/jMusic_Stable/1.6.5/jMusic1.6.5.jar
wget https://newcontinuum.dl.sourceforge.net/project/jmusic/jMusic_Instruments/1.6/jMusic_1.6_inst.zip

unzip jMusic_1.6_inst.zip

cd $HOME

echo export CLASSPATH=$CLASSPATH:$HOME/jMusic/jMusic1.6.5.jar:$HOME/jMusic/inst/ >> .bash_profile

source ~/.bash_profile

echo "You can always compile and execute using the -classpath flag"
