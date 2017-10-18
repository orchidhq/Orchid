document.addEventListener('DOMContentLoaded', function () {
    var request = new XMLHttpRequest();
    request.open('GET', '{{ component.changelogVersionSource }}', true);

    request.onload = function () {
        if (request.status >= 200 && request.status < 400) {
            var versions = JSON.parse(request.responseText).childrenPages.versions.childrenPages;

            var dropdownVersions = document.getElementById('versionDropdown');
            dropdownVersions.innerHTML = '';

            for (var key in versions) {
                if (versions.hasOwnProperty(key)) {
                    var li = document.createElement('li');
                    var a = document.createElement('a');
                    var linkText = document.createTextNode(versions[key].ownKey);
                    a.appendChild(linkText);
                    a.href = versions[key].ownPages[0].reference.link;
                    li.appendChild(a);
                    dropdownVersions.appendChild(li);
                }
            }

            $('.dropdown-button.versions-dropdown').sideNav({});
        }
    };
    request.send();
}, false);