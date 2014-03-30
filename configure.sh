export CATALINA_HOME=/home/$USER/TheatreM1GCJS/apache-tomcat-6.0.14
export JAVA_HOME=/usr
alias start="$CATALINA_HOME/bin/catalina.sh start"
alias stop="$CATALINA_HOME/bin/catalina.sh stop"
alias compile="$CATALINA_HOME/webapps/ROOT/compile.sh"
alias restart="stop; start"
