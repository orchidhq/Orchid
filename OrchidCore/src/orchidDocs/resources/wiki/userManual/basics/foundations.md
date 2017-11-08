---
components:
  - type: pageContent
  - type: prism
    theme: 'twilight'
    languages:
      - 'yaml'
      - 'java'
      - 'groovy'
      - 'markdown'
---

> Orchid: A beautiful and truly unique documentation engine and static site generator

When the World Wide Web was first created, web pages were nothing more than a document sitting in a web server. URLs 
pointed directly to files on a remote filesystem, and servers would return those files from across the Internet. Life
was simpler back then, and web development was easy. Just drop a file on your server and you were done.

In time, world began to demand more from web sites. Rather than just hosting static content, websites needed to be 
dynamic. People needed to be able to update their website without going through the hassle of logging into their server
and changing files, and server languages like PHP started to read data from a database and construct the webpage when 
requested, rather than having the page already created.

Today, nearly every webpage you visit is dynamic in some fashion. There is simply too much content out there to manage
your website as a static collection of HTML and CSS. The problem with this comes in the fact that, with so much content, 
servers are forced to process extraordinarily large amounts of data in a fraction of a second, which simply doesn't 
scale. This can be mitigated with advanced caching, but implementing your cache layer just-right is a time-intensive, 
error-prone, and difficult journey.

Kinda makes you miss the good-old-days, when men were men, and webpages were just text files.

Orchid brings together these two worlds: dynamic and ever-changing content, but the simplicity of your site simply being
HTML and CSS. But rather than creating your dynamic webpages on the server when requested, Orchid generates _every_ 
page for your _entire_ site all at once, and after Orchid has done its job, you simply drop it all on a server. 

Done. Simple. Secure.

Orchid certainly isn't the first tool that does this, there are hundreds of alternatives out there. Static Site 
generators have gained great popularity in recent years, especially with Github's integration of Jekyll into Github
Pages, for all the reasons stated above. But there is a serious gap in the market of SSGs that fundamentally limits
their usefulness, which Orchid was designed specifically to solve.

Nearly every SSG trademarks itself as being "blog-ready", but in reality this just means that its content model is 
fundamentally limited, and locked into the Wordpress-like model of blog posts and static pages. While many people
choose to use these tools for blogs, a much larger share of static sites go toward generating documentation for 
open-source projects. It is precisely this reason that Github even created Github Pages and maintains Jekyll, and yet, 
for most of these projects, the documentation is not much different from the first webpages. 

Orchid is the first Static Site Generator targeted specifically at generating the best, most flexible, and most 
beautiful documentation pages available. Rather than limiting itself to a single content model like a blog or wiki, 
Orchid starts by taking one step back, and asking users what kind of content _they_ want to produce. Does it come from
files structured in a certain way? Does it come by reading comments in your source code, or hooking into tools that 
produce an intelligent model of your code, such as Javadoc? Does the data from from a headless CMS over an API? It 
doesn't matter, Orchid can handle it all. 

Orchid indexes every page in your site, and is then able to do really cool stuff that other tools simply cannot handle: 
producing dynamic menus, generating sitemaps, transparently linking to other Orchid sites as if it were your own, all
while giving you the freedom and creativity offered by simple, yet powerful, themeing capabilities. Orchid was inspired
by the best features of these other tools, filling in the missing gaps and removing the clutter, bringing you a tool 
that is at once familiar yet completely new. 

If you've worked with tools like Jekyll, GitBook, or Hugo, you will feel right at home with Orchid, and once you get 
familiar with it, you'll never want to go back. But if you're new to SSGs, you'll find that it's simpler than ever to 
create and maintain a site customized to exactly your needs, flexible enough to work with any kind of content, and 
able to maintain even the largest sites without the risk of feature- or theme- lock-in.

Your project is unique and beautiful. It's time your website is too. 