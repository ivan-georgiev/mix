from pprint import pprint as pp
import ipaddress

def addToCollectionIfValid(ips:dict, ip:str):
    try:
        ipaddress.IPv6Address(ip)
        ips[ip] = ips.get(ip, 0) + 1
        return
    except:
        pass
    
    try:
        ipaddress.IPv4Address(ip)
        ips[ip] = ips.get(ip, 0) + 1
    except:
        pass


def readLog(fname:str):

    ips = {}

    with open(fname) as f:
        for line in f:
            line = line.strip().split()
            try:
                #ip address in first or second column, no additional citeria. result in ips
                addToCollectionIfValid(ips, line[0])
                addToCollectionIfValid(ips, line[1])
            except IndexError:
                pass
            
    r = sorted(ips.items(), key=lambda x:x[1], reverse=True)
    print(r)
        

def main():
    fname = "log.txt"
    
    try:
        readLog(fname)
    except FileNotFoundError as e:
        print("Opps!" + str(e))
        
    
if __name__ == "__main__":
    main()
   