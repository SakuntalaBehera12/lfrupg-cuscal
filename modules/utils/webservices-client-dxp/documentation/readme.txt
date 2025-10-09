This readme details how to extend the client for new webservices

1. Update the pom.xml to include any new wsdl references (wsimport goal)
2. run maven to generate the client code (mvn generate-sources)
3. edit WebServicePooling class if new wsdl:
		a. add new key for the new wsdl end points, eg WEBSERVICE_POOL_PCT
		b. add new serviceUrl property with getters/setters, eg pctServicesUrl
		c. create a new factory method (use existing one as a template), eg newPctService()
		d. update ServicePoolFactory.makeObject for the new key in a) and factory method in c)  
4. Create a new interface extending CuscalWebServices with the new methods
5. Create a new class extending AbstractCuscalWebServices and implementing the interface in 4)
	CardServiceImpl can be used as a template.

*******************************	
* This next step is important *
*******************************
6. Increment the version number in the pom.xml 

7. Build & test.

8. When the changes are ready to be shared via nexus run "mvn deploy". 
	
	** Step 6 is important to ensure we do not overwrite previous versions **
	
9. Update any dependent projects to use the version defined in step 6.
