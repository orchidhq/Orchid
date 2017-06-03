---csv

Col 1, Col 2, Col 3
1,2,3
a,b,c
x,y,z

---

Adds support for processing CSV/TSV in front matter and configuration files. 

Front matter of

```
---csv

Col 1, Col 2, Col 3
1,2,3
a,b,c
x,y,z

---
```

becomes

```
{{ data | json }}
```