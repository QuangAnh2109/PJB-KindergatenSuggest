$(document).ready(function (){


    $("body").on("click", "li.parent-page-item", function() {
        if ($(this).data("page") != null) {
            console.log($(this).data("page"))
            findAll($(this).data("page"));
        }
    })

    var role = $("#userRole").val().trim();
    var parentId =$("#parentId").val();
    function findAll(currentPage) {
        $.get({
            url: "/" + role + "/parent-list/parent-details/" + parentId,
            data: {

                currentPage: currentPage,
            },
            success: function(responseData) {
                console.log("LOADED!");
                $("#main-content").html(responseData);
            },
        });
    };
});