#!/usr/bin/env python3


from unittest import TestCase
from unittest.mock import patch
from pathlib import Path

from duplicatefiles import findDuplicates, hashFile


def mock_hashFile(path: str, blocksize=65536) -> str:
    '''
    Do not make a hash, compare filename instead for testing the duplicate logic
    :param path:
    :param blocksize:
    :return:
    '''
    return Path(path).name

class TestDuplicateFiles(TestCase):

    @patch('duplicatefiles.hashFile', side_effect=mock_hashFile)
    def test_findDuplicates(self, mock_hashFile):

        #test originalA two duplicates
        self.assertEqual(
            findDuplicates([r"/tmp/fileA", r"/tmp/new/fileA", r"/tmp/new/newb/fileA"]),
                         {r"/tmp/fileA": [r"/tmp/new/fileA", r"/tmp/new/newb/fileA"]}
                         )

        #test originalA two duplicates and originalB with one duplicate
        self.assertEqual(
            findDuplicates([r"/tmp/fileA", r"/tmp/new/fileA", r"/tmp/new/newb/fileA",
                            r"/tmp/fileB", r"/tmp/new/fileB"]),
            {r"/tmp/fileA": [r"/tmp/new/fileA", r"/tmp/new/newb/fileA"],
             r"/tmp/fileB": [r"/tmp/new/fileB"]}
        )

        #test no duplicates
        self.assertEqual(
            findDuplicates([r"/tmp/fileA", r"/tmp/new/fileB", r"/tmp/new/newb/fileC"]),
                         {}
        )

        #test empty input
        self.assertEqual(
            findDuplicates([]),
                         {}
                         )

        # test None input raises error
        self.assertRaises(TypeError, findDuplicates, None)

