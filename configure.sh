export CATALINA_HOME=$PWD/apache-tomcat-6.0.14
export JAVA_HOME=/usr
alias start="$CATALINA_HOME/bin/catalina.sh start"
alias stop="$CATALINA_HOME/bin/catalina.sh stop"
alias compile="cd $CATALINA_HOME/webapps/ROOT/ && ./compile.sh && cd -"
alias restart='stop && compile && start'
