import json
import os

test = os.listdir('New Folder/')

#print(test)

for stuff in test:

    #print(stuff)
    f= open('New Folder/'+stuff)

    data = json.load(f)

    print(data['heat_capacity'])


    f.close