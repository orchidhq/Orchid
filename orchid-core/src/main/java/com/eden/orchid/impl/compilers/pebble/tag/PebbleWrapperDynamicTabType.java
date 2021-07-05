package com.eden.orchid.impl.compilers.pebble.tag;

import com.mitchellbosecke.pebble.lexer.Token;
import com.mitchellbosecke.pebble.node.RenderableNode;
import com.mitchellbosecke.pebble.parser.Parser;
import com.mitchellbosecke.pebble.tokenParser.TokenParser;

import java.util.Map;

public class PebbleWrapperDynamicTabType implements TokenParser {

    private final String tabName;

    public PebbleWrapperDynamicTabType(String tabName) {
        this.tabName = tabName;
    }

    @Override
    public String getTag() {
        return tabName;
    }

    @Override
    public RenderableNode parse(Token token, Parser parser) {
        return null;
    }
}
