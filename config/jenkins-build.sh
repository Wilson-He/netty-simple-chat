BUILD_ID=DONTKILLME
pid=$(ps -aux|grep simple-chat | grep -v grep| gawk '{print $2}')
if [ ${#pid} != 0 ]
    then kill -9 $pid
fi
cd /var/lib/jenkins/workspace/netty-simple-chat
mvn clean package
nohup java -jar /var/lib/jenkins/workspace/netty-simple-chat/target/simple-chat.jar --server.port=9500 -Xmx256m -Xms256m -Xss4m &
pid=$(ps -aux|grep simple-chat | grep -v grep| gawk '{print $2}')
if [ ${#pid} == 0 ]
    then
     echo "*****  BUILD FAILED  ******"
     exit 1
     else
     echo "*****  BUILD SUCCESS  *****"
fi