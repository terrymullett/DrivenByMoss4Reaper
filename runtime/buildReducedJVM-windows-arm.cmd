set DESTINATION_FOLDER=windows-arm\java-runtime

rmdir /Q /S "%DESTINATION_FOLDER%"
"%JAVA_HOME25%/bin/jlink" --no-header-files --no-man-pages --strip-debug --add-modules java.desktop,java.instrument,java.management,java.naming,java.sql,java.logging,jdk.accessibility --output %DESTINATION_FOLDER%
