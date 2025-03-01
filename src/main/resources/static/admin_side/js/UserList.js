$(document).ready(function () {

    $("body").on("click", "a.edit-user-btn", function (event) {
        event.preventDefault();
        var userId = $(this).data("userid");
        window.location.href = "/admin/edit-user/" + userId;
    });

    $("body").on("click", "a.delete-user-btn", function (event) {
        event.preventDefault(); // Ngăn chặn hành động mặc định của thẻ <a>

        // Lấy userId từ thuộc tính data-userid của nút xóa
        var userId = $(this).data("userid");

        // Xác nhận trước khi xóa
        if (confirm("Are you sure you want to delete this user?")) {
            // Gửi yêu cầu DELETE đến server
            $.get({
                url: "/admin/api/user/" + userId, // URL endpoint
                success: function (responseData) {
                    // Hiển thị thông báo thành công
                    alert(responseData);
                    // Tải lại danh sách người dùng hoặc xóa hàng khỏi bảng
                    location.reload(); // Tải lại trang để cập nhật danh sách
                },
                error: function (responseData) {
                    // Hiển thị thông báo lỗi
                    alert("Failed to delete user: " + responseData.responseText);
                }
            });
        }
    });


    var timeout = null;
    var num = $('#userSearchField').val();

// Khi nhập vào ô search và nhấn Enter
    $("#userSearchField").on("keydown", function (event) {
        if (event.key === "Enter") {
            event.preventDefault(); // Ngăn chặn reload trang
            findAll($(this).val(), 0);
        }
    });

// Khi bấm vào nút search
    $("#userSearchButton").on("click", function () {
        findAll($("#userSearchField").val(), 0);
    });

    $('#userSearchField').focus().val('').val(num);

// Xử lý phân trang bằng AJAX
    $("body").on("click", "a.user-page-item", function (event) {
        event.preventDefault(); // Ngăn tải lại trang
        let page = $(this).data("page");
        if (page !== undefined) {
            findAll($("#userSearchField").val(), page);
        }
    });

    function findAll(keySearch, currentPage) {
        $.get({
            url: "/admin/user-list",
            data: {
                search: keySearch,
                currentPage: currentPage,
            },
            success: function (responseData) {
                let newContent = $(responseData);
                $("#userListContent").html(newContent.find("#userListContent").html());
                $(".pagination-container").html(newContent.find(".pagination-container").html());

                // // Hiển thị hoặc ẩn thông báo "No results found."
                // if (newContent.find("#userListContent tbody tr").length === 0) {
                //     $("#noResultsMessage").show();
                // } else {
                //     $("#noResultsMessage").hide();
                // }
            },
            error: function () {
                console.error("Error fetching data.");
            }
        });
    }
});
