from os import PathLike
from typing import Any, Optional, TextIO, Union, cast, get_args

from iterjson.common import JsonFormatError, _missing, _MissingType
from iterjson.values import (ArrayValue, ConstantValue, GenericJsonValue,
                             JsonValue, ObjectValue, StringValue)

__all__ = ['JsonReader']

AnyPath = Union[str, bytes, PathLike]

_CHAR_TYPE_MAP: dict[str, type[JsonValue[Any]]] = {
    '{': ObjectValue,
    '[': ArrayValue,

    '"': StringValue,

    'n': ConstantValue,
    't': ConstantValue,
    'f': ConstantValue,
}

_WHITESPACE = ' \r\n\t'


class JsonReader:
    _fp: TextIO
    _root: Union[GenericJsonValue, _MissingType]

    def __init__(self, input: Union[TextIO, AnyPath]) -> None:
        if isinstance(input, get_args(AnyPath)):
            input = open(cast(AnyPath, input), 'rt')
        self._fp = cast(TextIO, input)
        self._root = _missing

    @property
    def root(self) -> GenericJsonValue:
        if self._root is _missing:
            self._root = self._read_value()
        return cast(GenericJsonValue, self._root)

    def __enter__(self) -> 'JsonReader':
        self._fp.__enter__()
        return self

    def __exit__(self, *args) -> Optional[bool]:
        return self._fp.__exit__(*args)

    def _read_value(self, char: Optional[str] = None) -> GenericJsonValue:
        if char is None:
            char = self._read_past_whitespace()
        type = _CHAR_TYPE_MAP.get(char)
        if type is None:
            raise JsonFormatError(char)
        return type(char, self)

    def _read_past_whitespace(self) -> str:
        while (c := self._fp.read(1)) in _WHITESPACE:
            if c == '':
                raise JsonFormatError('EOF')
        return c
