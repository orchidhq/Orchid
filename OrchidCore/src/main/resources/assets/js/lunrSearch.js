(function ($, lunr) {

    function initializeSearchField() {
        $('form[data-lunar-search]').submit(function (e) {
            event.preventDefault();

            var $queryEl = $(this).find("input[name=query]");
            var $query = $queryEl.val();

            setSearchWorking(true);
            loadOrchidIndex(function () {
                searchOrchidIndex($query, function (results) {
                    displaySearchResults(results, function () {
                        setSearchWorking(false);
                    });
                });
            });
        });
    }

    function setSearchWorking(isWorking) {
        if (isWorking) {
            $('#search-progress').show();
            $('#search-results').hide();
        }
        else {
            $('#search-progress').hide();
            $('#search-results').show();
        }
    }

    function getOrchidDocuments(cb) {
        if (!window.orchidDocuments) {
            loadRootIndex(function (documents) {
                window.orchidDocuments = documents;
                window.orchidDocumentsMap = {};

                window.orchidDocuments.map(function (document) {
                    window.orchidDocumentsMap[document.link] = document;
                });

                cb(window.orchidDocuments);
            });
        }
        else {
            cb(window.orchidDocuments);
        }
    }

    function loadOrchidIndex(cb) {
        if (!window.orchidIdx) {
            getOrchidDocuments(function (docs) {
                console.log("all docs");
                console.log(docs);
                window.orchidIdx = lunr(function () {
                    this.ref('link');
                    this.field('title');
                    this.field('description');
                    this.field('content');
                    this.metadataWhitelist = ['position'];

                    docs.forEach(function (doc) {
                        this.add(doc)
                    }, this)
                });

                cb(window.orchidIdx);
            });
        }
        else {
            cb(window.orchidIdx);
        }
    }

    function searchOrchidIndex(query, cb) {
        setTimeout(function () {
            var results = window.orchidIdx.search(query);
            cb(results);
        }, 1000)
    }

    function displaySearchResults(results, cb) {
        console.log("displaying results");
        console.log(results);

        var items = [];

        results.map(function (result) {
            var document = window.orchidDocumentsMap[result.ref];
            var summary = getSearchSummary(result, document);

            var $item = "<li><a href='" + document.link + "'>";
            $item += (items.length + 1) + ": " + document.title;
            $item += "</a><br>";
            if (summary.length > 0) {
                $item += "<p>" + summary + "</p>";
            }
            $item += "</li>";

            items.push($item);
        });

        var $searchResults = $('#search-results ul');

        $searchResults.empty();
        $searchResults.html(items.join(''));

        cb();
    }

    function getSearchSummary(result, document) {
        var matches = 0;

        // for every matching word
        for (var word in result.matchData.metadata) {

            // for every document field which has that word
            for (var field in result.matchData.metadata[word]) {
                if (field && result.matchData.metadata[word][field].position) {
                    matches += result.matchData.metadata[word][field].position.length;
                }
            }
        }

        return "Relevance: " + parseFloat(Math.round(result.score * 100) / 100).toFixed(2) + ", " + matches + ' Matches';
    }

// Load indices via AJAX and build a map of documents that can be passed to Lunr
//----------------------------------------------------------------------------------------------------------------------

    function loadRootIndex(done) {
        var allDocs = [];
        $.getJSON(window.site.baseUrl + "/meta/index.json", function (data) {
            var childIndices = data.childrenPages.meta.ownPages;
            var childIndicesFinishedCount = 0;

            childIndices.map(function (indexPage) {
                loadIndexPage(indexPage.reference.link,
                    function (document) {
                        allDocs.push(document);
                    },
                    function () {
                        childIndicesFinishedCount++;

                        if (childIndicesFinishedCount === childIndices.length) {
                            done(allDocs);
                        }
                    });
            });
        });
    }

    function loadIndexPage(url, cb, done) {
        $.getJSON(url, function (data) {
            getChildDocuments(data, cb);
            done();
        });
    }

    function getChildDocuments(data, cb) {
        for (var key in data.childrenPages) {
            if (data.childrenPages.hasOwnProperty(key)) {
                var doc = data.childrenPages[key];

                if (doc.childrenPages) {
                    getChildDocuments(doc, cb);
                }
                else if (doc.ownPages) {
                    doc.ownPages.map(function (ownPage) {
                        ownPage.link = ownPage.reference.link;
                        ownPage.content = stripTags(ownPage.content);
                        delete ownPage.reference;
                        cb(ownPage);
                    });
                }
            }
        }
    }

    function stripTags(html) {
        var doc = new DOMParser().parseFromString(html, 'text/html');
        return doc.body.textContent || "";
    }

// Initialize searching
//----------------------------------------------------------------------------------------------------------------------

    $(function () {
        initializeSearchField();
    });

})(jQuery, lunr);