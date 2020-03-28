#!/bin/sh

MVN_USER="alexander"

if [ "$USER" != "$MVN_USER" ]
then
	echo "=== For release you must change user to $MVN_USER ==="
    exit
fi

LATEST_TAG=$(git describe --tags)

echo "latest tag: $LATEST_TAG"
exit 1


# path for GITHUB maven repository
MVN_REPO_PATH="/home/alexander/projects/github/mvnrepo"

# maven executable path
MVN_PATH="/opt/apache-maven-3.3.9/bin/mvn8"

# ===================================================================================
# ===================================================================================
# Main branch =======================================================================
BRANCH_FOR_RELEASE='v1.0-release'
BRANCH_SOURCE='master'
# ===================================================================================
# ===================================================================================
# ===================================================================================

if [[ "$1" = "--help" ]]
then
echo "Usage: release.sh releaseVersion developmentVersion"
echo "Example: ./release.sh 1.0.1 1.0.2-SNAPSHOT"
echo "By default version numbers generated automatically"
exit
fi

# change working directory
#cd ~/projects/github/java-utils/cute-config/


if [[ "$1" = "" || "$2" = "" ]]
then
RELEASE_VERSION=`cat ./pom.xml | grep "<version>.*</version>" -m1`
RELEASE_VERSION=${RELEASE_VERSION/-SNAPSHOT<\/version>/}
RELEASE_VERSION=${RELEASE_VERSION/*<version>/}
NEW_VERSION_DEV=${RELEASE_VERSION:0:${#RELEASE_VERSION}-1}$((${RELEASE_VERSION:${#RELEASE_VERSION}-1:1}+1))
NEW_VERSION_DEV="$NEW_VERSION_DEV-SNAPSHOT"
else 
RELEASE_VERSION=$1
NEW_VERSION_DEV=$2
fi


TAG_NAME="CUTE-CONFIG-$RELEASE_VERSION"

echo "MAVEN:"
$MVN_PATH --version
echo "________________________________________________"

echo "RELEASE PARAMETERS:"
echo "  source branch:      $BRANCH_SOURCE"
echo "  release branch:     $BRANCH_FOR_RELEASE"
echo "  releaseVersion:     $RELEASE_VERSION"
echo "  developmentVersion: $NEW_VERSION_DEV"
echo ""
echo ""
echo ""
echo "Press enter to continue release process or Ctrl+C to Break"
read 




echo ""
echo ""
echo ""
echo "=================================================================================================="
echo "=== 1. Pull data from GIT ========================================================================"
echo "=================================================================================================="
git pull --rebase origin $BRANCH_FOR_RELEASE
if [ $? -ne 0 ] 
then
  echo "STEP 1 failed!" 	
  exit
fi


echo ""
echo ""
echo ""
echo "=================================================================================================="
echo "=== 2. Update data from GIT ======================================================================"
echo "=================================================================================================="
git remote update
if [ $? -ne 0 ] 
then
  echo "STEP 2 failed!" 	
  exit
fi
echo ""
echo "=================================================================================================="
echo "=== 3. Merge from delopment branch      =================================================="
echo "=================================================================================================="
echo "git merge remotes/origin/$BRANCH_SOURCE"
echo ""
git merge remotes/origin/$BRANCH_SOURCE
if [ $? -ne 0 ]
then
  echo "STEP 3 failed"
  exit
fi
echo ""
echo ""
echo "=================================================================================================="
echo "=== 4. Push merge results to remote repo   ==============================================="
echo "=================================================================================================="
echo "git push origin HEAD "
echo ""
git push origin HEAD
if [ $? -ne 0 ]
then
  echo "STEP 4 failed"
  exit
fi

echo ""
echo ""
echo ""
echo "=================================================================================================="
echo "=== 5. Set new version: $RELEASE_VERSION for pom.xml files ======================================="
echo "=================================================================================================="
$MVN_PATH versions:set -DnewVersion=$RELEASE_VERSION -DgenerateBackupPoms=false
if [ $? -ne 0 ] 
then
  echo "STEP 5 failed!" 	
  exit
fi



echo ""
echo ""
echo ""
echo "=================================================================================================="
echo "=== 6. Commit ===================================================================================="
echo "=================================================================================================="
git add .
git commit -a -m "Release process: setting RELEASE version: $RELEASE_VERSION"
if [ $? -ne 0 ] 
then
  echo "STEP 6 failed!" 	
  exit
fi


echo ""
echo ""
echo ""
echo "=================================================================================================="
echo "=== 7. Make TAG: $TAG_NAME  ======================================================================"
echo "=================================================================================================="
git tag -a $TAG_NAME -m "Release $RELEASE_VERSION"
if [ $? -ne 0 ] 
then
  echo "STEP 7 failed!" 	
  exit
fi



echo ""
echo ""
echo ""
echo "=================================================================================================="
echo "=== 8. Set new development version: $NEW_VERSION_DEV for pom.xml files ==========================="
echo "=================================================================================================="
$MVN_PATH versions:set -DnewVersion=$NEW_VERSION_DEV -DgenerateBackupPoms=false
if [ $? -ne 0 ] 
then
  echo "STEP 8 failed!" 	
  exit
fi



echo ""
echo ""
echo ""
echo "=================================================================================================="
echo "=== 9. Commit ===================================================================================="
echo "=================================================================================================="
git add .
git commit -a -m "Release process: setting SNAPSHOT version: $NEW_VERSION_DEV"
if [ $? -ne 0 ] 
then
  echo "STEP 9 failed!" 	
  exit
fi



echo ""
echo ""
echo ""
echo "=================================================================================================="
echo "=== 10. Push ======================================================================================"
echo "=================================================================================================="
git push origin HEAD
git push origin $TAG_NAME
if [ $? -ne 0 ] 
then
  echo "STEP 10 failed!" 	
  exit
fi



echo ""
echo ""
echo ""
echo "=================================================================================================="
echo "=== 11. Go to TAG for further assembling =========================================================="
echo "=================================================================================================="
git checkout $TAG_NAME
if [ $? -ne 0 ] 
then
  echo "STEP 11 failed!" 	
  exit
fi



echo ""
echo ""
echo ""
echo "=================================================================================================="
echo "=== 12. build whole project =========================================================================="
echo "=================================================================================================="
$MVN_PATH clean install
if [ $? -ne 0 ] 
then
  echo "STEP 12 failed!"
  exit
fi


echo ""
echo ""
echo ""
echo "=================================================================================================="
echo "=== 13. build version for MAVEN REPO ============================================================="
echo "=================================================================================================="
$MVN_PATH  install:install-file -DgroupId=ru.absoft.util -DartifactId=cute-config -Dversion=$RELEASE_VERSION -Dfile=target/cute-config.jar -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=$MVN_REPO_PATH  -DcreateChecksum=true
if [ $? -ne 0 ] 
then
  echo "STEP 13 failed!"
  exit
fi


echo ""
echo ""
echo ""
echo "=================================================================================================="
echo "=== 14. Go to the main branch: $BRANCH_FOR_RELEASE ======================================================"
echo "=================================================================================================="
git checkout $BRANCH_FOR_RELEASE
if [ $? -ne 0 ] 
then
  echo "STEP 14 failed!"
  exit
fi


echo ""
echo ""
echo ""
echo "=================================================================================================="
echo "=== 15. Push new versions in REPO           ======================================================"
echo "=================================================================================================="
CUR_PATH=`pwd`
cd $MVN_REPO_PATH

git add .

if [ $? -ne 0 ] 
then
  echo "STEP 15.1 failed!"
  exit
fi

git commit -a -m "CUTE-CONFIG new version $RELEASE_VERSION"

if [ $? -ne 0 ] 
then
  echo "STEP 15.2 failed!"
  exit
fi

git push origin HEAD

if [ $? -ne 0 ] 
then
  echo "STEP 15.3 failed!"
  exit
fi

# go to the current path
cd $CUR_PATH

echo ""
echo ""
echo "=================================================================================================="
echo "        Release successfuly completed"
echo "	released version:        $RELEASE_VERSION"
echo "  new development version: $NEW_VERSION_DEV"
echo "=================================================================================================="





