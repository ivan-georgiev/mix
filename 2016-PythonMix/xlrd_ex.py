#!/usr/bin/env python 

import sys
import xlrd
# from xlrd.sheet import ctype_text


__author__ = "ivan"
__date__ = "$Jul 28, 2015 21:02:10$"


class DocumentReader:
    # Global objects
    mergedCells = {}
    wBook = None
    sheet = None

    def __init__(self, workbookName, sheetNameIndex=0, headingsTuple=None, headingRow=None):

        if not ((headingsTuple is None and headingRow is None) or all([headingsTuple, headingRow])):
            raise Exception("If headings are provided headings row is also expected")

        self.wBook = xlrd.open_workbook(workbookName)

        try:
            if isinstance(sheetNameIndex, str):
                self.sheet = self.wBook.sheet_by_name(sheetNameIndex)
            elif isinstance(sheetNameIndex, int):
                self.sheet = self.wBook.sheet_by_index(sheetNameIndex)
            else:
                raise Exception("Sheet must be specified by index or name: " + sheetNameIndex)
        except:
            print('Available sheets: ', self.wBook.sheet_names())
            raise

            # Fill the dictionary with merged cells values
        for crange in self.sheet.merged_cells:
            rlo, rhi, clo, chi = crange
            for rowx in range(rlo, rhi):
                for colx in range(clo, chi):
                    self.mergedCells[str(rowx) + "-" + str(colx)] = '%s' % self.sheet.cell(rlo, clo).value

        if headingsTuple is not None and isinstance(headingsTuple, tuple):
            if self.sheet.row(headingRow) != headingsTuple:
                raise Exception("Cannot validate sheet headings. Expected: " + [print(i) for i in headingsTuple] + " and found: " + [print(i) for i in self.sheet.row(headingRow)])



    # Smart take value method
    def takeValue(self, row_idx, col_idx):
        if str(row_idx) + "-" + str(col_idx) in self.mergedCells.keys():
            return ('%s' % (self.mergedCells[str(row_idx) + "-" + str(col_idx)]))
        return str(self.sheet.cell(row_idx, col_idx).value).rstrip()



        # EXEC Section


sys.argv = [__name__, "test.xlsx"]
# print (sys.argv)

print("\n=== Excel parser ===\n")

excelFile = None

for arg in sys.argv[1:]:

    ext = arg.split('.')[-1]

    if ext == 'xlsx':
        excelFile = arg
        print("XLSX file: " + excelFile)
        continue

    raise Exception("Invalid parameter: " + arg)

if not (excelFile):
    raise Exception("Pass <file>.xlsx as argument.")

run = DocumentReader(excelFile)

for row_idx in range(1, run.sheet.nrows):  # Iterate through rows 

    col_idx = 2
    val = run.takeValue(row_idx, col_idx).strip(' ')
    print(val)

sys.exit(0)

