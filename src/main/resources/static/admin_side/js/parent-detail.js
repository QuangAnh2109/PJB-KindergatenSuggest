$(document).ready(function (){


    $("body").on("click", "li.parent-page-item", function() {
        if ($(this).data("page") != null) {
            console.log($(this).data("page"))
            findAll($(this).data("page"),true);
        }
    })

    var parentId =$("#parentId").val();
    function findAll(currentPage,updateUrl = false) {
        if (updateUrl) {
            updateUrlParams(search, currentPage);
        }
        $.get({
            url: "/manager/parent-list/parent-details/" + parentId,
            data: {

                currentPage: currentPage,
            },
            success: function(responseData) {
                console.log("LOADED!");
                document.getElementById("main-content").outerHTML = responseData;
            },
        });
    };

    function updateUrlParams(search, currentPage) {
        let newUrl = window.location.pathname + "?currentPage=" + currentPage;
        window.history.pushState({ path: newUrl }, "", newUrl);
    }

    // Load correct search & page from URL on page reload
    let urlParams = new URLSearchParams(window.location.search);
    let currentPage = parseInt(urlParams.get("currentPage")) || 0;

    $("#parentSearchField").val(search); // Restore search field value
    findAll(currentPage, false);

});