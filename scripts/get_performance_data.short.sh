#!/bin/sh
lang=$1
code=$2
param=$3
#
#Error handling
trap "error" ERR
#
function error() {
	echo '
##########
Ein unbekannter Fehler ist aufgetreten. Prüfen Sie die folgenden möglichen Fehlerquellen:
- ungültige Java Parameter
- fehlerhafte Programmdatei (*.jar)
##########'
	exit 0
}

#Check parameters
if [ $lang != clojure -a $lang != java -a $lang != scala ] ; then
	echo 'Enter clojure, java or scala for Parameter 1!'
	exit 0
fi
#
if [ ! -f $code ] ; then
	echo 'File "'$code'" not found!'
	exit 0
fi
#
#Overwrite existing files?
for tfile in /opt/data/$lang.* ] ; do
	if [ -f $tfile ] ; then
		echo '
Bestehende Dateien überschreiben? Geben Sie "Y" ein um die bestehenden Datei zu löschen oder drücken Sie Enter um neuen Messdaten in die bestehenden Dateien zu schreiben!'
		read answer
		if [ "$answer" = y -o "$answer" = Y ] ; then
			rm /opt/data/$lang.*
			echo 'Dateien wurden gelöscht!'
			break
		else
			echo 'Daten werden in bestehende Dateien geschrieben!'
			break		
		fi			
	fi
done
#
#Create a title
echo "#
####$(date +%d.%m.%Y" "%T)####
#" | tee -a /opt/data/$lang.memory /opt/data/$lang.cpu /opt/data/$lang.time > /dev/null
#Get Memory Data
echo '
Collecting Memory Data...
'
java -server -jar $code $param & 
#Is the process still running?
while ps $! > /dev/null
do
	sed -n '11,20p' /proc/$!/status >> /opt/data/$lang.memory
	echo '-----------------------' >> /opt/data/$lang.memory		
	sleep .2
done
echo '
done'
#Get CPU Data
echo '
Collecting CPU Data...
'
random=tmp$(date +%d%m%Y_%H%M%S)
java -server -jar $code $param &	
while ps $! > /dev/null
do
	ps -p $! -o user,pid,%cpu,c,time >> /opt/data/$random
	sleep .2
done
sed '3~2d' /opt/data/$random >> /opt/data/$lang.cpu
rm /opt/data/$random
echo '
done'
#Get Time Data
echo '
Collecting Time Data...
'
time -f "Elapsed real time: %E\nCPU usage: %P\nTotal CPU-seconds in user mode: %U\nTotal CPU-seconds in kernel mode: %S\nSignals delivered to the process: %k\nName and arguments of the command: %C" -o /opt/data/$lang.time -a java -server-jar $code $param
echo '
done'
