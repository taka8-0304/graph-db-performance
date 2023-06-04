## クエリ

### Gremlinをクエリのインタフェースで使用出来るかの検討

#### Neo4jアクセス

GremlinからNeo4jにアクセスすることは可能だが、Neo4jの3までしか対応していない。

### Cypherを他のデータベースで使用出来るかの検討

#### Gremlin

[cypher for gremlin](https://github.com/opencypher/cypher-for-gremlin)で変換できるが、こちらの最終リリースが2019年になっている。言語仕様はそれほど変わっていないと思われるので、おおむね動作すると思われる。

#### ArangoDB

OSSのツールは発見できず

## 参考

- [Cypher For Gremlin](https://github.com/opencypher/cypher-for-gremlin)
- [Tinkerpop Data System Provider](https://tinkerpop.apache.org/docs/current/dev/provider/)
