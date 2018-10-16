import subprocess
import json
from pprint import pprint as pp

class FSItem:
    def __init__(self, ftype:str, perm:str,  fitem:str, owner:str = None, group:str = None, size:int = None, dt:str = None, tm:str = None):
        
        if ftype == '-':
            self.ftype = "f"
        else:
            self.ftype = ftype
        self.perm = perm
        self.fitem = fitem
        
    def __str__(self):
        return "{}".format(self.fitem)

def jdefault(o):
    if isinstance(o, set):
        return list(o)
    return o.__dict__
    
def main():
    
    fsitems = []

    r = subprocess.getoutput("ls -ld --time-style='+%Y-%m-%d %H:%M' `find {}`".format("/etc"))
    
    for line in r.split("\n"):
        line = line.strip().split()
        
        try:
            item = FSItem(ftype = line[0][0], perm = line[0][1:], fitem = line[7])
            fsitems.append(item)
        except:
            pass
        
    jitems = json.dumps(fsitems, default=jdefault)
    fsitemr = json.loads(jitems)
    
    b = [x for x in fsitemr if x["ftype"] == "d" and "/xml/" in x["fitem"]]
    pp(b)
    
    
    #python 3.5
    #r = subprocess.run(["ls", "-l", "/tmp"], stdout=subprocess.PIPE, check=True)
    #print(r.stdout.decode("utf-8"))    


if __name__ == "__main__":
    main()
   