import xml.etree.ElementTree as ET
from pprint import pprint as pp

data = '''<?xml version="1.0"?>
<data>
    <country name="Liechtenstein">
        <rank>1</rank>
        <year>2008</year>
        <gdppc>141100</gdppc>
        <neighbor name="Austria" direction="E"/>
        <neighbor name="Switzerland" direction="W"/>
        <test val="tt">
            <loc name="koza" >BG</loc>
            <age>10</age>
        </test>
    </country>
    <country name="Singapore">
        <rank>4</rank>
        <year>2011</year>
        <gdppc>59900</gdppc>
        <neighbor name="Malaysia" direction="N"/>
    </country>
    <country name="Panama">
        <rank>68</rank>
        <year>2011</year>
        <gdppc>13600</gdppc>
        <neighbor name="Costa Rica" direction="W"/>
        <neighbor name="Colombia" direction="E"/>
    </country>
</data>
'''



#tree = ET.parse('country_data.xml')
#root = tree.getroot()

root = ET.fromstring(data)

for i in root.iter('loc'):
    print(i.tag, i.attrib, i.text)

for i in root.findall("./country/test/loc"):
    print(i.tag, i.attrib, i.text)

for i in root.findall(".//loc[@name='koza']"):
    print(i.tag, i.attrib, i.text)

for i in root.findall(".//test/[age='10']/loc"):
    print(i.tag, i.attrib, i.text)