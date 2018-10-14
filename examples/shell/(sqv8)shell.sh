#!/bin/bash



if which jq >/dev/null;then
    y=y
else
    echo "jq not found,try apt install jq first"
fi
if which ncat >/dev/null;then
    y=y
else
    echo "ncat not found,try apt install ncat first"
fi
# ====== Set the constants below =======
killall -9 ncat
# The websocket server and port
WS_SERVER="localhost"
WS_PORT=8888

# The header values to use when performing a websocket handshake.
HS_GET="/"
HS_ORIGIN="http://127.0.0.1"
HS_HOST="127.0.0.1"

# This script uses a local netcat server to forward messages to the remote server.
WS_LOCAL_PORT=9999

# ===== Do not edit below this line =====

split_hex()
{
  local hex code
  while read -n 2 code
  do
    if [ -n "$code" ]
    then
      hex="$hex\x$code"
    fi
  done
  echo -n "$hex"
}

ws_send()
{
  local data length
  while true
  do
    # Binary frame: 0x82 [length] [data]
    # Max length: 00-7D -> 125; 0000-FFFF -> 65535
    data=$(dd bs=65535 count=1 2>/dev/null)
    length=$(echo -n "$data" | wc -c)
    # exit if received 0 bytes
    [ "$length" -gt 0 ] || break
    if [ "$length" -gt 125 ]
    then
      printf "\x81\x7E$(printf '%04x' ${length} | split_hex)"
    else
      printf "\x81\x$(printf '%02x' ${length})"
    fi
    echo -n "$data"
  done
}




# generate a random Sec-WebSocket-Key
random_bytes="$(dd if=/dev/urandom bs=16 count=1 2> /dev/null)"
HS_KEY=`echo "$random_bytes" | base64`

handshake="\
GET $HS_GET HTTP/1.1\r
Origin: $HS_ORIGIN\r
Connection: Upgrade\r
Host: $HS_HOST\r
Sec-WebSocket-Key: $HS_KEY\r
Upgrade: websocket\r
Sec-WebSocket-Version: 13\r\n\r\n"


sendMessage(){
local type="$1";
local groupid="$2";
local message="$3";
local message_type="$4";
local qquin="$5";
local time="$6";
local code="$7";
local title="$8";
local value="$9";
local groupidName=${10};
message_to_send="{\"from\": \"client\", \"msgparams\": {\"method\": \"send_message\", \"message\": \"$message\", \"message_type\": \"$message_type\", \"groupid\": $groupid, \"qquin\": $qquin, \"type\": $type, \"title\": \"$title\", \"time\": $time, \"code\": $code, \"value\": $value}}"
echo "$message_to_send" |ws_send| ncat localhost $WS_LOCAL_PORT
if [[ $type == 0 || $type == 2 ]];then
    echo "发送群组消息 群:$groupidName 消息:$message"
elif [[ $type == 1 ]];then
    echo "发送好友消息 qq:$sendid 消息:$message"
elif [[ $type == 4 ]];then
    echo "发送私聊消息 群:$groupid qq:$sendid 消息:$message"
fi
}


onQQmessage(){
local type=$1;
local groupid=$2;
local sendid=$3;
local msg=$4;
local nick=$5;
local ATQ=$6;
local ATname=$7;
local robort=$8;
local sendtime=$9;
local groupidName=${10};
local code=0;
if [ $type = 4 ];then
    code=$groupid
fi
    #type----消息类型
    #groupid----群号码
    #sendid----信息发出者qq号码
    #msg----消息内容
    #nick----消息发出者qq昵称
    #ATQ----被艾特的qq号
    #ATname----被艾特qq昵称
    #robort----登陆机器人qq号码
    #sendtime----消息发送时间
    #groupidName----群名称
if [ $msg = "\"Hello\"" ];then
      #@sendMessage(type,groupid,message,message_type,qquin,time,code,title,value)
        #type----消息类型 0:群消息 1好友消息 2:讨论组 3:系统消息 4:群临时消息 9:设置群名片 10:成员禁言 11:全体禁言 12:踢人
        #groupid----群号
        #message----消息内容
        #message_type----消息内容类型 msg:文本 xml:卡片 img:图片 json:json消息
        #qquin----发送目标qq/禁言qq/名片操作qq/踢人目标qq
        #time----发送消息时间
        #code----发送临时消息必须有这个，设置成跟群号一样就可以了
        #title----改名片时的名片内容
        #value----禁言时长(秒)
    sendMessage "$type" "$groupid" "World" "msg" "$sendid" 0 "$code"  "" 0 "$groupidName"
elif [ $msg = "\"测试卡片\"" ];then
    sendMessage "$type" "$groupid" '<msg serviceID=\"2\" templateID=\"1\" action=\"web\" brief=\"酷狗音乐\" sourceMsgId=\"0\" url=\"\" flag=\"0\" adverSign=\"0\" multiMsgFlag=\"0\"><item layout=\"2\"><audio cover=\"http://singerimg.kugou.com/uploadpic/softhead/400/20170426/20170426152155521.jpg\" src=\"http://fs.w.kugou.com/201809150140/0fe84be3831cea79c86d693e721f0e7b/G012/M07/01/09/rIYBAFUKilCAV55kADj2J33IqoI680.mp3\" /><title>Innocence</title><summary>Avril Lavigne</summary></item><source name=\"酷狗音乐\" icon=\"http://url.cn/4Asex5p\" url=\"http://url.cn/SXih4O\" action=\"app\" a_actionData=\"com.kugou.android\" i_actionData=\"tencent205141://\" appid=\"205141\" /></msg>' "xml" "$sendid" 0 "$code"  "" 0 "$groupidName" #卡片最好用''单引号强制禁止转移
elif [ $msg = "\"测试图片\"" ];then
    sendMessage "$type" "$groupid" "https://i.loli.net/2018/10/10/5bbda09b17a1a.png" "img" "$sendid" 0 "$code"  "" 0 "$groupidName"
elif [ $msg = "\"开启全体禁言\"" ];then #全体禁言
    sendMessage 11 "$groupid" "" "" 0 0 0 "" 0 "$groupidName"
elif [ $msg = "\"关闭全体禁言\"" ];then #全体禁言
    sendMessage "$type" "$groupid" "貌似不支持关闭全体禁言" "msg" "$sendid" 0 "$code"  "" 0 "$groupidName"
fi

}







