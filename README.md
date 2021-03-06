# Readable, reloadable and overridable configuration management implementation for working with ordinal or specified format configuration data

For more information please see https://github.com/ababin/cute-config/wiki

Configuration file can look like this:

```
prop1=val1
prop2=val2
prop3=val3

[some.prop.propList]
# section 1 with just numbers
1,2,3,4,5,6,7,8

# section 2 with words
word1 , word2, word3, word4, some phrase

# section 3 with phrases
some phrase 1, some phrase 2, some phrases 3


{some.prop.propMap}
key1:val1
key2:val2, key3:val3,key4:val4

key5:val5, key6: some phrase or something a little more
```


### Java code example
```java
CuteConfiguration config = CuteConfiguration.builder().filePath(CONFIG_FILE).refreshPeriodMS(1000).build();
List<String> propList = config.getStringList("some.prop.propList");

Map<String, String> propMap = config.getStringMap("some.prop.propMap");

```
