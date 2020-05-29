---
title: Copper - No-Box Page Layout
theme: 
  from: 'theme.demo.Copper'
layoutConfig:
    wrapPageInBox: false
    wrapTitleInBox: false
    includeTitle: true
    includeBreadcrumbs: false
skipTaxonomy: true
parent: Copper Demo
description: This layout can be used to be build complex layouts with custom-formatted HTML.
---

<div class="columns">
  <div class="column">
    <div class="box">
{% filter compileAs('md') %}
## Pronus et Alcidae iacuit

Lorem markdownum ponto; [per](#) qui quasque.
Cincta obscena: intus, fert totum prodere clamore, progenies, Agenorides; parva
illo circum nec salutis. Vulnus tardatus Saturnia Aello, milite par vitam
Diomede se rauca insula nunc tempore.

- Loquiturque catenis magno conspicui infelix
- Dubiaeque fallunt
- Postquam in humus sed nam primis hanc
{% endfilter %}
    </div>
  </div>
  <div class="column">
    <div class="box">
{% filter compileAs('md') %}
## Virgo ferebant urbes redditur et et sit

Peto tum, ilice illis elementa sperando in fuit et dixit, quoque Asiae: haut.
Promissaque nunc nos gravi [deducit tum patres](#)
aditus haec, est *potentia sepulcri* derat.

## Aliena quod pro

Iubeoque ut longa dumque retinens ego nihil ausa altera vultum tacta mitis
mirabile iam cuncta modus neque. Cruorem aequalique [operi
me](#) sic coepere! Amores ductus, isset et feris testantia,
satis introrsus aurum possedit disiectum subitis classe? Gaudet tumebat, nec
ille folioque inculpata madefecit iter,
[prius](#).
{% endfilter %}
    </div>
  </div>
</div>
