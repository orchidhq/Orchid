---toml
[address]
  street = "123 A Street"
  city = "AnyVille"
  
[contacts]
  "email address" = "me@example.com"
---

Adds support for processing TOML in front matter and configuration files. 

Front matter of

```
---toml
title = "TOML Parser"

[address]
  street = "123 A Street"
  city = "AnyVille"
  
[contacts]
  "email address" = "me@example.com"
---
```

becomes

```
{{ data | json }}
```