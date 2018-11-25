import numpy

# file1 = "courses.txt"
# file2 = "track_courses.txt"
file1 = "a.txt"
list1 = []
# list2 = set()

with open(file1) as f:
    line = f.readline()
    while line:
        
        if line in list1:
            print(line)
        
        list1.append(line)

        line = f.readline()

# with open(file2) as f:
#     line = f.readline()
#     while line:
#         temp_line = line.rstrip('\n')
#         list2.add(temp_line)
#         line = f.readline()

# diff = []

# for i in list1:
#     if i not in list2:
#         diff.append(i)

# print(diff)
        