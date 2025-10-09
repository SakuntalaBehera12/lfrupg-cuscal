#Shared properties files location

With Liferay DXP 7.x, WAB Spring portlets cannot share loader from Tomcat `shared.loader`. This module is to wrap shared properties files to be used by Cuscal portlets, especially files for Spring beans. Shared properties files, which used to be under folder `LR_HOME/property-files/`, will be collected to this module.

NOTE: Must have corresponding child folders mapped to configs environment.