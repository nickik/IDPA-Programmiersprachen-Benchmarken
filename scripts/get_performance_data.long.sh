#!/bin/sh
lang=$1
code=$2
#
#Check parameter
if [ ! -f $code ] ; then
	echo 'File "'$code'" not found!'
	exit 0
fi
#
#Overwrite existing files?
for tfile in /opt/data/$lang.* ] ; do
	if [ -f $tfile ] ; then
		echo 'Bestehende Dateien überschreiben? Geben Sie "Y" ein um die bestehenden Datei zu löschen oder drücken Sie Enter um neuen Messdaten in die bestehenden Dateien zu schreiben!'
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
#
case $lang in
java)
	#Create a title
	echo "#
####$(date +%d.%m.%Y" "%T)####
#" | tee -a /opt/data/$lang.memory /opt/data/$lang.cpu /opt/data/$lang.time > /dev/null
	#Get Memory Data
	java -server -jar $code &
	#Is the process still running?
	while ps $! > /dev/null
	do
		sed -n '11,20p' /proc/$!/status >> /opt/data/$lang.memory
		echo '-----------------------' >> /opt/data/$lang.memory		
		sleep .2
	done
	#Get CPU Data
	random=tmp$(date +%d%m%Y_%H%M%S)
	java -server -jar $code &	
	while ps $! > /dev/null
	do
		ps -p $! -o user,pid,%cpu,c,time >> /opt/data/$random
		sleep .2
	done
	sed '3~2d' /opt/data/$random >> /opt/data/$lang.cpu
	rm /opt/data/$random
	#Get Time Data
	/usr/bin/time -f "Elapsed real time: %E\nCPU usage: %P\nTotal CPU-seconds in user mode: %U\nTotal CPU-seconds in kernel mode: %S\nSignals delivered to the process: %k\nName and arguments of the command: %C" -o /opt/data/$lang.time -a java -server -jar $code
	;;
clojure)
	#Create a title
	echo "#
####$(date +%d.%m.%Y" "%T)####
#" | tee -a /opt/data/$lang.memory /opt/data/$lang.cpu /opt/data/$lang.time > /dev/null
	#Get Memory Data
	java -server -jar $code &
	#Is the process still running?
	while ps $! > /dev/null
	do
		sed -n '11,20p' /proc/$!/status >> /opt/data/$lang.memory
		echo '-----------------------' >> /opt/data/$lang.memory		
		sleep .2
	done
	#Get CPU Data
	random=tmp$(date +%d%m%Y_%H%M%S)
	java -server -jar $code &	
	while ps $! > /dev/null
	do
		ps -p $! -o user,pid,%cpu,c,time >> /opt/data/$random
		sleep .2
	done
	sed '3~2d' /opt/data/$random >> /opt/data/$lang.cpu
	rm /opt/data/$random
	#Get Time Data
	/usr/bin/time -f "Elapsed real time: %E\nCPU usage: %P\nTotal CPU-seconds in user mode: %U\nTotal CPU-seconds in kernel mode: %S\nSignals delivered to the process: %k\nName and arguments of the command: %C" -o /opt/data/$lang.time -a java -server -jar $code
	;;
scala)
	#Create a title
	echo "#
####$(date +%d.%m.%Y" "%T)####
#" | tee -a /opt/data/$lang.memory /opt/data/$lang.cpu /opt/data/$lang.time > /dev/null
	#Get Memory Data
	java -server -jar $code &
	#Is the process still running?
	while ps $! > /dev/null
	do
		sed -n '11,20p' /proc/$!/status >> /opt/data/$lang.memory
		echo '-----------------------' >> /opt/data/$lang.memory		
		sleep .2
	done
	#Get CPU Data
	random=tmp$(date +%d%m%Y_%H%M%S)
	java -server -jar $code &	
	while ps $! > /dev/null
	do
		ps -p $! -o user,pid,%cpu,c,time >> /opt/data/$random
		sleep .2
	done
	sed '3~2d' /opt/data/$random >> /opt/data/$lang.cpu
	rm /opt/data/$random
	#Get Time Data
	/usr/bin/time -f "Elapsed real time: %E\nCPU usage: %P\nTotal CPU-seconds in user mode: %U\nTotal CPU-seconds in kernel mode: %S\nSignals delivered to the process: %k\nName and arguments of the command: %C" -o /opt/data/$lang.time -a java -server -jar $code
	;;
*)
	echo 'Enter java, clojure or scala for Parameter 1!'
esac

