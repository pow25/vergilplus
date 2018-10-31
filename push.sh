#!/bin/sh

mv -f ./target/surefire-reports/com.vplus.maven.vplus.AppTest.txt ./report
mv -f ./target/surefire-reports/TEST-com.vplus.maven.vplus.AppTest.xml ./report
setup_git() {
  git config --global user.email "zhangchi8518@gmail.com"
  git config --global user.name "pow25"
}

commit_website_files() {
  git checkout -b master
  git add *
  git commit --message "Travis uploading files [ci skip]"
}

upload_files() {
  git remote add origin-pages https://$GITHUB@github.com/pow25/vergilplus.git > /dev/null 2>&1
  git push --quiet --set-upstream origin-pages master
}

setup_git
commit_website_files
upload_files