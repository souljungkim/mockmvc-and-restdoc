#!/usr/bin/env sh
######################################################################################################################################################
#
#   Build Avaj
#     - Check path:./avaj-starter/build/libs/*.war after build
#
######################################################################################################################################################

####################################################################################################
#####
##### Functions
#####
####################################################################################################
runIfExists(){
  file=$1
  if [ -e "$file" ]; then
      echo ""
      echo "[RUN] $file"
      $file
  else
      echo "$file not found."
  fi
}
lsIfExists(){
  dir=$1
  if [ -d "$dir" ]; then
      echo ""
      echo "[Check Files] $dir"
      ls -al $dir
      echo ""
  else
      echo "$dir not found."
  fi
}
YELLOW='\033[1;33m'
GREEN='\033[0;32m'
CYAN='\033[0;36m'
RESET='\033[0m'
BG_RED='\033[41m'
printBigTitle(){
    echo -e "${BG_RED}##################################################${RESET}"
    echo -e "${BG_RED}##################################################${RESET}"
    echo -e "${BG_RED}#${RESET}"
    echo -e "${BG_RED}#${RESET} $1 "
    echo -e "${BG_RED}#${RESET}"
    echo -e "${BG_RED}##################################################${RESET}"
    echo -e "${BG_RED}##################################################${RESET}"
}
printTitle(){
    echo ""
    echo -e "${YELLOW}##################################################"
    echo "# $1 "
    echo -e "################################################## ${RESET}"
}
printStep(){
    echo ""
    echo "#########################"
    echo -e "# ${CYAN} $1 ${RESET}"
    cd "$2"
    echo -e "# `pwd`"
    echo "#########################"
    echo ""
}
print(){
    echo ""
    echo "#########################"
    echo -e "# ${CYAN} $1 ${RESET}"
    echo "#########################"
    echo ""
}



####################################################################################################
#####
##### Script Starts
#####
####################################################################################################
printBigTitle "Start - BUILD-WAR"
./gradlew clean bootWar "$@"

printBigTitle "Checking Artifacts"
lsIfExists "./avaj-starter/build/libs"

printBigTitle "Finish - BUILD-WAR... Good luck :)"