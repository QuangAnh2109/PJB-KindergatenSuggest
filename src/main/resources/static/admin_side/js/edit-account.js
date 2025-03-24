// setTimeout(function () {
//     $(".alert").remove();
// }, 3000); // Hide alert after 3 seconds

var initialUserData = {};

$(document).ready(function () {
    initialUserData = {
        id: $("#userID").val() || null,
        fullName: $("#fullName").val().trim(),
        email: $("#email").val().trim(),
        dob: $("#dob").val().trim(),
        phone: $("#phone").val().trim(),
        role: $('#role').val().trim(),
        status: $("#status").val().trim(),
        recordNo: $("#recordNo").val()
    };
});

$("body").on("submit", "#userForm", function (event) {
    event.preventDefault();

    $(".error-message").html("");
    $(".text-danger").html("");

    var user = {
        id: $("#userID").val() || null,
        fullName: $("#fullName").val().trim(),
        email: $("#email").val().trim(),
        dob: $("#dob").val().trim(),
        phone: $("#phone").val().trim(),
        role: $('#role').val().trim(),
        status: $("#status").val().trim(),
        recordNo: $("#recordNo").val()
    };

    // So sánh dữ liệu nhập với dữ liệu ban đầu
    if (JSON.stringify(user) === JSON.stringify(initialUserData)) {
        alert("There are no changes to update.");
        return;
    }

    $.ajax({
        url: "/admin/api/save-user",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(user),
        success: function (response) {
            alert(response.message);
            $("#recordNo").val(response.recordNo);
        },
        error: function (xhr) {
            if (xhr.status === 409) {
                alert("The data has been modified by someone else. Please reload the page!");
                location.reload();
            }else if (xhr.status === 304) {
                // data has no change
                alert(xhr.responseJSON.message);
            }
            else {
                var errors = xhr.responseJSON;
                if (errors) {
                    $("#fullNameError").html(errors.fullNameError);
                    $("#emailError").html(errors.emailError);
                    $("#dobError").html(errors.dobError);
                    $("#phoneError").html(errors.phoneError);
                    $("#roleError").html(errors.roleError);
                    $("#statusError").html(errors.statusError);
                } else {
                    alert("An error occurred: " + xhr.responseText);
                }
            }
        }
    });
});

document.getElementById("cancel-button").addEventListener("click", function () {
    var previousUrl = localStorage.getItem('previousUrl');
    if (previousUrl) {
        window.location.href = previousUrl;
        localStorage.removeItem('previousUrl');
    } else {
        window.history.back();
    }
});



