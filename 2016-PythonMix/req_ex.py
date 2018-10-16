import requests
from pprint import pprint as pp

def validateVal(val):
    if val == 1:
        return True
    return False

r = requests.get('https://jsonplaceholder.typicode.com/posts', verify=False)
#pp(r.content.decode("utf-8"))

jo = r.json()

a = [[item['id'],item['title']] for item in jo if validateVal(item['userId']) ]

print(a)
