#!/bin/sh

setup_git() {
  git config --global user.email "zhangchi8518@gmail.com"
  git config --global user.name "pow25"
}

commit_website_files() {
  git checkout -b test_files
  git add ./target/surefire-reports/com.vplus.maven.vplus.AppTest.txt
  git add ./target/surefire-reports/TEST-com.vplus.maven.vplus.AppTest.xml
  git commit --message "Travis uploading files"
}

upload_files() {
  git remote add origin-pages https://${GITHUB}@github.com/pow25/vegilplus.git > /dev/null 2>&1
  git push --quiet --set-upstream origin-pages gh-pages 
}

setup_git
commit_website_files
upload_files