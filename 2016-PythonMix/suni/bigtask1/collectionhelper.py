class CollectionHelper:

    #All methods could be static but are not defiend like that
    #Instance of the class must be created

    def enrichList(self, lst: list, ref: dict) -> list:
        for i, elem in enumerate(lst):
            elem.update(ref[elem['id']])
        return lst


    def colAsList(self, inp, colName: str) -> float:
        res = []
        for i, elem in enumerate(inp):
            res.append(elem[colName])
        return res


    def groupSumDict(self, inp: dict, grpCol: str, valCol: str) -> float:
        res = {}
        for i, elem in enumerate(inp):
            res[elem[grpCol]] = res.get(elem[grpCol], 0) + float(elem[valCol])
        return res


    def getMaxValueKey(self, dictItem: {}) -> list:
        v = list(dictItem.values())
        k = list(dictItem.keys())
        return [k[v.index(max(v))], max(v)]


    def printTopFive(self,grpGroup):
        grpSize = 10000
        starLimit = 20

        for _ in range(5):
            r = self.getMaxValueKey(grpGroup)

            s = (int)(r[1] / grpSize)
            if s > starLimit:
                s = starLimit

            print('{0}: {1} {2:.2f}$'.format(r[0], s * '*', r[1]))
            del (grpGroup[r[0]])
