#!/bin/sh

RELEASE_USER='alexander'

if [ "$USER" != "$RELEASE_USER" ]
then
	echo "=== For release you must change user to $RELEASE_USER ==="
    exit
fi


# release branch
BRANCH_SOURCE='master'
# maven executable path
MVN_PATH="/opt/apache-maven-3.3.9/bin/mvn8"
# path for GITHUB maven repository
MVN_REPO_PATH="/home/alexander/projects/github/mvnrepo"

# check last version number and prepare next version number
LATEST_VER=$(git describe --tags --abbrev=0)
NEXT_VER="${LATEST_VER%.*}.$((${LATEST_VER##*.}+1))"
BRANCH_VER="release-$NEXT_VER"

echo "MAVEN:"
$MVN_PATH --version
echo "________________________________________________"

echo "RELEASE PARAMETERS:"
echo "  source branch:      $BRANCH_SOURCE"
echo "  release version:    $NEXT_VER"
echo "  release branch:     $BRANCH_VER"
echo ""
echo ""
echo "Press enter to continue release process or Ctrl+C to Break"
read 

echo ""
echo ""
echo ""
STEP=1
echo "=================================================================================================="
echo "=== $STEP. Make TAG: $NEXT_VER  ======================================================================"
echo "=================================================================================================="
git tag -a $NEXT_VER -m "Release $NEXT_VER"
if [ $? -ne 0 ] 
then
  echo "STEP $STEP failed!" 	
  exit
fi


echo ""
echo ""
echo ""
STEP=2
echo "=================================================================================================="
echo "=== $STEP. Push brach and tags==================================================================="
echo "=================================================================================================="
git push origin HEAD
git push origin --tags
if [ $? -ne 0 ] 
then
  echo "STEP $STEP failed!" 	
  exit
fi


echo ""
echo ""
echo ""
STEP=3
echo "=================================================================================================="
echo "=== $STEP. Go to the relase branch $BRANCH_VER ================================================================"
echo "=================================================================================================="
git checkout -b $BRANCH_VER
if [ $? -ne 0 ] 
then
  echo "STEP $STEP failed!" 	
  exit
fi


echo ""
echo ""
echo ""
STEP=4
echo "=================================================================================================="
echo "=== $STEP. Go to the tag $NEXT_VER ================================================================"
echo "=================================================================================================="
git checkout $NEXT_VER
if [ $? -ne 0 ] 
then
  echo "STEP $STEP failed!" 	
  exit
fi


echo ""
echo ""
echo ""
STEP=5
echo "=================================================================================================="
echo "=== $STEP. Set new version: $NEXT_VER for pom.xml files =========================================="
echo "=================================================================================================="
$MVN_PATH versions:set -DnewVersion=$NEXT_VER -DgenerateBackupPoms=false
if [ $? -ne 0 ] 
then
  echo "STEP $STEP failed!" 	
  exit
fi

echo ""
echo ""
echo ""
STEP=6
echo "=================================================================================================="
echo "=== $STEP. build whole project =========================================================================="
echo "=================================================================================================="
$MVN_PATH clean install
if [ $? -ne 0 ] 
then
  echo "STEP $STEP failed!"
  exit
fi


echo ""
echo ""
echo ""
STEP=7
echo "=================================================================================================="
echo "=== $STEP. build version for MAVEN REPO ============================================================="
echo "=================================================================================================="
$MVN_PATH  install:install-file -DgroupId=ru.absoft.util -DartifactId=cute-config -Dversion=$NEXT_VER -Dfile=target/cute-config.jar -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=$MVN_REPO_PATH  -DcreateChecksum=true
if [ $? -ne 0 ] 
then
  echo "STEP $STEP failed!"
  exit
fi


echo ""
echo ""
echo ""
STEP=8
echo "=================================================================================================="
echo "=== $STEP. Checkout $BRANCH_SOURCE ============================================================================"
echo "=================================================================================================="
git checkout $BRANCH_SOURCE
if [ $? -ne 0 ] 
then
  echo "STEP $STEP failed!" 	
  exit
fi


echo ""
echo ""
echo ""
STEP=9
echo "=================================================================================================="
echo "=== $STEP. Remove release brunch $BRANCH_VER ======================================================================"
echo "=================================================================================================="
git branch -d $BRANCH_VER
if [ $? -ne 0 ] 
then
  echo "STEP $STEP failed!" 	
  exit
fi


echo ""
echo ""
echo ""
STEP=10
echo "=================================================================================================="
echo "=== $STEP. Push new versions in REPO           ======================================================"
echo "=================================================================================================="
CUR_PATH=`pwd`
cd $MVN_REPO_PATH

git add .

if [ $? -ne 0 ] 
then
  echo "STEP $STEP.1 failed!"
  exit
fi

git commit -a -m "CUTE-CONFIG new version $RELEASE_VERSION"

if [ $? -ne 0 ] 
then
  echo "STEP $STEP.2 failed!"
  exit
fi

git push origin HEAD

if [ $? -ne 0 ] 
then
  echo "STEP $STEP.3 failed!"
  exit
fi

# go to the current path
cd $CUR_PATH

echo ""
echo ""
echo "=================================================================================================="
echo "        Release successfuly completed"
echo "	released version:        $NEXT_VER"
echo "=================================================================================================="
