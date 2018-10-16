from decimal import *

def t4():
    prlist = []

    while (True):
        price = input("Enter price: ")

        if price.lower() == "stop":
            break

        try:
            prlist.append(Decimal(price))
        except InvalidOperation:
            print("Invalid decimal {}\n".format(price))

    prlist.sort()

    try:
        minpr = prlist.pop(0)
        maxpr = prlist.pop(-1)
    except IndexError:
        print("Not enough values")
        return

    # remove min/max price duplicates
    prlist[:] = [i for i in prlist if i != minpr and i != maxpr]

    if len(prlist) < 4:
        print("Not enough values")
        return

    print("Min price: " + str(minpr))
    print("Max price: " + str(maxpr))
    print("Avg price: " + str(sum(prlist) / len(prlist)))


t4()