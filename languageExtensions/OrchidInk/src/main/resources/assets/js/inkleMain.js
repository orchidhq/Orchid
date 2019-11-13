/* globals inkjs storyContent */

(function(storyContent) {
    var clear_on_choice;

    // If set to true, this will cause the page to clear after every choice,
    // similar to how Twine and ChoiceScript work
    setPageClearing(false);

    // Create ink story from the content using inkjs
    var story = new inkjs.Story(storyContent);

    // Just some examples of how interactions between Ink and JS work
    /*
        // Important note:
        //   All functions and variables must be already declared in
        //   Ink before you can access them in javascript

        Function binding
        - https://github.com/inkle/ink/blob/master/Documentation/RunningYourInk.md#external-functions

            ink declaration:
                EXTERNAL multiply(x,y)

            javascript:
                story.BindExternalFunction("multiply", function(arg1, arg2) {
                    return arg1 * arg2;
                });

            example use in ink:
                3 times 4 is {multiply(3, 4)}.

        Setting variables
        - https://github.com/inkle/ink/blob/master/Documentation/RunningYourInk.md#settinggetting-ink-variables

            ink declaration:
                VAR player_health = 50

            javascript:
                story.variablesState["player_health"] = 100;
                var health = story.variablesState["player_health"];

        Lists
        - https://github.com/inkle/ink/blob/master/Documentation/RunningYourInk.md#working-with-lists

            ink declaration:
                LIST fruit = orange, apple, banana
                LIST shopping_list = default
                ~ shopping_list = ()  // clear list

            javascript:
                var newList = new inkjs.InkList("fruit", story);
                newList.AddItem("apple");
                story.variablesState["shopping_list"] = newList;

        Variable Observer (works with both LIST and VAR)
        - https://github.com/inkle/ink/blob/master/Documentation/RunningYourInk.md#variable-observers

            ink declaration:
                VAR health = 100

            observe variables in JS:
                story.ObserveVariable("health", function(varName, newValue) {
                    SetHealthInUI(newValue);
                });
    */

    // Tag Selectors
    var _StoryContainer = document.getElementById("story");
    var _BackButton = document.getElementById('back_button');
    var _OuterScrollContainer = document.querySelector(".outerContainer");

    // Global tags - those at the top of the ink file
    // We support:
    //  # title: Your Story Title
    //  # author: Your Name
    //  # pageclearing: true / false
    //  # history: 0 (off) or number of history items for use with back button

    var history_count = 0;

    var story_title = "inkstory";
    var globalTags = story.globalTags;
    if( globalTags ) {
        for(var i=0; i<story.globalTags.length; i++) {
            var globalTag = story.globalTags[i];
            var splitTag = splitPropertyTag(globalTag);

            if (!splitTag) continue;

            splitTag.property = splitTag.property.toUpperCase();

            switch(splitTag.property) {
                case "PAGECLEARING":
                    setPageClearing(JSON.parse(splitTag.val));
                    break;
                case "HISTORY":
                    history_count = parseInt(splitTag.val);
                    if (history_count > 0) _BackButton.style.display = "inline-block";
                    else {
                        history_count = 0;
                        setVisible("#back_button", false);
                    }
                    break;
            }
        }
    }

    var saveText = [];
    var saveTags = [];

    var history = [];

    var localStorage = {};

    checkLocalStorage();
    if (!loadGame(true)) continueStory(true);

    // Kick off the start of the story!

    // Main story processing function. Each time this is called it generates
    // all the next content up as far as the next set of choices.
    function continueStory(firstTime, paragraphs, tags) {
        var delay = 0.0;

        // Don't over-scroll past new content
        var previousBottomEdge = (firstTime) ? 0 : contentBottomEdgeY();

        if (firstTime || clear_on_choice) scrollToTop();

        if (firstTime === undefined && clear_on_choice) {
            clearText();
        }

        if (tags === undefined) tags = [];

        if (paragraphs === undefined) {
            paragraphs = [];

            while(story.canContinue) {
                // Get ink to generate the next paragraph
                paragraphs.push(story.Continue());
                tags.push(story.currentTags);
            }
        }
        saveText = paragraphs;
        saveTags = tags;

        if (history_count > 0) {
            history.push(storeData());
            if (history.length > history_count) {
                history.splice(0, 1);
            }
        }

        // Generate story text - loop through available content
        var a, b;
        for (a = 0; a < paragraphs.length; ++a) {
            var customClasses = [];

            // Any special tags included with this line
            if (tags[a] !== undefined) {
                for (b = 0; b < tags[a].length; ++b) {
                    if (tags[b] === undefined) continue;

                    var tag = tags[a][b];

                    // Detect tags of the form "X: Y". Currently used for IMAGE and CLASS but could be
                    // customised to be used for other things too.
                    var splitTag = splitPropertyTag(tag);
                    if (splitTag) splitTag.property = splitTag.property.toUpperCase();
                    tag = tag.toUpperCase();

                    // IMAGE: src
                    if( splitTag && splitTag.property == "IMAGE" ) {
                        var imageElement = document.createElement('img');
                        imageElement.src = splitTag.val;
                        _StoryContainer.appendChild(imageElement);

                        showAfter(delay, imageElement);
                        delay += 200.0;
                    }

                    // CLASS: className
                    else if( splitTag && splitTag.property == "CLASS" ) {
                        customClasses.push(splitTag.val);
                    }

                    // CLEAR - removes all existing content.
                    // RESTART - clears everything and restarts the story from the beginning
                    else if( tag == "CLEAR" || tag == "RESTART" ) {
                        removeAll("p");
                        removeAll("img");

                        if( tag == "RESTART" ) {
                            restart();
                            return;
                        }
                    }
                }
            }

            // Create paragraph element (initially hidden)
            var paragraphElement = document.createElement('p');
            paragraphElement.innerHTML = paragraphs[a];
            _StoryContainer.appendChild(paragraphElement);

            // Add any custom classes derived from ink tags
            for(i=0; i<customClasses.length; i++)
                paragraphElement.classList.add(customClasses[i]);

            // Fade in paragraph after a short delay
            showAfter(delay, paragraphElement);
            delay += 200.0;
        }

        // Create HTML choices from ink choices
        story.currentChoices.forEach(function(choice) {

            // Create paragraph with anchor element
            var choiceParagraphElement = document.createElement('p');
            choiceParagraphElement.classList.add("choice");
            choiceParagraphElement.innerHTML = "<a>" + choice.text + "</a>";
            _StoryContainer.appendChild(choiceParagraphElement);

            // Fade choice in after a short delay
            showAfter(delay, choiceParagraphElement);
            delay += 200.0;

            // Click on choice
            var choiceAnchorEl = choiceParagraphElement.querySelectorAll("a")[0];
            choiceAnchorEl.addEventListener("click", function(event) {

                // Don't follow <a> link
                event.preventDefault();

                // Remove all existing choices
                removeAll("p.choice");

                // Tell the story where to go next
                story.ChooseChoiceIndex(choice.index);

                // Aaand loop
                continueStory();
            });
        });

        // Extend height to fit
        // We do this manually so that removing elements and creating new ones doesn't
        // cause the height (and therefore scroll) to jump backwards temporarily.
        _StoryContainer.style.height = contentBottomEdgeY()+"px";

        if (clear_on_choice) scrollToTop();
        else if(!firstTime) scrollDown(previousBottomEdge);

        saveGame(true);
    }

    function restart(no_confirm) {
        if (!no_confirm && !confirm("Restart story?")) return;

        if (localStorage.getItem(story_title + "_savedata_auto") !== null) {
            localStorage.removeItem(story_title + "_savedata_auto");
        }

        location.reload(true);

        /*

        story.ResetState();

        clearText();

        continueStory(true);

        scrollToTop();

        */

        // _OuterScrollContainer.scrollTo(0, 0);
    }

    // -----------------------------------
    // Various Helper functions
    // -----------------------------------

    // Fades in an element after a specified delay
    function showAfter(delay, el) {
        el.classList.add("hide");
        setTimeout(function() { el.classList.remove("hide") }, delay);
    }

    // Scrolls the page down, but no further than the bottom edge of what you could
    // see previously, so it doesn't go too far.
    function scrollDown(previousBottomEdge) {

        // Line up top of screen with the bottom of where the previous content ended
        var target = previousBottomEdge - 35;

        // Can't go further than the very bottom of the page
        var limit = _OuterScrollContainer.scrollHeight - _OuterScrollContainer.clientHeight;
        if( target > limit ) target = limit;

        var start = _OuterScrollContainer.scrollTop;

        var dist = target - start;
        var duration = 300 + 300*dist/100;
        var startTime = null;
        function step(time) {
            if( startTime == null ) startTime = time;
            var t = (time-startTime) / duration;
            var lerp = 3*t*t - 2*t*t*t; // ease in/out
            _OuterScrollContainer.scrollTop = ((1.0-lerp)*start) + (lerp*target);
            if( t < 1 ) requestAnimationFrame(step);
        }
        requestAnimationFrame(step);
    }

    // The Y coordinate of the bottom end of all the story content, used
    // for growing the container, and deciding how far to scroll.
    function contentBottomEdgeY() {
        var bottomElement = _StoryContainer.lastElementChild;
        return bottomElement ? bottomElement.offsetTop + bottomElement.offsetHeight : 0;
    }

    // Remove all elements that match the given selector. Used for removing choices after
    // you've picked one, as well as for the CLEAR and RESTART tags.
    function removeAll(selector)
    {
        var allElements = _StoryContainer.querySelectorAll(selector);
        for(var i=0; i<allElements.length; i++) {
            var el = allElements[i];
            el.parentNode.removeChild(el);
        }
    }

    // Used for hiding and showing the header when you CLEAR or RESTART the story respectively.
    function setVisible(selector, visible)
    {
        var allElements = _StoryContainer.querySelectorAll(selector);
        for(var i=0; i<allElements.length; i++) {
            var el = allElements[i];
            if( !visible )
                el.style.display = "none";
            // el.classList.add("invisible");
            else
                el.style.display = "block";
            // el.classList.remove("invisible");
        }
    }

    // Helper for parsing out tags of the form:
    //  # PROPERTY: value
    // e.g. IMAGE: source path
    function splitPropertyTag(tag) {
        if (tag === undefined) return null;

        var propertySplitIdx = tag.indexOf(":");
        if( propertySplitIdx != null ) {
            var property = tag.substr(0, propertySplitIdx).trim();
            var val = tag.substr(propertySplitIdx+1).trim();
            return {
                property: property,
                val: val
            };
        }

        return null;
    }

    function checkLocalStorage() {
        //Algorithm to emulate local storage with cookies from MDN
        try {
            if (window.localStorage !== undefined && window.localStorage !== null) {
                localStorage = window.localStorage;
                return;
            }
        }
        catch(exception){
            console.log("window.localStorage not available. Attempting emulation via cookies.")
        }

        // Credit: https://developer.mozilla.org/en-US/docs/Web/API/Web_Storage_API/Local_storage
        localStorage = {
            getItem: function (sKey) {
                // eslint-disable-next-line no-prototype-builtins
                if (!sKey || !this.hasOwnProperty(sKey)) { return null; }
                // eslint-disable-next-line no-useless-escape
                return unescape(document.cookie.replace(new RegExp("(?:^|.*;\\s*)" + escape(sKey).replace(/[\-\.\+\*]/g, "\\$&") + "\\s*\\=\\s*((?:[^;](?!;))*[^;]?).*"), "$1"));
            },
            key: function (nKeyId) {
                // eslint-disable-next-line no-useless-escape
                return unescape(document.cookie.replace(/\s*\=(?:.(?!;))*$/, "").split(/\s*\=(?:[^;](?!;))*[^;]?;\s*/)[nKeyId]);
            },
            setItem: function (sKey, sValue) {
                if(!sKey) { return; }
                document.cookie = escape(sKey) + "=" + escape(sValue) + "; expires=Tue, 19 Jan 2038 03:14:07 GMT; path=/";
                // eslint-disable-next-line no-useless-escape
                this.length = document.cookie.match(/\=/g).length;

                // the cookie name/value pair is typically first
                // var cookie = document.cookie.split(";")[0].split("=")[1];
                // console.log(cookie.length);
                // console.log(cookie.split(/%(?:u[0-9A-F]{2})?[0-9A-F]{2}|./).length);
            },
            length: 0,
            removeItem: function (sKey) {
                // eslint-disable-next-line no-prototype-builtins
                if (!sKey || !this.hasOwnProperty(sKey)) { return; }
                document.cookie = escape(sKey) + "=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/";
                this.length--;
            },
            hasOwnProperty: function (sKey) {
                // eslint-disable-next-line no-useless-escape
                return (new RegExp("(?:^|;\\s*)" + escape(sKey).replace(/[\-\.\+\*]/g, "\\$&") + "\\s*\\=")).test(document.cookie);
            }
        };
        // eslint-disable-next-line no-useless-escape
        localStorage.length = (document.cookie.match(/\=/g) || localStorage).length;
    }

    function storeData() {
        var saveData = JSON.parse(story.state.toJson());
        saveData.saveText = saveText;
        saveData.saveTags = saveTags;
        saveData = JSON.stringify(saveData);
        return saveData;
    }

    function saveGame(no_confirm) {
        var saveSlot = "auto";

        if (!no_confirm && localStorage.getItem(story_title + "_savedata_" + saveSlot) !== null) {
            if (!confirm("Overwrite save file?")) return;
        }

        var saveData = storeData();

        localStorage.setItem(story_title + "_savedata_" + saveSlot, saveData);

        if (!no_confirm) alert("Save successful!");
    }

    function restoreData(saveData) {
        story.state.LoadJson(saveData);
        saveData = JSON.parse(saveData);

        clearText();
        continueStory(true, saveData.saveText, saveData.saveTags);
        scrollToTop();
    }

    function loadGame(no_confirm) {
        var saveSlot = "auto";

        var saveData = localStorage.getItem(story_title + "_savedata_" + saveSlot);

        if (saveData === null) {
            if (!no_confirm) alert("Save data not found!");
            return false;
        }

        if (!no_confirm && !confirm("Load save file? Current progress will be lost.")) return;

        history = [];

        restoreData(saveData);

        if (!no_confirm) alert("Load succesful!" );

        return true;
    }

    function back() {
        if (history.length > 1) history.pop(); // current thread

        var saveData = history.pop(); // previous thread
        restoreData(saveData);
    }

    function clearText() {
        removeAll();
        scrollToTop();
    }

    function scrollToTop() {
        // $(document.body).scrollTop(0);
        // return;
        if (clear_on_choice) {
            document.body.scrollTop = 0; // For Safari
            document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
        }
        else {
            _OuterScrollContainer.scrollTop = 0; // For Safari
        }
    }

    // eslint-disable-next-line no-unused-vars
    function setPageClearing(val) {
        clear_on_choice = val;

        if (val) document.body.classList.add("pageclear");
        else document.body.classList.remove("pageclear");
    }

    window.inkgame = {
        story: story,
        saveGame: saveGame,
        loadGame: loadGame,
        restart: restart,
        back: back,
        setPageClearing: setPageClearing
    };

})(storyContent);
