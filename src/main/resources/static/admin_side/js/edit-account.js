// setTimeout(function () {
//     $(".alert").remove();
// }, 3000); // Hide alert after 3 seconds

var initialUserData = {};

$(document).ready(function () {
    var userID = $("#userID").val();

    // Check only if perform edit
    if (userID) {
        initialUserData = {
            id: userID,
            fullName: $("#fullName").val().trim(),
            email: $("#email").val().trim(),
            dob: $("#dob").val().trim(),
            phone: $("#phone").val().trim(),
            role: $('#role').val().trim(),
            status: $("#status").val().trim(),
            recordNo: $("#recordNo").val()
        };
    }
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

    // If performing edit , check has changes or not
    if (user.id && JSON.stringify(user) === JSON.stringify(initialUserData)) {
        showModalMessage("There are no changes to update.");
        return;
    }

    $.ajax({
        url: "/admin/api/save-user",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(user),
        success: function (response) {
            showModalMessage(response.message);
            $("#recordNo").val(response.recordNo);
        },
        error: function (xhr) {
            if (xhr.status === 409) {
                showModalMessage("The data has been modified by someone else. Please reload the page!");
                location.reload();
            } else if (xhr.status === 304) {
                showModalMessage(xhr.responseJSON.message);
            } else {
                var errors = xhr.responseJSON;
                if (errors) {
                    $("#fullNameError").html(errors.fullNameError);
                    $("#emailError").html(errors.emailError);
                    $("#dobError").html(errors.dobError);
                    $("#phoneError").html(errors.phoneError);
                    $("#roleError").html(errors.roleError);
                    $("#statusError").html(errors.statusError);
                } else {
                    showModalMessage("An error occurred: " + xhr.responseText);
                }
            }
        }
    });

    function showModalMessage(message) {
        $("#modalMessage").text(message);
        $("#notificationModal").modal("show");
    }

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




