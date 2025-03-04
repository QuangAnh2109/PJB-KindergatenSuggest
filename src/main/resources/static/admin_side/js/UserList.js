$(document).ready(function () {

    $("body").on("click", "a.edit-user-btn", function (event) {
        event.preventDefault();
        var userId = $(this).data("userid");
        window.location.href = "/admin/edit-user/" + userId;
    });



    var selectedUserId = null;

    // Khi người dùng nhấn vào icon delete
    $("body").on("click", "a.delete-user-btn", function (event) {
        event.preventDefault();
        selectedUserId = $(this).data("userid");
        $("#deleteUserModal").modal("show");
    });

    // Khi người dùng xác nhận xóa
    $("#confirmDeleteUser").click(function () {
        if (selectedUserId) {
            $.get({
                url: "/admin/api/user/" + selectedUserId,
                success: function (responseData) {
                    $("#deleteUserModal").modal("hide"); // Đóng modal xác nhận
                    $("#deleteResultMessage").text(responseData);
                    $("#deleteResultModal").modal("show"); // Hiển thị modal kết quả
                    setTimeout(function () {
                        location.reload(); // Tải lại danh sách sau khi đóng modal
                    }, 1500);
                },
                error: function (responseData) {
                    $("#deleteUserModal").modal("hide"); // Đóng modal xác nhận
                    $("#deleteResultMessage").text("Failed to delete user: " + responseData.responseText);
                    $("#deleteResultModal").modal("show"); // Hiển thị modal thất bại
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
