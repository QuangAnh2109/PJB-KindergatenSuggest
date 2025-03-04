$(document).ready(function () {

    var num = $('#parentSearchField').val();

// Trigger search on Enter key
    $("body").on("keyup", "input#parentSearchField", function (event) {
        if (event.key === "Enter") {
            findAll($(this).val(), 0);
        }
    });

// Trigger search on button click
    $("body").on("click", "button#searchButtonParent", function (event) {
        findAll($('#parentSearchField').val(), 0);
    });

// Keep previous value on focus
    $('#parentSearchField').focus().val('').val(num);
    $("body").on("click", "li.parent-page-item", function () {
        if ($(this).data("page") != null) {
            console.log($(this).data("page"))
            findAll($("#parentSearchField").val(), $(this).data("page"));
        }
    })

    function findAll(search, currentPage) {
        $.get({
            url: "/manager/parent-list",
            data: {
                search: search,
                currentPage: currentPage,
            },
            success: function (responseData) {
                console.log("LOADED!");
                $("#main-content").html(responseData);
            },
            error: function (responseData) {
                // Hiển thị thông báo lỗi
                alert("Failed to Search user: " +$('#parentSearchField').val() );
            }
        });
    };
});