messageFactory(){
local type="";
local groupid="";
local sendid="";
local msg="";
local nick="";
local ATQ="";
local ATname="";
local robort="";
local sendtime="";
local groupidName="";
local json_data

json_data=$(echo "$@"|grep -aPo  "{\"from.*"|jq ".msgparams");
type=$(echo "$json_data"|jq ".type")
groupid=$(echo "$json_data"|jq ".groupid")
sendid=$(echo "$json_data"|jq ".sendid")
msg=$(echo "$json_data"|jq ".message")
nick=$(echo "$json_data"|jq ".nick")
ATQ=$(echo "$json_data"|jq ".ATQ")
ATname=$(echo "$json_data"|jq ".ATname")
robort=$(echo "$json_data"|jq ".robort")
sendtime=$(echo "$json_data"|jq ".sendtime")
groupidName=$(echo "$json_data"|jq ".groupidname")
#终端输出消息
if [[ $type == 0 || $type == 2 ]];then
    echo "收到群组消息 群:$groupidName 消息:$msg"
elif [[ $type == 1 ]];then
    echo "收到好友消息 qq:$sendid 消息:$msg"
elif [[ $type == 4 ]];then
    echo "收到私聊消息 群:$groupid qq:$sendid 消息:$msg"
fi
onQQmessage "$type" "$groupid" "$sendid" "$msg" "$nick" "$ATQ" "$ATname" "$robort" "$sendtime" "$groupidName"
}



start=$(printf "\x81\x82")
ping=$(printf "\x89")
pong=$(printf "\x8a")

loop(){
while read -n 1 line
    do
       if [[ $line == "" ]];then
              line=" "
       else
           if [ "$line" = $ping ];then #心跳 x89为ping x8a为pong
               echo "接受心跳Ping"
                printf "\x8a\x00"| ncat localhost $WS_LOCAL_PORT
                echo "发送心跳Pong"
                message=""
            elif [ "$line" = $pong ];then #心跳 x89为ping x8a为pong
               echo "接收心跳Pong"
               message=""
            elif [[ $start =~ $line ]];then
                message=""
            fi
        fi
        message=$message$line
        if [[ $message == *}} ]];then
            messageFactory $message
        fi
    done
}


#创建nc服务器监听9999端口对接websocket插件8888端口并输出重定向到主循环
ncat -l -k -p $WS_LOCAL_PORT | ncat $WS_SERVER $WS_PORT |loop &

sleep 1
echo -ne "$handshake" | ncat localhost $WS_LOCAL_PORT


self=$0
nc1_pid=$!
nc2_pid=`jobs -p`

retry=$@
if [[ $retry == "" ]];then
    retry=0
fi
echo $retry

while true;do
sleep 30
echo "发送心跳Ping"
if printf "\x89\x00"| ncat localhost $WS_LOCAL_PORT;then
    echo "connection is alive"
else
    echo "connection is dead,try to reconnect"
    if [[ $retry -lt 30 ]];then
        $0 $((retry +1))
    else
        echo "max retry";
    fi
    killall -9 sleep
    kill -9 $! $$
    break
    exit 
fi
done &

