$(document).ready(function() {
    var timeout = null;
    var num = $('#userSearchField').val();

    // Khi nhập vào ô search
    $("#userSearchField").on("input", function() {
        clearTimeout(timeout);
        timeout = setTimeout(function() {
            findAll($("#userSearchField").val(), 0);
        }, 500);
    });

    $('#userSearchField').focus().val('').val(num);

    // Xử lý phân trang bằng AJAX
    $("body").on("click", "a.user-page-item", function(event) {
        event.preventDefault(); // Ngăn tải lại trang
        let page = $(this).data("page");
        if (page !== undefined) {
            findAll($("#userSearchField").val(), page);
        }
    });

    function findAll(keySearch, currentPage) {
        $.get({
            url: "/admin/userlist",
            data: {
                search: keySearch,
                currentPage: currentPage,
            },
            success: function(responseData) {
                let newContent = $(responseData);
                $("#userListContent").html(newContent.find("#userListContent").html());
                $(".pagination").html(newContent.find(".pagination").html());
            },
            error: function() {
                console.error("Error fetching data.");
            }
        });
    }

});
