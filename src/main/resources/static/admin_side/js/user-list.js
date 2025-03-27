$(document).ready(function () {

    $("body").on("click", "a.edit-user-btn", function (event) {
        event.preventDefault();
        var userId = $(this).data("userid");
        var currentUrl = window.location.href;
        localStorage.setItem('previousUrl', currentUrl);
        window.location.href = "/admin/edit-user/" + userId;
    });

    // when click con <tr>
    $("body").on("click", "tr[data-userid]", function (event) {
        let target = $(event.target);

        // if not a link <a> direct to page details
        if (target.closest("a").length === 0) {
            let userId = $(this).data("userid");
            window.location.href = "/admin/user-details/" + userId;
        }
    });

    var selectedUserId = null;

    // when click on trash icon
    $("body").on("click", "a.delete-user-btn", function (event) {
        event.preventDefault();
        selectedUserId = $(this).data("userid");
        $("#deleteUserModal").modal("show");
    });

    // Confirm delete
    $("#confirmDeleteUser").click(function () {
        if (selectedUserId) {

            $.get({
                url: "/admin/api/delete/" + selectedUserId,
                success: function (responseData) {
                    $("#deleteUserModal").modal("hide");
                    $("#deleteResultMessage").text(responseData);
                    $("#deleteResultModal").modal("show");
                    setTimeout(function () {
                        location.reload();
                    }, 1500);
                },
                error: function (responseData) {
                    $("#deleteUserModal").modal("hide"); // Close modal
                    $("#deleteResultMessage").text("Failed to delete user: " + responseData.responseText);
                    $("#deleteResultModal").modal("show"); //display modal
                }
            });
        }
    });

    var timeout = null;
    var num = $('#userSearchField').val();

// when input in search field and press Enter
    $("#userSearchField").on("keydown", function (event) {
        if (event.key === "Enter") {
            event.preventDefault(); // Ngăn chặn reload trang
            findAll($(this).val(), 0);
        }
    });

// Khi click on button search instead of press Enter
    $("#userSearchButton").on("click", function () {
        findAll($("#userSearchField").val(), 0);
    });

    $('#userSearchField').focus().val('').val(num);

// Pagination with ajax
    $("body").on("click", "a.user-page-item", function (event) {
        event.preventDefault(); // Ngăn tải lại trang
        let page = $(this).data("page");
        if (page !== undefined) {
            findAll($("#userSearchField").val(), page);
        }
    });

    function findAll(keySearch, currentPage) {
        let newUrl = window.location.pathname + "?search=" + encodeURIComponent(keySearch) + "&currentPage=" + currentPage;
        window.history.pushState({ path: newUrl }, "", newUrl); // Cập nhật URL mà không reload

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
            },
            error: function () {
                console.error("Error fetching data.");
            }
        });
    }

});
