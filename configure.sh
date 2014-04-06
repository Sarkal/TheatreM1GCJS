export CATALINA_HOME=$PWD/apache-tomcat-6.0.14
export JAVA_HOME=/usr
alias catalina="$CATALINA_HOME/bin/catalina.sh"
alias start="catalina start"
alias stop="catalina stop"
alias run="catalina run"
alias compile="cd $CATALINA_HOME/webapps/ROOT/; make; cd -"
alias restart='stop && compile && start'
