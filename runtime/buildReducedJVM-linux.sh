export JAVA_HOME=/home/mos/java/jdk-15.0.1+9/
export DESTINATION_FOLDER=linux/java-runtime
rm -drf $DESTINATION_FOLDER
$JAVA_HOME/bin/jlink --no-header-files --no-man-pages --compress=2 --strip-debug --add-modules java.base,java.datatransfer,java.desktop,java.instrument,java.logging,java.management,java.naming,java.sql,java.logging,java.xml,jdk.accessibility --output $DESTINATION_FOLDER
