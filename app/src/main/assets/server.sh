#!/bin/bash
#set -x

VIDEO_FILES=( ); # array to store all available *.ts files at the moment
VIDEO_FILES_MAX=10; # how many elements can be stored in $VIDEO_FILES array
LIST_LEN=0; #*.ts list length

VIDEO_WINDOW=""; # array to store current video files window
VIDEO_WINDOW_LEN=3; # how many files we are storing in the window

LAST_CONVERTED=0; # ID of last converted video slice

RAW_SLICES_PATH="/tmp/DV/"; # where to look for raw video slices
MP4_SLICES_PATH="/tmp/MP4/"; # where to place converted chunks
MP4_SLICES_WEBPATH="http://istream.jurnaltv.md/live/"; # web path from the user`s POV
SLICE_DURATION=10; # seconds, 10-15 seconds recomended by Apple
M3U_FILE_NAME="/tmp/MP4/live.m3u"; # full path to the m3u index file

FFMPEG_CMD="/usr/local/bin/ffmpeg -y -i ";

update_m3u() {
# updating number of elements
LIST_LEN=${#VIDEO_FILES[@]};
echo "Number of elements in array is: $LIST_LEN ";
echo -n "(";
for slice in ${VIDEO_FILES[@]}
do
echo -n "${slice} ";
done
echo ")";
echo;
# getting last $VIDEO_WINDOW_LEN files from array
let LAST_IDX=LIST_LEN-VIDEO_WINDOW_LEN;
if [ $LAST_IDX -le 0 ]
then
LAST_IDX=0;
fi
echo "Last index we must use is $LAST_IDX";
# recreating m3u file
# getting slice id from $LAST_CONVERTED
SLICE_ID=0;
let SLICE_ID=LAST_CONVERTED-VIDEO_WINDOW_LEN;
if [ $SLICE_ID -le 0 ]
then
SLICE_ID=0;
fi
echo "------------- DUMP START ------------- ";
echo "#EXTM3U">$M3U_FILE_NAME;
echo "#EXT-X-TARGETDURATION:$SLICE_DURATION">>$M3U_FILE_NAME;
echo "#EXT-X-MEDIA-SEQUENCE:$SLICE_ID">>$M3U_FILE_NAME;
i=$LAST_IDX;
while [ $i -lt $LIST_LEN ]; do
echo "#EXTINF:${SLICE_DURATION},">>$M3U_FILE_NAME;
echo "${MP4_SLICES_WEBPATH}${VIDEO_FILES[${i}]}">>$M3U_FILE_NAME;
let i++;
done
echo "------------- DUMP END ------------- ";

# if array length is greater than $VIDEO_FILES_MAX - remove first element and compact array: array=( "${array[@]}" )
if [ $LIST_LEN -ge $VIDEO_FILES_MAX ]
then
echo "Packing array by removing first element";
echo ${MP4_SLICES_PATH}${VIDEO_FILES[0]};
rm -f ${MP4_SLICES_PATH}${VIDEO_FILES[0]};
unset VIDEO_FILES[0];
VIDEO_FILES=( "${VIDEO_FILES[@]}" );
fi
echo "-------";
}

# gracefly handle SIG_TERM
on_sigterm() {
echo "Got sigterm, exiting!";
RUN="0";
}

trap 'on_sigterm' TERM

# cleanup source and converted folders
rm -f ${RAW_SLICES_PATH}*.dv;
rm -f ${MP4_SLICES_PATH}*.dv;

# forever do
# convert video
# move to MP4
# erase original
# add converted to the tail of array
# update live.m3u file for $VIDEO_WINDOW_LEN files
# if array len>$VIDEO_FILES_MAX
# then remove first element from array and compact array it
# forever end

RUN="1";
raw_slice="";

while [ $RUN -eq "1" ]; do
#getting oldest file from the list of slices
raw_slice=`ls -tr ${RAW_SLICES_PATH}|head -1`;
if [ "$raw_slice" != "" ];
then
OPEN_FLAG=`lsof|grep $raw_slice|wc -l`;
if [ $OPEN_FLAG -eq 0 ];
then
#converting video
echo "Converting ${raw_slice}">>/tmp/istream.txt
#sleep 6; # simulating transcoding delay
mp4_slice="live-${LAST_CONVERTED}.ts";
$FFMPEG_CMD ${RAW_SLICES_PATH}${raw_slice} -acodec libfaac -ac 1 -ar 48000 -ab 96k -vcodec libx264 -vpre baseline -vpre fast -vpre ipod640 -b 800k -g 5 -async 25 -keyint_min 5 -s 512x256 -aspect 16:9 -bt 100k -maxrate 800k -bufsize 800k -deinterlace -f mpegts ${MP4_SLICES_PATH}${mp4_slice}
rm -f ${RAW_SLICES_PATH}$raw_slice
LIST_LEN=${#VIDEO_FILES[@]};
VIDEO_FILES[${LIST_LEN}]=$mp4_slice;
#generating m3u file
let LAST_CONVERTED++;
update_m3u;
else
sleep 1; # sleep one second
echo "Waiting for file to be closed!";
fi
else
sleep 1; # sleep one second
echo "Sleeping!"
fi done