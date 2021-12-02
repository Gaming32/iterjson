from pprint import pprint

from iterjson.reader import JsonReader
from iterjson.values import GenericJsonValue, ObjectValue

# with JsonReader('test.json') as reader:
#     pprint(reader.root.read())

infos: list[GenericJsonValue] = []
with JsonReader('test.json') as reader:
    assert isinstance(reader.root, ObjectValue)
    for (letter, info) in reader.root:
        infos.append(info)

for info in infos:
    try:
        print(info.value)
    except ValueError as e:
        print(e)
