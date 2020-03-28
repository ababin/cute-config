# Wrapper for working with specified format configuration files (readable and reloadable)

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


Command for installing artifact into repository:

```
 mvn8 install:install-file -DgroupId=ru.absoft.util -DartifactId=cute-config -Dversion=1.0.1 -Dfile=target/cute-config.jar -Dpackaging=jar -DgeneratePom=true -DlocalRepositoryPath=/home/alexander/projects/github/mvnrepo  -DcreateChecksum=true
```

### Java code example
```java
CuteConfiguration config = new CuteConfiguration(CONFIG_FILE, 1000);
List<String> propList = config.getStringList("some.prop.propList");

Map<String, String> propMap = config.getStringMap("some.prop.propMap");

```
