#!/bin/sh

MVN_USER='alexander'

if [ "$USER" != "$MVN_USER" ]
then
	echo "=== For release you must change user to $MVN_USER ==="
    exit
fi


# release branch
BRANCH_SOURCE='master'
# maven executable path
MVN_PATH="/opt/apache-maven-3.3.9/bin/mvn8"

# check last version number and prepare next version number
DEV_VER='1.1-SNAPSHOT'
LATEST_VER=$(git describe --tags --abbrev=0)
NEXT_VER="${LATEST_VER%.*}.$((${LATEST_VER##*.}+1))"


echo "MAVEN:"
$MVN_PATH --version
echo "________________________________________________"

echo "RELEASE PARAMETERS:"
echo "  source branch:      $BRANCH_SOURCE"
echo "  release version:    $NEXT_VER"
echo "  developmentVersion: $DEV_VER"
echo ""
echo ""
echo ""
echo "Press enter to continue release process or Ctrl+C to Break"
read 


echo ""
echo ""
echo ""
echo "=================================================================================================="
echo "=== 1. Update data from GIT ======================================================================"
echo "=================================================================================================="
git remote update
if [ $? -ne 0 ] 
then
  echo "STEP 1 failed!" 	
  exit
fi
echo ""
echo "=================================================================================================="
echo "=== 2. Push branch  ============================== ==============================================="
echo "=================================================================================================="
echo "git push origin HEAD "
echo ""
git push origin HEAD
if [ $? -ne 0 ]
then
  echo "STEP 2 failed"
  exit
fi

echo ""
echo ""
echo ""
echo "=================================================================================================="
echo "=== 3. Set new version: $NEXT_VER for pom.xml files =========================================="
echo "=================================================================================================="
$MVN_PATH versions:set -DnewVersion=$NEXT_VER -DgenerateBackupPoms=false
if [ $? -ne 0 ] 
then
  echo "STEP 3 failed!" 	
  exit
fi



echo ""
echo ""
echo ""
echo "=================================================================================================="
echo "=== 4. Commit ===================================================================================="
echo "=================================================================================================="
git add .
git commit -a -m "Release process: setting RELEASE version: $NEXT_VER"
if [ $? -ne 0 ] 
then
  echo "STEP 4 failed!" 	
  exit
fi


echo ""
echo ""
echo ""
echo "=================================================================================================="
echo "=== 5. Make TAG: $NEXT_VER  ======================================================================"
echo "=================================================================================================="
git tag -a $NEXT_VER -m "Release $NEXT_VER"
if [ $? -ne 0 ] 
then
  echo "STEP 5 failed!" 	
  exit
fi


echo ""
echo ""
echo ""
echo "=================================================================================================="
echo "=== 6. Deploy new version: $NEXT_VER  ==============================================================="
echo "=================================================================================================="
$MVN_PATH deploy
if [ $? -ne 0 ] 
then
  echo "STEP 6 failed!" 	
  exit
fi


echo ""
echo ""
echo ""
echo "=================================================================================================="
echo "=== 7. Set development version: $DEV_VER for pom.xml files ======================================"
echo "=================================================================================================="
$MVN_PATH versions:set -DnewVersion=$DEV_VER -DgenerateBackupPoms=false
if [ $? -ne 0 ] 
then
  echo "STEP 7 failed!" 	
  exit
fi


echo ""
echo ""
echo ""
echo "=================================================================================================="
echo "=== 8. Commit ===================================================================================="
echo "=================================================================================================="
git add .
git commit -a -m "Release process: setting SNAPSHOT version: $DEV_VER"
if [ $? -ne 0 ] 
then
  echo "STEP 8 failed!" 	
  exit
fi


echo ""
echo ""
echo ""
echo "=================================================================================================="
echo "=== 9. Push ======================================================================================"
echo "=================================================================================================="
git push origin HEAD
git push origin --tags
if [ $? -ne 0 ] 
then
  echo "STEP 9 failed!" 	
  exit
fi


echo ""
echo ""
echo "=================================================================================================="
echo "        Release successfuly completed"
echo "	released version:    $NEXT_VER"
echo "  development version: $DEV_VER"
echo "=================================================================================================="





