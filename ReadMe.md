#### Analyzer
Contains code to analyze a large `jsonl` file containing information of a blog post & gather statistics.

Tools used: 
- Scala: 2.13.4 | Sbt: 1.4.7 
  - Tested with Java: 1.8/11 (AdoptOpenJDK)
- [Apache common math3](https://commons.apache.org/proper/commons-math/javadocs/api-3.6.1/index.html)
    - [Frequency](https://commons.apache.org/proper/commons-math/javadocs/api-3.6.1/org/apache/commons/math3/stat/Frequency.html)
    - [Mean](https://commons.apache.org/proper/commons-math/javadocs/api-3.6.1/org/apache/commons/math3/stat/descriptive/moment/Mean.html)
- [TDigest](https://github.com/tdunning/t-digest)
- [uPickle](https://github.com/com-lihaoyi/upickle)

#### How to run

- `sbt "run $filename"`
- File `small.jsonl` represents a sample dataset format.  