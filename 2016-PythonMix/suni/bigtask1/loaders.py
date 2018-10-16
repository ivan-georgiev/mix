import json, yaml
import os
import re
import logging
import iso8601
from datetime import timezone


class Loader:
    def __init__(self, filename):
        # base validations
        if os.access(filename, os.R_OK) and os.path.isfile(filename):
            self.filename = filename
            self._logger = logging.getLogger(__name__)
            self._logger.setLevel(logging.WARN)
        else:
            raise ValueError("Inaccessible file '{}'".format(filename))

    def load(self):
        raise NotImplementedError()


class JSONLoader(Loader):
    def load(self):
        with open(self.filename) as f:
            input_data = json.load(f)
            return input_data


class YAMLLoader(Loader):
    def load(self):
        with open(self.filename) as f:
            input_data = yaml.load(f)
            return input_data


class CSVLoader(Loader):
    INT = 'int'
    FLOAT = 'float'
    ISO8601 = 'iso8601'
    STR = 'str'

    def loadAsDict(self, keyColId: int, colList: list) -> {}:
        with open(self.filename) as f:

            catalog = {}
            bad_lines = []

            for line in f:
                line = line.replace(',,', ', ,').rstrip(
                        '\n')  # ,, won't be caught as empty string by the regex, so whitespace must be added
                la = re.findall(r'(?:[^,"]|"[^"]*")+', line)
                catalog[self.caster(la[keyColId])] = {}
                try:
                    for ind, col in enumerate(colList):
                        if ind == keyColId:
                            continue
                        if isinstance(col, list):
                            catalog[self.caster(la[keyColId])].update({col[0]: self.caster(la[ind], col[1])})
                        else:
                            catalog[self.caster(la[keyColId])].update({col: self.caster(la[ind])})

                except:
                    self._logger.warning(line + ' -> ' + str(la))
                    bad_lines.append(line)
            # print('Bad lines number: ' + str(len(bad_lines)))

            return catalog

    def loadAsList(self, colList: list) -> []:
        with open(self.filename) as f:

            catalog = []
            bad_lines = []

            for line in f:
                line = line.replace(',,', ', ,').rstrip(
                        '\n')  # ,, won't be caught as empty string by the regex, so whitespace must be added
                la = re.findall(r'(?:[^,"]|"[^"]*")+', line)
                cur = {}
                try:
                    for ind, col in enumerate(colList):
                        if isinstance(col, list):
                            cur.update({col[0]: self.caster(la[ind], col[1])})
                        else:
                            cur.update({col: self.caster(la[ind])})
                except:
                    self._logger.warning(line + ' -> ' + str(la))
                    bad_lines.add(line)
                catalog.append(cur)
            # print('Bad lines number: ' + str(len(bad_lines)))

            return catalog

    def caster(self, var, type=STR):

        var = re.sub('(^"|"$)', '', var)

        if type == CSVLoader.INT:
            return int(var)
        elif type == CSVLoader.FLOAT:
            return float(var)
        elif type == CSVLoader.ISO8601:
            return iso8601.parse_date(var).astimezone(timezone.utc)
        else:
            return var #STR or as it is
