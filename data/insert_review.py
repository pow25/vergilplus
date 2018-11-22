import csv

with open("sentiments.csv", "r") as csvfile:
    reader = csv.DictReader(csvfile, delimiter=",", quotechar='"')
    for i in reader:
        print(i)
