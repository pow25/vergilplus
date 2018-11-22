import csv
import pymysql

def change_quote(string):
    res = ""
    for i in string:
        if i == "'":
            res += "\\"
        res += i 
    return res

cnx = pymysql.connect("localhost", "dbuser", "dbuser", "vergilplus", charset='utf8mb4', cursorclass=pymysql.cursors.DictCursor)
cursor = cnx.cursor()
q = "CREATE TABLE `vergilplus`.`sentiment` (\
  `number` VARCHAR(64) NOT NULL,\
  `professor` TEXT NOT NULL,\
  `review` TEXT NOT NULL,\
  `score` TEXT NOT NULL,\
  `magnitude` TEXT NOT NULL);"
cursor.execute(q)
cnx.commit()

with open("sentiments.csv", "r",encoding='UTF-8') as csvfile:
    reader = csv.DictReader(csvfile, delimiter=",")
    for i in reader:
        q = "INSERT INTO `vergilplus`.`sentiment` ( number, professor, review, score,magnitude ) VALUES ('"
        q += i['number'] + "','"
        q += i['professor'] + "','"
        q += change_quote(i['review']) + "','"
        q += i['score'] + "','"
        q += i['magnitude'] + "');"
        cursor.execute(q)
        cnx.commit()
