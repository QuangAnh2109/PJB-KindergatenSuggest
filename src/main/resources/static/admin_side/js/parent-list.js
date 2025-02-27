$(document).ready(function (){

    var timeout = null;
    var num = $('#parentSearchField').val();
    $("body").on("change keydown paste input", "input#parentSearchField", function() {
        clearTimeout(timeout);
        timeout = setTimeout(function() {
            findAll($("#parentSearchField").val(), 0);
        }, 500);
    })
    $('#parentSearchField').focus().val('').val(num);

    $("body").on("click", "li.parent-page-item", function() {
        if ($(this).data("page") != null) {
            console.log($(this).data("page"))
            findAll($("#parentSearchField").val(),$(this).data("page"));
        }
    })
    function findAll(search,currentPage) {
        $.get({
            url: "/manager/parent-list",
            data: {
                search: search,
                currentPage: currentPage,
            },
            success: function(responseData) {
                console.log("LOADED!");
                $("#main-content").html(responseData);
            },
        });
    };
});