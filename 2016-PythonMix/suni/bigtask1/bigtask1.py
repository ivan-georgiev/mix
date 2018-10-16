from loaders import CSVLoader
from collectionhelper import CollectionHelper


def main():
    # custom class containing different operations over dictionaries and lists
    ch = CollectionHelper()

    # load catalog
    loader = CSVLoader('bigtask1_data/catalog.csv')
    catalog = loader.loadAsDict(keyColId=0, colList=['id', 'name', 'colors', 'type', 'sport', 'group', 'subgroup', 'sex'])
    # print(catalog)

    # load sales records
    loader = CSVLoader('bigtask1_data/sales-100K.csv')
    sales = loader.loadAsList(
            colList=['id', 'country', 'city', ['date', CSVLoader.ISO8601], ['price', CSVLoader.FLOAT]])

    # merge catalog info into sales records
    sales = ch.enrichList(sales, catalog)

    print('\n' + '-' * 5 + ' General info:')
    print('Total sales count: {0}'.format(len(sales)))
    print('Total sales amount: {0:.2f}'.format(sum(ch.colAsList(sales, 'price'))))
    print('Min date: {0}'.format(min(ch.colAsList(sales, 'date'))))
    print('Max date: {0}'.format(max(ch.colAsList(sales, 'date'))))

    print('\n' + '-' * 5 + ' Grouped by product group desc:')
    grpGroup = ch.groupSumDict(sales, 'group', 'price');
    ch.printTopFive(grpGroup)

    print('\n' + '-' * 5 + ' Grouped by city desc:')
    grpGroup = ch.groupSumDict(sales, 'city', 'price');
    ch.printTopFive(grpGroup)

    # by hour, needs extraction of the hour
    print('\n' + '-' * 5 + ' Grouped by sale hour desc:')
    hourSales = {}
    for i, elem in enumerate(sales):
        h = elem['date'].strftime("%H")
        hourSales[h] = hourSales.get(h, 0) + elem['price']
    ch.printTopFive(hourSales)


if __name__ == "__main__":
    main()
