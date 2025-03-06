$(document).ready(function () {
    var num = $('#parentSearchField').val();

    // Trigger search on Enter key
    $("body").on("keyup", "input#parentSearchField", function (event) {
        if (event.key === "Enter") {
            findAll($(this).val(), 0, true);
        }
    });

    // Trigger search on button click
    $("body").on("click", "button#searchButtonParent", function () {
        findAll($('#parentSearchField').val(), 0, true);
    });

    // Keep previous search value
    $('#parentSearchField').val(num);

    // Pagination click event
    $("body").on("click", "li.parent-page-item", function () {
        let page = $(this).data("page");
        if (page != null) {
            console.log(page);
            findAll($("#parentSearchField").val(), page, true);
        }
    });

    function findAll(search, currentPage, updateUrl = false) {
        if (updateUrl) {
            updateUrlParams(search, currentPage);
        }

        $.get({
            url: "/manager/parent-list",
            data: { search: search, currentPage: currentPage },
            success: function (responseData) {
                console.log("LOADED!");
                document.getElementById("main-content").outerHTML = responseData;
            },
            error: function () {
                alert("Failed to Search user: " + search);
            }
        });
    }

    function updateUrlParams(search, currentPage) {
        let newUrl = window.location.pathname + "?search=" + encodeURIComponent(search) + "&currentPage=" + currentPage;
        window.history.pushState({ path: newUrl }, "", newUrl);
    }

    // Load correct search & page from URL on page reload
    let urlParams = new URLSearchParams(window.location.search);
    let search = urlParams.get("search") || "";
    let currentPage = parseInt(urlParams.get("currentPage")) || 0;

    $("#parentSearchField").val(search); // Restore search field value
    findAll(search, currentPage, false);
});
