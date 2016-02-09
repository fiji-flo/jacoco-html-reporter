# jacoco-html-reporter
Simpe tool to gernerate coverage reports

### Usage

```
java -jar jacoco-html-reporter-jar-with-dependencies.jar /path/for/output "/path/to/exec1;/other/execFile" "/some/path/to/classes;/oherclasses" "/fancy/sources;/more/of/them"
```

Usually the paths like the following.
Jacoco exec files:
```
/home/foo/project/target/jacoco.exec
```
Classes:
```
/home/foo/project/target/classes
```
Sources:
```
/home/foo/project/src/main/java
```
