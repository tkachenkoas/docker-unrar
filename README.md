# docker-unrar

Super primitive demo app build using Spring Boot to unrar rar archive using web interface (inculding password-protected). 
it unpacks provided archive and downloads first file from it (I guess, ordered by name).

On windows: winrar must be installed. Uses ${HOME}/temp folder for temporary files. After 

In docker: unrar dependency included.

Default profile is windows.

In order to build dockerfile, run "mvn clean package -Pdocker" command
