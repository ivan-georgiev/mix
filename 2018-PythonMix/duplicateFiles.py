#!/usr/bin/env python3

import os, sys
from pathlib import Path
import hashlib
from multiprocessing import Pool, cpu_count
from functools import reduce
import logging

# Generates file md5 hash
def hashFile(path: str, blocksize=65536) -> str:
    hasher = hashlib.md5()

    with open(path, 'rb') as afile:
        buf = afile.read(blocksize)
        while len(buf) > 0:
            hasher.update(buf)
            buf = afile.read(blocksize)

    return hasher.hexdigest()

# Checks if files are duplicates; returns in format: original_file: [duplicate1, duplicate2, ...]
def findDuplicates(files: list) -> dict:

    hashDict = {}
    duplicates = {}

    if len(files) > 1:
        for f in files:
            try:
                h = hashFile(str(f))

                if h in hashDict.keys():
                    duplicates.setdefault(hashDict[h], []).append(str(f))
                else:
                    hashDict[h] = str(f)
            except Exception as e:
                logging.error(e)
    return duplicates

# Groups the files with same size and generates hashes if more then one. Runs in several processes
def optimizedDuplicatedFinder(fPath: str) -> dict:

    # get all files in dict: size - [file1, file2, ...]
    filesSizeDict = {}
    for i in list(Path(fPath).rglob("*")):
        try:
            if i.is_file() and not i.is_symlink():
                filesSizeDict.setdefault(i.stat().st_size, []).append(i)
        except PermissionError as e:
            logging.error(e)
        except:
            raise

    # if no files return empty dict
    if not filesSizeDict:
        return {}

    # sort by creation time, older files is original
    for k, v in filesSizeDict.items():
        v.sort(key=lambda f: f.stat().st_ctime)

    # start generating hashes in separate processes (cpu count) for same size files
    with Pool(cpu_count()) as p:
        data = p.map(findDuplicates, [v for k, v in filesSizeDict.items()])

    # merge the dictionaries from the previous step
    return reduce((lambda x, y: {**x, **y}), data)


def main(fPath: str):

    #logger config to read from env
    logging.basicConfig(level=os.environ.get('LOGLEVEL', 'WARNING').upper())

    if not os.path.isdir(fPath):
        raise Exception("No such directory: {}".format(fPath))

    #Get the duplicates
    duplicates = optimizedDuplicatedFinder(fPath)

    #print result
    for k, v in duplicates.items():
        print("Original: {} \nDuplicates: {} \n".format(k, ', '.join(v)))


if __name__ == '__main__':
    #sys.argv = ["", r"/home"]
    if len(sys.argv) == 2:
        main(sys.argv[1])
    else:
        raise "Bad arguments. Pass single path as string"

