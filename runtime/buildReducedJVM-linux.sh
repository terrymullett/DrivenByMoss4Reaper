export JAVA_HOME=export JAVA_HOME=/home/mos/java/jdk-24+36/
export DESTINATION_FOLDER=linux/java-runtime
rm -drf $DESTINATION_FOLDER
$JAVA_HOME/bin/jlink --no-header-files --no-man-pages --strip-debug --add-modules java.desktop,java.instrument,java.management,java.naming,java.sql,java.logging,jdk.accessibility --output $DESTINATION_FOLDER
