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

    var currentPage = parseInt(sessionStorage.getItem("currentPage")) || 0;
    var searchKey = sessionStorage.getItem("searchKey") || "";

    // Điền lại giá trị vào ô tìm kiếm khi load trang
    $("#userSearchField").val(searchKey);

    function setPage(page) {
        sessionStorage.setItem("currentPage", page);
        history.replaceState(null, null, "?page=" + page);
    }

    function setSearchKey(key) {
        sessionStorage.setItem("searchKey", key);
    }

    function loadPageData() {
        let searchKey = $("#userSearchField").val();
        $.get({
            url: "/admin/user-list",
            data: {
                search: $("#userSearchField").val(),
                currentPage: parseInt(sessionStorage.getItem("currentPage")) || 0,
            },
            success: function (responseData) {
                let newContent = $(responseData);
                $("#userListContent").html(newContent.find("#userListContent").html());
                $(".pagination-container").html(newContent.find(".pagination-container").html());
            },
            error: function () {
                console.error("Error fetching data.");
            }
        });
    }

    // Khi nhấn vào phân trang, chỉ tải lại nội dung mà không reload trang
    $("body").on("click", "a.user-page-item", function (event) {
        event.preventDefault();
        let page = $(this).data("page");

        if (page !== undefined) {
            currentPage = page;  // Cập nhật biến global
            setPage(page); // Lưu vào sessionStorage
            loadPageData();
        }
    });

    // Khi nhấn nút tìm kiếm
    $("#userSearchButton").click(function () {
        let searchValue = $("#userSearchField").val();
        setSearchKey(searchValue);
        setPage(0); // Reset về trang đầu khi tìm kiếm
        loadPageData();
    });

    // Khi nhấn Enter trong ô tìm kiếm
    $("#userSearchField").keypress(function (event) {
        if (event.which === 13) { // Enter key
            event.preventDefault();
            $("#userSearchButton").click();
        }
    });

    // Khi trang vừa tải xong, tự động tải dữ liệu trang hiện tại luôn
    if (currentPage > 0 || searchKey !== "") {
        loadPageData();
    }

    // Khi xác nhận xóa, tải lại trang hiện tại mà không bị nháy về trang đầu
    $("#confirmDeleteUser").click(function () {
        if (selectedUserId) {
            $.get({
                url: "/admin/api/user/" + selectedUserId,
                success: function (responseData) {
                    $("#deleteUserModal").modal("hide");
                    $("#deleteResultMessage").text(responseData);
                    $("#deleteResultModal").modal("show");
                    setTimeout(function () {
                        loadPageData(); // Chỉ tải lại nội dung thay vì reload toàn trang
                    }, 1500);
                },
                error: function (responseData) {
                    $("#deleteUserModal").modal("hide");
                    $("#deleteResultMessage").text("Failed to delete user: " + responseData.responseText);
                    $("#deleteResultModal").modal("show");
                }
            });
        }
    });

    // Khi trang vừa tải xong, tự động tải dữ liệu trang hiện tại luôn
    if (currentPage > 0 || searchKey !== "") {
        loadPageData();
    }
});