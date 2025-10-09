@echo off

echo =========================================================
echo Install Project's Liferay Bundle (Tomcat).
echo =========================================================
set "BACKUP_FOLDER=D:\Backup\Liferay"
set "LIFERAY_FOLDER_NAME=Liferay"
set "LIFERAY_FOLDER=D:\App\%LIFERAY_FOLDER_NAME%"
set "BUNDLE_FOLDER=D:\Upload"
set "LIFERAY_FOLDER_DIR=%LIFERAY_FOLDER:\=\\%"
sc query state=all | findstr Liferay
if errorlevel 1 (
  echo Liferay service has not installed yet, exit
  goto:end
)
:stop_liferay
 echo stop Liferay service
 net stop Liferay
:loop
	sc query Liferay | find "STOPPED"
	if errorlevel 1 (
	  ping -n 10 127.0.0.1 >NUL
	  echo waiting for Liferay stop
	  goto loop
	)
:close_process_using_liferay_folder
	setlocal enableDelayedExpansion
	for /f "usebackq tokens=* delims=" %%a in (`
		wmic process  where 'CommandLine like "%%!LIFERAY_FOLDER_DIR!%%" and not CommandLine like "%%RuntimeBroker%%"'   get CommandLine^,ProcessId  /format:value
	`) do (
		for /f "tokens=* delims=" %%# in ("%%a")  do (
			if "%%#" neq "" (
				echo %%#
				set "%%#"
			)
		)
	)
	taskkill /pid %ProcessID% /f
:find_bundle_zip
    D:
    cd "%BUNDLE_FOLDER%"
    if not exist temp\ mkdir temp
	for /f "delims=" %%a in ('dir /s /b CUSCAL*BUNDLE*.zip') do (
	 set "bundle_path=%%a"
     set bundle_name=%%~nxa
    )
    echo "Bundle name is %bundle_name%"
:backup_previos_data
    rmdir %BACKUP_FOLDER% /S /Q
	ping -n 60 127.0.0.1 >NUL
    mkdir %BACKUP_FOLDER%
    cd %LIFERAY_FOLDER%
    xcopy "%LIFERAY_FOLDER%\data" "%BACKUP_FOLDER%\data" /S /H /E /Q /C /F /I /D
    xcopy "%LIFERAY_FOLDER%\osgi\configs" "%BACKUP_FOLDER%\osgi\configs" /S /H /E /Q /C /F /I /D
    dir "%BACKUP_FOLDER%"
    cd %BUNDLE_FOLDER%
    rmdir "%LIFERAY_FOLDER%_bak" /S /Q
    ping -n 60 127.0.0.1 >NUL
	xcopy "%LIFERAY_FOLDER%" "%LIFERAY_FOLDER%_bak" /S /H /E /Q /C /F /I /D
    echo Finished backing up data from previous build
:delete_liferay_modules
    rmdir "%LIFERAY_FOLDER%\osgi\configs" /S /Q
	rmdir "%LIFERAY_FOLDER%\deploy" /S /Q
	rmdir "%LIFERAY_FOLDER%\property-files" /S /Q
	echo Y|del "%LIFERAY_FOLDER%\osgi\modules\*.*"
	echo Y|del "%LIFERAY_FOLDER%\osgi\war\*.*"
	del "%LIFERAY_FOLDER%\*.properties"
	ping -n 60 127.0.0.1 >NUL
:extract_new_bundle
	echo Extract bunlde to temporary folder
	tar -xf  %bundle_path% -C "%BUNDLE_FOLDER%\temp"
	echo Finished extracting bundle
:move_new_bundle
	echo Moving new bundle
	if not exist "%LIFERAY_FOLDER%\" mkdir "%LIFERAY_FOLDER%"
	echo a|xcopy "%BUNDLE_FOLDER%\temp\deploy" "%LIFERAY_FOLDER%\deploy" /H /E /Q /C /F /I /S /Y /D
    echo a|xcopy "%BUNDLE_FOLDER%\temp\osgi\modules" "%LIFERAY_FOLDER%\osgi\modules" /H /E /Q /C /F /I /S /Y /D
	echo a|xcopy "%BUNDLE_FOLDER%\temp\osgi\war" "%LIFERAY_FOLDER%\osgi\war" /H /E /Q /C /F /I /S /Y /D
	echo a|xcopy "%BUNDLE_FOLDER%\temp\osgi\configs" "%LIFERAY_FOLDER%\osgi\configs" /H /E /Q /C /F /I /S /Y /D
	echo a|xcopy "%BUNDLE_FOLDER%\temp\tomcat-9.0.43\bin" "%LIFERAY_FOLDER%\tomcat-9.0.43\bin" /H /E /Q /C /F /I /S /Y /D
	echo a|xcopy "%BUNDLE_FOLDER%\temp\tomcat-9.0.43\conf" "%LIFERAY_FOLDER%\tomcat-9.0.43\conf" /H /E /Q /C /F /I /S /Y /D
	rmdir "%BUNDLE_FOLDER%\temp" /S /Q
	echo Finished moving new bundle
:copy_env_config
   echo Copying configs
   echo a|xcopy "%BUNDLE_FOLDER%\configs" "%LIFERAY_FOLDER%" /H /E /Q /C /F /I /S /Y /D
   echo Finished copying configs
:restore_previos_data
    echo Restoring data from backup
    echo a|xcopy "%BACKUP_FOLDER%\data" "%LIFERAY_FOLDER%\data" /H /E /Q /C /F /I /D /ZB
:clean_bundle
	cd %LIFERAY_FOLDER%
	for /f "delims=" %%a in ('dir /s /b /AD tomcat-*') do set "tomcat_path=%%a"
	echo "Tomcat path is %tomcat_path%"
	cd /
:start_liferay
   echo start Liferay service
   net start Liferay
:end
   echo Finished installing Liferay instance
goto:eof