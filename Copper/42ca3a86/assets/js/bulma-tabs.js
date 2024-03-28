/*
MIT License

Copyright (c) 2019 Jens Törnell

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

/*
Github: https://github.com/jenstornell/tabbis.js
 */


var tabbis = (function () {
    var fn = {};

    var data;
    var types = ['tab', 'pane'];
    var type;
    var groups = [];
    var activeGroups = [];
    var activeChildren = [];
    var activeItems = [];
    var indexGroup;
    var indexItem;
    var memory = [];

    fn.init = function(options) {
        data = options;
        fn.setDefaults();
        fn.setMemory();

        groups['tab'] = document.querySelectorAll(data.tabGroup);
        groups['pane'] = document.querySelectorAll(data.paneGroup);

        for( var groupIndex = 0; groupIndex < groups['tab'].length; groupIndex++ ) {
            var tabItems = groups['tab'][groupIndex].children;

            for( var itemIndex = 0; itemIndex < tabItems.length; itemIndex++ ) {
                tabItems[itemIndex].addEventListener('click', fn.onClick.bind(this, groupIndex, itemIndex), false);

                indexGroup = groupIndex;
                indexItem = itemIndex;

                if(!fn.hasMemory()) continue;
                fn.setNodes(groupIndex, itemIndex);
            }
        }
    };

    fn.onClick = function(groupIndex, itemIndex) {
        fn.setNodes(groupIndex, itemIndex);

        fn.setCallback(indexGroup, indexItem);
    };

    fn.setNodes = function(groupIndex, itemIndex) {
        indexGroup = groupIndex;
        indexItem = itemIndex;

        for( var i = 0; i < types.length; i++ ) {
            type = types[i];

            fn.setActiveGroup();
            fn.setActiveChildren();
            fn.setActiveItems();
            fn.putActiveClass();
        }

        memory[groupIndex] = [];
        memory[groupIndex][itemIndex] = true;

        localStorage.setItem('tabbis', JSON.stringify(memory));
    };

    fn.hasMemory = function() {
        if( typeof memory === 'undefined' ) return;
        if( typeof memory[indexGroup] === 'undefined' ) return;
        if( typeof memory[indexGroup][indexItem] === 'undefined') return;
        if( memory[indexGroup][indexItem] !== true ) return;
        return true;
    };

    fn.setMemory = function() {
        if(localStorage.getItem('tabbis') === null) return;
        if(localStorage.getItem('tabbis').length == 0) return;

        memory = Object.values(JSON.parse(localStorage.getItem('tabbis')));
    };

    fn.putActiveClass = function() {
        for( var i = 0; i < activeChildren[type].length; i++ ) {
            activeChildren[type][i].classList.remove(data[type + 'Active']);
        }

        activeItems[type].classList.add(data[type + 'Active']);
    };

    fn.setDefaults = function() {
        for( var i = 0; i < types.length; i++ ) {
            type = types[i];

            fn.setOption(type + 'Group', '[data-' + type + 's]');
            fn.setOption(type + 'Active', 'active');
        }
    };

    fn.setOption = function(key, value) {
        data = data || [];
        data[key] = data[key] || value;
    };

    fn.setActiveGroup = function() {
        activeGroups[type] = groups[type][indexGroup];
    };

    fn.setActiveChildren = function() {
        activeChildren[type] = activeGroups[type].children;
    };

    fn.setActiveItems = function() {
        activeItems[type] = activeChildren[type][indexItem];
    };

    fn.setCallback = function() {
        if (typeof data.callback === "function") {
            data.callback(activeItems.tab, activeItems.pane);
        }
    };

    fn.reset = function() {
        for( var groupIndex = 0; groupIndex < groups['tab'].length; groupIndex++ ) {
            tabItems = groups['tab'][groupIndex].children;
            paneItems = groups['pane'][groupIndex].children;

            for( var itemIndex = 0; itemIndex < tabItems.length; itemIndex++ ) {
                tabItems[itemIndex].classList.remove(data['tabActive']);
                paneItems[itemIndex].classList.remove(data['paneActive']);
            }
        }
        localStorage.removeItem('tabbis');
    };

    return fn;
})();

tabbis.init({
    tabGroup: '[data-tabs]',
    paneGroup: '[data-panes]',
    tabActive: 'is-active',
    paneActive: 'is-block'
});