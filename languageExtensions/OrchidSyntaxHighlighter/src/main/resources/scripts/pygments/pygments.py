from pygments import highlight
from pygments.lexers import get_lexer_by_name
from pygments.formatters import HtmlFormatter

lexer = get_lexer_by_name(codeLanguage, stripall=True)
formatter = HtmlFormatter(linenos='inline', cssclass='highlight')
result = highlight(code, lexer, formatter)