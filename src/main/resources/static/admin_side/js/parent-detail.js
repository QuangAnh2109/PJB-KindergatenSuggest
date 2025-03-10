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
            updateUrlParams(currentPage);
        }
        $.get({
            url: "/manager/parent-list/parent-details/" + parentId,
            data: {

                currentPage: currentPage,
            },
            success: function(responseData) {
                console.log("LOADED!");
                $("#main-content").html($(responseData).find("#main-content").html());
            },
        });
    };

    function updateUrlParams(currentPage) {
        let newUrl = window.location.pathname + "?currentPage=" + currentPage;
        window.history.pushState({ path: newUrl }, "", newUrl);
    }


});